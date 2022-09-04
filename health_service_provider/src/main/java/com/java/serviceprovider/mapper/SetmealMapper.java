package com.java.serviceprovider.mapper;

import com.github.pagehelper.Page;
import com.java.common.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map<String, Integer> map);
    public Page<Setmeal> selectByCondition(String queryString);
    public List<Setmeal> findAll();
    public Setmeal findById(int id);
    public List<Map<String,Object>> findSetmealCount();
}
