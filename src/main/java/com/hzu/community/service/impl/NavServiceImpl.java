package com.hzu.community.service.impl;

import com.hzu.community.bean.Nav;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.NavExecution;
import com.hzu.community.enums.NavEnum;
import com.hzu.community.exceptions.NavException;
import com.hzu.community.mapper.NavMapper;
import com.hzu.community.service.NavService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void del(Integer id) {
        navMapper.del(id);
    }

    @Override
    @Transactional
    public NavExecution diySave(Nav nav, ImageHolder imageHolder) throws NavException {
        if (nav == null) {
            return new NavExecution(NavEnum.NULL_Nav);
        }else if (nav.getName() == null){
            return new NavExecution(NavEnum.NULL_Name);
        }else if (nav.getUrl() == null){
            return new NavExecution(NavEnum.NULL_Url);
        }
        Nav nav1 = navMapper.findByName(nav);
        if (nav1 != null){
            return new NavExecution(NavEnum.REPEACT_NAV);
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
        return new NavExecution(NavEnum.SUCCESS,nav);
    }

    private void addNavImg(Nav nav, ImageHolder ImageHolder){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getNavImagePath(nav.getUserId(),nav.getId());
        String navAddr = ImageUtil.generateThumbnail(ImageHolder,dest);//处理图片。并返回图片地址
        nav.setImgUrl(navAddr);//设置图片地址

    }
}
