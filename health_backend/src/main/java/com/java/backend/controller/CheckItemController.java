package com.java.backend.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.entity.PageResult;
import com.java.common.entity.QueryPageBean;
import com.java.common.entity.Result;
import com.java.common.pojo.CheckItem;
import com.java.service.CheckItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkitem")

@Api(tags = "检查项管理后端接口")
public class CheckItemController {

    @Reference(retries = 0)
    private CheckItemService checkItemService;

    //新增
    @PostMapping("/add")
    @ApiOperation(value = "新增检查项")
    public Result add(@RequestBody @ApiParam(name = "checkItem",value = "新增检查项对象") CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //分页查询
    @PostMapping("/findPage")
    @ApiOperation(value = "分页查询检查项")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    //删除
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除检查项")
    public Result delete(@PathVariable("id") Integer id){
        try {
            checkItemService.delete(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e){
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //编辑
    @PutMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable("id") Integer id){
        try{
            CheckItem checkItem = checkItemService.findById(id);
            return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //查询所有
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        if(checkItemList != null && checkItemList.size() > 0){
            Result result = new Result(true,
                    MessageConstant.QUERY_CHECKITEM_SUCCESS,
                    checkItemList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }

}

