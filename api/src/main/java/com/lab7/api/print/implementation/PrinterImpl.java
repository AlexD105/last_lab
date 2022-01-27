package com.lab7.api.print.implementation;

import com.lab7.api.print.api.Printer;

public class PrinterImpl implements Printer {
    public void printResult(String result) {
        if(result != null) {
            System.out.println(result);
        }
    }
}