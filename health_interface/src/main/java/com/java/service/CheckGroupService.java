package com.java.service;

import com.java.common.entity.PageResult;
import com.java.common.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组服务接口
 */
public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds);
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
    CheckGroup findById(Integer id);
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    public void edit(CheckGroup checkGroup,Integer[] checkitemIds);
    List<CheckGroup> findAll();
}
