package com.java.serviceprovider.mapper;

import com.github.pagehelper.Page;
import com.java.common.pojo.CheckItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CheckItemMapper {
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByCondition(String queryString);
    public void deleteById(Integer id);
    public long findCountByCheckItemId(Integer checkItemId);
    public void edit(CheckItem checkItem);
    public CheckItem findById(Integer id);
    public List<CheckItem> findAll();

}
