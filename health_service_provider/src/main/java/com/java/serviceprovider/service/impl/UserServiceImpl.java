package com.java.serviceprovider.service.impl;

import com.java.common.pojo.Menu;
import com.java.common.pojo.Permission;
import com.java.common.pojo.Role;
import com.java.common.pojo.User;
import com.java.redis.RedisOptBean;
import com.java.service.UserService;
import com.java.serviceprovider.mapper.MenuMapper;
import com.java.serviceprovider.mapper.PermissionMapper;
import com.java.serviceprovider.mapper.RoleMapper;
import com.java.serviceprovider.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service(timeout = 3000)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisOptBean redisOptBean;

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleMapper.findByUserId(userId);
        if(roles != null && roles.size() > 0){
            for(Role role : roles){
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionMapper.findByRoleId(roleId);
                if(permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);
                }

                //Redis缓存一级菜单
                String keyword = role.getKeyword();
                long count = redisOptBean.llen(keyword);
                if(count == 0){
                    List<Menu> menus = menuMapper.selectByRoleid(role.getId());

                    List<Menu> firstMenus = menus.stream().filter(menu -> {
                        return menu.getParentMenuId() == null;
                    }).collect(Collectors.toList());

                    for (Menu firstMenu : firstMenus) {
                        List<Menu> secondMenus = menus.stream().filter(menu -> {
                            if (menu.getParentMenuId() == firstMenu.getId()){
                                return true;
                            }
                            return false;
                        }).collect(Collectors.toList());
                                firstMenu.setChildren(secondMenus);
                                redisOptBean.rpush(keyword,firstMenu,60*60*24*10);
                    }
                }

            }
            user.setRoles(roles);
        }
        return user;
    }
}
