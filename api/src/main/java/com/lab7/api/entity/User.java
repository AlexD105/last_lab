package com.lab7.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.Locale;

@Data
@NoArgsConstructor
public class User implements Serializable {

    private String name;
    private String password;
    private String address;

    public User(String name, String password) {
        this.name = name;
        this.password = DigestUtils.md2Hex(password).toUpperCase(Locale.ROOT);
    }
}