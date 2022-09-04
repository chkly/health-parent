package com.java.serviceprovider.service.impl;

import com.java.common.pojo.Member;
import com.java.service.MemberService;
import com.java.serviceprovider.mapper.MemberMapper;
import com.java.utils.MD5Util;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(timeout = 3000)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    //新增会员
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Util.encrypt(member.getPassword()));
        }
        memberMapper.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    //根据月份统计会员数量
    public List<Integer> findMemberCountByMonth(List<String> month) {
        List<Integer> list = new ArrayList<>();
        for(String m : month){
            m = m + ".31";//格式：2019.04.31
            Integer count = memberMapper.findMemberCountBeforeDate(m);
            list.add(count);
        }
        return list;
    }
}

