package com.hzu.community.service.impl;

import com.hzu.community.bean.Salary;
import com.hzu.community.mapper.SalaryMapper;
import com.hzu.community.service.SalaryService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {
    @Autowired
    private SalaryMapper salaryMapper;
    @Override
    public List<Salary> getSalaryList() {
        return salaryMapper.getSalaryList();
    }

    @Override
    public Salary findSalaryById(Integer id) {
        return salaryMapper.findSalaryById(id);
    }
}
