package com.java.backend.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.entity.PageResult;
import com.java.common.entity.QueryPageBean;
import com.java.common.entity.Result;
import com.java.common.pojo.CheckGroup;
import com.java.service.CheckGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")

@Api(tags = "检查组管理后端接口")
public class CheckGroupController {

        @Reference
        private CheckGroupService checkGroupService;

        //新增
        @PostMapping("/add")
        @ApiOperation(value = "新增检查组")
        public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
            try {
                checkGroupService.add(checkGroup,checkitemIds);
            }catch (Exception e){
                //新增失败
                return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
            }
            //新增成功
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }

    //分页查询
    @PostMapping("/findPage")
    @ApiOperation(value = "分页查询检查组")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    //根据id查询
    @GetMapping("/findById/{id}")
    @ApiOperation(value = "根据id查询检查组")
    public Result findById(@PathVariable("id") Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if(checkGroup != null){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    //根据检查组合id查询对应的所有检查项id
    @GetMapping("/findCheckItemIdsByCheckGroupId/{id}")
    @ApiOperation(value = "根据检查组合id查询对应的所有检查项id检查组")
    public Result findCheckItemIdsByCheckGroupId(@PathVariable("id") Integer id){
        try{
            List<Integer> checkitemIds =
                    checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //编辑
    @PutMapping("/edit")
    @ApiOperation(value = "编辑检查组")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        if(checkGroupList != null && checkGroupList.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroupList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

}

