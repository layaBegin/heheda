package com.tanhua.manage.Utils;

import com.tanhua.domain.db.User;
import com.tanhua.manage.pojo.Admin;

//将user 对象绑定到该线程上，方便获取
public class AdminHolder {
    private static ThreadLocal<Admin> threadLocal = new ThreadLocal<>();

    /*
    对象绑定到当前线程上
     */
    public static void set(Admin admin){
        threadLocal.set(admin);
    }

    public static Admin get(){
        return threadLocal.get();
    }

    public static Long getAdminId(){
        return  get().getId();
    }
}
