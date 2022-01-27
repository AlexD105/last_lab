package com.lab7.server;

import com.lab7.api.command.Exit;
import com.lab7.api.command.Save;
import com.lab7.api.command.manager.CommandManager;
import com.lab7.api.i18n.Messenger;
import com.lab7.api.i18n.MessengerFactory;
import com.lab7.api.entity.User;
import com.lab7.api.message.MessageReq;
import com.lab7.api.message.MessageResp;
import com.lab7.api.print.implementation.FormatterImpl;
import com.lab7.api.print.implementation.PrinterImpl;
import com.lab7.api.dao.DragonDao;
import com.lab7.api.dao.UserDao;
import com.lab7.api.service.DragonService;
import com.lab7.api.service.UserService;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);
    private static final Messenger messenger = MessengerFactory.getMessenger();

    private ExecutorService requestExecutor;
    private ExecutorService responseExecutor;

    private ServerSocketChannel serverSocket;
    private Selector selector;
    private final ServerHelper serverHelper;

    private List<User> users;

    // Используем ReadWriteLock для обеспечения потокобезопасности использования коллекции users
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Server(ServerHelper serverHelper) {
        this.serverHelper = serverHelper;
    }

    public void init() throws IOException {
        // Получаем Selector
        selector = Selector.open();

        // Получаем ServerSocketChannel, задаем ему адрес, а также регистрируем полученный Selector
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        serverHelper.init();

        // Создаем три ExecutorService для принятия, обработки и послания запроса соответственно
        this.requestExecutor = Executors.newCachedThreadPool();
        this.responseExecutor = Executors.newCachedThreadPool();

        this.users = serverHelper.getUserService().findAll();

        logger.info("Server initialized");

        // Запускаем новый поток, который слушает пользовательский ввод
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            String serverCommand;
            while (!(serverCommand = sc.nextLine()).equals("exit")) {
                if ("save".equals(serverCommand)) {
                    System.out.println(new Save(serverHelper.getMDataSet(), serverHelper.getDragonService()).execute(null));
                } else {
                    System.out.println("Неизвестная команда: " + serverCommand);
                }
            }
            new Exit(serverHelper.getMDataSet(), serverHelper.getDragonService()).execute(null);
        }).start();

    }

    public void start() throws IOException {

        init();

        System.out.println(messenger.getMessage("greeting_sever"));

        while (serverSocket.isOpen()) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }
                if (key.isReadable()) {
                    try {
                        getRequest(key);
                    } catch (Exception e) {
                        key.cancel();
                        System.err.println(e.getMessage());
                        key.channel().close();
                    }
                }
                iter.remove();
            }
        }
    }

    private void getRequest(SelectionKey key) throws Exception {
        // Получаем SocketChannel, через который мы будем обмениваться данными (через ByteBuffer) с конкретным клиентом
        SocketChannel client = (SocketChannel) key.channel();

        // Инициализируем ByteBuffer
        ByteBuffer requestBuffer = ByteBuffer.allocate(1024 * 1024);
        requestBuffer.clear();

        // Обрабатываем входящий запрос с помощью cachedThreadPool
        // В качестве параметра методу submit передается не Runnable, а Callable объект
        // Callable может возвращать значение, в отличие от Runnable
        // Внутри Callable мы считываем данные от клиента и возвращаем количество считанных байтов
        Future<Integer> readFuture = requestExecutor.submit(() -> client.read(requestBuffer));

        // Получаем значение из Future, как только оно готово
        int read = readFuture.get();

        logger.info("New request from " + ((SocketChannel) key.channel()).socket().getRemoteSocketAddress());

        // Если полученное значение равно -1, то клиент по каким-то
        // причинам потерял связь (Просто отключился или возникла ошибка)
        if(read == -1) {
            throw new Exception("Соединение с одним из клиентов прервано: " +
                    ((SocketChannel) key.channel()).socket().getRemoteSocketAddress());
        }

        byte[] bytes = new byte[read];
        requestBuffer.position(0);
        requestBuffer.get(bytes);

        // Десериализуем Message и, исходя из команды, запускаем
        // авторизацию, регистрацию, либо обычное выполнение команды
        MessageReq message = SerializationUtils.deserialize(bytes);
        AtomicReference<MessageResp> response = new AtomicReference<>(new MessageResp());

        // Выполняем необходимую команду в новом потоке
        new Thread(() -> {
            switch (message.getCommand()) {
                case "login": response.set(login(client, message)); break;
                case "registration": response.set(registration(client, message)); break;
                default:
                    try {
                        response.set(serverHelper.getCommandManager().executeCommand(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }).start();

        // Отправляем результат в новом потоке с помощью cachedThreadPool
        responseExecutor.submit(() -> {
            try {
                sendResponse(client, response.get());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendResponse(SocketChannel client, MessageResp message) throws IOException {
        // Сериализуем Message и заворачиваем его в ByteBuffer
        // Отправляем результат клиенту
        ByteBuffer responseBuffer = ByteBuffer.wrap(SerializationUtils.serialize(message));
        while (responseBuffer.hasRemaining()) {
            client.write(responseBuffer);
        }

        logger.info("Response sent to " + client.socket().getRemoteSocketAddress());

        responseBuffer.clear();
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        // Принимаем клиента
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        // Регистрируем Selector для этого клиента
        client.register(selector, SelectionKey.OP_READ);
        logger.info("New connection at: " + client.socket().getRemoteSocketAddress());
    }

    public MessageResp login(SocketChannel client, MessageReq message) {

        MessageResp result = new MessageResp();

        result.setResult(ServerHelper.FL);

        readLock.lock();
        try {
            for (User u : users) {
                if (u.getName().equals(message.getUser().getName()) && u.getPassword().equals(message.getUser().getPassword())) {
                    result.setResult(ServerHelper.SL);
                    u.setAddress(client.socket().getRemoteSocketAddress().toString());
                    System.out.println("User authorized: " + u);
                    break;
                }
            }
        } finally {
            readLock.unlock();
        }

        return result;
    }

    public MessageResp registration(SocketChannel client, MessageReq message) {

        MessageResp result = new MessageResp();

        result.setResult(ServerHelper.SR);
        writeLock.lock();
        try {
            for (User u : users) {
                if (u.getName().equals(message.getUser().getName())) {
                    result.setResult(ServerHelper.FR);
                    break;
                }
            }
            if (result.getResult().equals(ServerHelper.SR)) {
                message.getUser().setAddress(client.socket().getRemoteSocketAddress().toString());
                serverHelper.getUserService().save(message.getUser());
                users.add(message.getUser());
                System.out.println("New user: " + message.getUser());
            }
        } finally {
            writeLock.unlock();
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        DragonService dragonService = new DragonService(new DragonDao());
        UserService userService = new UserService(new UserDao());

        new Server(
                new ServerExecutorBuilder()
                        .setDataSet(dragonService.findAll())
                        .setCommandManager(new CommandManager())
                        .setFormatter(new FormatterImpl())
                        .setPrinter(new PrinterImpl())
                        .setDragonService(dragonService)
                        .setUserDao(userService)
                        .build()
        ).start();

    }

}