package com.tanhua.server.utils;

import com.tanhua.domain.db.User;

//将user 对象绑定到该线程上，方便获取
public class UserHolder {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    /*
    对象绑定到当前线程上
     */
    public static void set(User user){
        threadLocal.set(user);
    }

    public static User get(){
        return threadLocal.get();
    }

    public static Long getUserId(){
        return  get().getId();
    }
}
