package com.hzu.community.service;




public interface ModifyUserService {
//    管理员删除某用户所有数据
    public Boolean delAll(Integer userId) throws RuntimeException;
}
