package com.java.serviceprovider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.java.common.constant.RedisConstant;
import com.java.common.entity.PageResult;
import com.java.common.pojo.CheckGroup;
import com.java.common.pojo.Setmeal;
import com.java.redis.RedisOptBean;
import com.java.service.SetmealService;
import com.java.serviceprovider.mapper.CheckGroupMapper;
import com.java.serviceprovider.mapper.SetmealMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 3000)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CheckGroupMapper checkGroupMapper;

    @Autowired
    private RedisOptBean redisOptBean;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${outputPath}")//从属性文件读取输出目录的路径
    private String outputpath ;

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {

        setmealMapper.add(setmeal);
        /*
        if(checkgroupIds != null && checkgroupIds.length > 0){
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        this.setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        BoundSetOperations opt = redisTemplate.boundSetOps(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        opt.add(setmeal.getId());
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());*/

        Integer setmealId = setmeal.getId();//获取套餐id
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        //完成数据库操作后需要将图片名称保存到redis
        BoundSetOperations opt = redisTemplate.boundSetOps(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        opt.add(setmeal.getId());
        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());
        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }


    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        redisOptBean.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }


    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",id);
            map.put("checkgroupId",checkgroupId);
            setmealMapper.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmeals = setmealMapper.selectByCondition(queryString);
        return new PageResult(setmeals.getTotal(),setmeals.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealMapper.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealMapper.findSetmealCount();
    }
}
