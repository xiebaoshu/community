package com.hzu.community.service;

import com.hzu.community.bean.Salary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaryService {
    public List<Salary> getSalaryList();
    public Salary findSalaryById(@Param("id") Integer id);
}
