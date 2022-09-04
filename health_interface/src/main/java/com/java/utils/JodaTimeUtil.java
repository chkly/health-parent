package com.java.utils;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class JodaTimeUtil {
    /**
     * 获取过去的num个月的日期字符串
     *
     * @param num
     * @return
     */
    public static List<String> getBeforeMonths(int num) {
        LocalDate localDate = LocalDate.now();
        List<String> list = new ArrayList<>();
        LocalDate beforeDate = null;
        for (int i = num; i >= 1; i--) {
            beforeDate = localDate.minusMonths(i);
            list.add(beforeDate.toString("yyyy-MM"));
        }
        return list;
    }
}
