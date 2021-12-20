package com.tanhua.server.test.cache;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoTestService {


    /*
    1,@Cacheable`修饰的方法返回的对象，一定要实现序列化`Serializable`接口，
    否则会报`java.io.NotSerializableException`异常。
    2,要使用@Cacheable 必须在先开启：在启动类上加注解@EnableCaching
    3，
     */
    @Cacheable(value = "userInfos")
    public List<UserInfo> findAll(){
        System.out.println("进入findAll 函数");
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {

            UserInfo userInfo = new UserInfo();
            userInfo.setId((long)i);
            userInfo.setAge(18 + i);
            userInfo.setNickname("zhsnghan"+i);
            list.add(userInfo);
        }
        return list;
    }

    //移除缓存中的数据
    //增删改的时候需要移除缓存，方便后续重新生成缓存
    @CacheEvict(value = "userInfos")
    public void update(){

    }

    @Cacheable(value = "userInfo",key = "#id")
    public UserInfo findByid(Long id){
        System.out.println("进入findByid");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAge(18 );
        userInfo.setNickname("zhsnghan"+id);
        return userInfo;
    }


    //@CacheEvict(value = "userInfo",key = "#p0.id") p0 表示param 0 即第一个参数
    @CacheEvict(value = "userInfo",key = "#userInfo.id")
    public void delete(UserInfo userInfo){

    }

    //allEntries = true 表示删除所有缓存
    @CacheEvict(value = "userInfo",allEntries = true)
    public void deleteAll(){

    }
}
