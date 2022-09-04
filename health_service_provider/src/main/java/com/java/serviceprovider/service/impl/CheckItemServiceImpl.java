package com.java.serviceprovider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.java.common.entity.PageResult;
import com.java.common.pojo.CheckItem;
import com.java.service.CheckItemService;
import com.java.serviceprovider.mapper.CheckItemMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(timeout = 3000)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Override
    public void add(CheckItem checkItem) {
        checkItemMapper.add(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //删除
    public void delete(Integer id) throws RuntimeException{
        //查询当前检查项是否和检查组关联
        long count = checkItemMapper.findCountByCheckItemId(id);
        if(count > 0){
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemMapper.deleteById(id);
    }

    @Override
    //编辑
    public void edit(CheckItem checkItem) {
        checkItemMapper.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }
}
