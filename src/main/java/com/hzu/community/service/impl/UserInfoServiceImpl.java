package com.hzu.community.service.impl;


import com.hzu.community.bean.UserInfo;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.UserExecution;

import com.hzu.community.enums.UserEnum;

import com.hzu.community.exceptions.UserException;
import com.hzu.community.exceptions.UserException;
import com.hzu.community.mapper.UserInfoMapper;
import com.hzu.community.service.UserInfoService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Override
    @Transactional
    public UserExecution save(UserInfo user, ImageHolder imageHolder) throws UserException {
        try {
                 /*因为add方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到userId
                 该值作为更新图片url的参数*/
            int insertNum = userInfoMapper.register(user);

            if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                throw new UserException("用户数据插入失败");
            } else {
                if (imageHolder != null) {
                    try {
//                      对图片进行处理，并把url设置在文章中
                        addUserImg(user,imageHolder);
                    } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                        throw new UserException("addImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                    }
//                        更新图片url
                    int updateImg = userInfoMapper.update(user);
                    if (updateImg<=0){
                        throw new UserException("更新图片地址失败");
                    }
                }



            }
            
        }catch (Exception e){
            throw new UserException(e.getMessage());
        }
        return new UserExecution(UserEnum.SUCCESS,user);
    }

    @Override
    @Transactional
    public UserExecution update(UserInfo user, ImageHolder imageHolder) throws UserException {
        try {
//            判断是否需要处理图片
            if(imageHolder != null){
//            若存在图片，则取出相应文章
               UserInfo oldUser = userInfoMapper.findUserInfoById(user.getUserId());
                if (oldUser.getUserImg()!= null){
//                      根据文章的图片url删除本地下载的图片
                    ImageUtil.deleteFileOrpath(oldUser.getUserImg());
                }
                addUserImg(user,imageHolder);
            }


            int updateNum = userInfoMapper.update(user);
            if (updateNum<=0){
                return new UserExecution(UserEnum.UPDATE_WRONG);
            }else {
                return new UserExecution(UserEnum.SUCCESS);
            }

        }catch (Exception e){
            throw new UserException(e.getMessage());
        }
    }
    private void addUserImg(UserInfo user, ImageHolder imageHolder){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getUserImagePath(user);
        String UserAddr = ImageUtil.generateThumbnail(imageHolder,dest);//处理图片。并返回图片地址
        user.setUserImg(UserAddr);//设置图片地址

    }
}