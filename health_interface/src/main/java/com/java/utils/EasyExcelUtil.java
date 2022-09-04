package com.java.utils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lujun
 * @Description
 * @Date 2021/11/22 11:49
 */
public class EasyExcelUtil{
    public static <T> List<T> readExcel(InputStream stream, Class<T> tClass, int sheetNo) {
        List<T> list = new ArrayList<>();
        EasyExcel.read(stream)
                //反射获取类型
                .head(tClass)
                //读取的excel的sheet索引
                .sheet(sheetNo)
                //注册监听器
                .registerReadListener(new AnalysisEventListener<T>() {
                    @Override
                    public void invoke(T t, AnalysisContext analysisContext) {
                        list.add(t);
                    }
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        System.out.println("读取数据完毕");
                    }
                }).doRead();
        return list;
    }
}
