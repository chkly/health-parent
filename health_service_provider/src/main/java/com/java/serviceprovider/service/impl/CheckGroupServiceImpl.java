package com.java.serviceprovider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.java.common.entity.PageResult;
import com.java.common.pojo.CheckGroup;
import com.java.service.CheckGroupService;
import com.java.serviceprovider.mapper.CheckGroupMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(timeout = 3000)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;
    @Autowired private CheckGroupService checkGroupService;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.add(checkGroup);
        checkGroupService.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupMapper.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    //???????????????????????????????????????????????????????????????
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //???????????????id???????????????????????????????????????????????????
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        //????????????(t_checkgroup_checkitem)?????????????????????????????????????????????????????????
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //???????????????????????????
        checkGroupMapper.edit(checkGroup);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }
}

