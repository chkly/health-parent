package com.java.serviceprovider.mapper;

import com.java.common.pojo.Menu;

import java.util.List;
import java.util.Set;

/**
 * 操作菜单表
 */
public interface MenuMapper {
    //Set<Menu> selectByRoleid(int id);
    List<Menu> selectByRoleid(int id);
}
