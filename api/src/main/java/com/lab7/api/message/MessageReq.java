package com.lab7.api.message;

import com.lab7.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MessageReq implements Serializable {

    private final long version = 1L;

    private User user;
    private String command;
}
