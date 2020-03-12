package com.hzu.community.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    public static int getInt(HttpServletRequest request,String key)
    {
        try{
            return Integer.decode(request.getParameter(key));
        }catch(Exception e){
            return -1;
        }
    }
    public static long getLong(HttpServletRequest request,String key)
    {
        try{
            return Long.decode(request.getParameter(key));
        }catch(Exception e){
            return -1L;
        }
    }
    public static double getDouble(HttpServletRequest request,String key)
    {
        try{
            return Double.valueOf(request.getParameter(key));
        }catch(Exception e){
            return -1d;
        }
    }
    public static boolean getBoolean(HttpServletRequest request,String key)
    {
        try{
            return Boolean.valueOf(request.getParameter(key));
        }catch(Exception e){
            return false;
        }
    }
    public static String getString(HttpServletRequest request, String key)
    {
        try{
            String result = request.getParameter(key);//从reques中取出属性
            if(result !=null){							//对属性进行处理
                result = result.trim();//去除2侧空格

            }
            if("".equals(result)){
                result = null;
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }
}
