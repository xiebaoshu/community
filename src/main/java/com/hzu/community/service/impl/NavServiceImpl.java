package com.hzu.community.service.impl;

import com.hzu.community.bean.JobArticle;
import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;

import com.hzu.community.dto.ImageHolder;

import com.hzu.community.enums.NavEnum;
import com.hzu.community.exceptions.NavException;
import com.hzu.community.mapper.NavMapper;
import com.hzu.community.service.NavService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NavServiceImpl implements NavService {
    @Autowired
    private NavMapper navMapper;
    @Override
    public List<Nav> navDefault() {
        return navMapper.navDefault();
    }

    @Override
    public List<Nav> navDiy(UserInfo userInfo) {
        return navMapper.navDiy(userInfo);
    }

    @Override
    public int add(Nav nav) {
        return navMapper.add(nav);
    }

    @Override
    public Nav findByName(Nav nav) {
        return navMapper.findByName(nav);
    }

    @Override
    public Nav findDefaultByName(Nav nav) {
        return navMapper.findDefaultByName(nav);
    }

    @Override
    public Nav findDefaultById(Nav nav) {
        return navMapper.findDefaultById(nav);
    }

    @Override
    public void del(Integer id) {
        navMapper.del(id);
    }

    @Override
    public void delDefault(Integer id) {
        navMapper.delDefault(id);
    }

    @Override
    @Transactional
    public NavEnum diySave(Nav nav, ImageHolder imageHolder) throws NavException {
        if (nav == null) {
            return NavEnum.NULL_Nav;
        }else if (nav.getName() == null){
            return NavEnum.NULL_Name;
        }else if (nav.getUrl() == null){
            return NavEnum.NULL_Url;
        }
        Nav nav1 = navMapper.findByName(nav);
        if (nav1 != null){
            return NavEnum.REPEACT_NAV;
        }
        try {
                /*因为add方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到id
                 该值作为更新图片url的参数*/
            if (imageHolder == null) {
//                如果没有图片则设置默认图片
                nav.setImgUrl("/upload/ico128.png");
            }
            int insertNum = navMapper.add(nav);
            if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                throw new NavException("数据插入失败");
            } else {
                if (imageHolder != null) {
                    try {
//                            对图片进行处理，并把url设置在文章中
                        addNavImg(nav, imageHolder);
                    } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                        throw new NavException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                    }
//                        更新图片url
                    int updateImg = navMapper.update(nav);
                    if (updateImg<=0){
                        throw new NavException("更新图片地址失败");
                    }
                }



            }
        }catch(Exception e){
            throw new NavException(e.getMessage());

        }
        return NavEnum.SUCCESS;
    }

    @Override
    @Transactional
    public NavEnum defaultSave(Nav nav, ImageHolder imageHolder) throws NavException {
        if (nav == null) {
            return NavEnum.NULL_Nav;
        }else if (nav.getName() == null){
            return NavEnum.NULL_Name;
        }else if (nav.getUrl() == null){
            return NavEnum.NULL_Url;
        }else if (nav.getDescription() == null){
            return NavEnum.NULL_Description;
        }
        Nav nav1 = navMapper.findDefaultByName(nav);
        if (nav1 != null){
            return NavEnum.REPEACT_NAV;
        }
        try {

            if (imageHolder == null) {
//                如果没有图片则设置默认图片
                nav.setImgUrl("/upload/ico128.png");
            }
            int insertNum = navMapper.addDefault(nav);
            if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                throw new NavException("数据插入失败");
            } else {
                if (imageHolder != null) {
                    try {
//                            对图片进行处理，并把url设置在文章中
                        addNavDefaultImg(nav, imageHolder);
                    } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                        throw new NavException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                    }
//                        更新图片url
                    int updateImg = navMapper.updateDefault(nav);
                    if (updateImg<=0){
                        throw new NavException("更新图片地址失败");
                    }
                }



            }
        }catch(Exception e){
            throw new NavException(e.getMessage());

        }
        return NavEnum.SUCCESS;
    }


    @Override
    @Transactional
    public NavEnum defaultUpdate(Nav nav, ImageHolder imageHolder) throws NavException {
        if (nav == null) {
            return NavEnum.NULL_Nav;
        } else if (nav.getName() == null) {
            return NavEnum.NULL_Name;
        } else if (nav.getUrl() == null) {
            return NavEnum.NULL_Url;
        } else if (nav.getDescription() == null) {
            return NavEnum.NULL_Description;
        }
        //        修改后的名字是否存在，并判断若存在，是否为本身
        Nav nav1 = navMapper.findDefaultByName(nav);
        if (nav1!=null && nav1.getId() != nav.getId()) {
            return NavEnum.REPEACT_NAV;
        }
        try {
//            判断是否需要处理图片
            if (imageHolder != null) {
//            若存在图片，则取出相应文章
                Nav oldNav = navMapper.findDefaultById(nav);
                if (oldNav.getImgUrl() != null && oldNav.getImgUrl() !="/upload/ico128.png") {
//                      根据导航的图片url删除本地下载的图片
                    ImageUtil.deleteFileOrpath(oldNav.getImgUrl());
                }
//                设置图片地址
                addNavDefaultImg(nav, imageHolder);
            }
//            更新操作

            int updateNum = navMapper.updateDefault(nav);
            if (updateNum<=0){
                return NavEnum.UPDATE_WRONG;
            }else {
//
                return NavEnum.SUCCESS;
            }


        }catch(Exception e){
            throw new NavException(e.getMessage());

        }

    }


    private void addNavImg(Nav nav, ImageHolder ImageHolder){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getNavImagePath(nav.getUserId(),nav.getId());
        String navAddr = ImageUtil.generateThumbnail(ImageHolder,dest);//处理图片。并返回图片地址
        nav.setImgUrl(navAddr);//设置图片地址

    }

    private void addNavDefaultImg(Nav nav, ImageHolder ImageHolder){
        //获取图片目录的相对值路径

        String dest = PathUtil.getNavDefaultImagePath();
        String navAddr = ImageUtil.generateThumbnail(ImageHolder,dest);//处理图片。并返回图片地址
        nav.setImgUrl(navAddr);//设置图片地址

    }
}
