package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.domain.db.UserInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**自定义sql，封装分页参数
     * 如果方法参数中传了分页参数 IPage<> 会自动分页
     * @Param("userId") 如果有多个参数，需要使用@Param绑定
     * @param iPage
     * @param userId
     * @return
     */
    @Select("SELECT info.* FROM `tb_user_info` info,`tb_black_list` b " +
            "WHERE info.id = b.black_user_id AND b.user_id = #{userId}")
    IPage<UserInfo> blackList(IPage<UserInfo> iPage,@Param("userId") Long userId);

    @Delete("DELETE FROM `tb_black_list` WHERE user_id=#{uid} AND black_user_id=#{bUid}")
    void deleteBlackByUid(@Param("uid") Long uid,@Param("bUid") Long bUid);


}