package com.java.serviceprovider.mapper;

import com.github.pagehelper.Page;
import com.java.common.pojo.CheckGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CheckGroupMapper {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);
    public Page<CheckGroup> selectByCondition(String queryString);
    CheckGroup findById(Integer id);
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    void deleteAssociation(Integer id);
    void edit(CheckGroup checkGroup);
    List<CheckGroup> findAll();
}
