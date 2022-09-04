package com.java.serviceprovider.mapper;

import com.java.common.pojo.Role;

import java.util.Set;

public interface RoleMapper {
    public Set<Role> findByUserId(int id);
}
