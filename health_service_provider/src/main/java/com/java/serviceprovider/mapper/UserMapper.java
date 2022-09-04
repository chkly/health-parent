package com.java.serviceprovider.mapper;

import com.java.common.pojo.User;

public interface UserMapper {
    public User findByUsername(String username);
}
