package com.java.service;

import com.java.common.pojo.User;

public interface UserService {
    public User findByUsername(String username);
}
