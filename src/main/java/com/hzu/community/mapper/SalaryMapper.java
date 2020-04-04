package com.hzu.community.mapper;

import com.hzu.community.bean.Salary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SalaryMapper {
    @Select("select * from salary")
    public List<Salary> getSalaryList();

    @Select("select * from salary where id=#{id}")
    public Salary findSalaryById(@Param("id") Integer id);
}
