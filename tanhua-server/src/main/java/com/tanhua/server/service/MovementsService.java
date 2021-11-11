package com.tanhua.server.service;



import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.db.Publish;
import com.tanhua.domain.mongo.vo.MovementsVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.server.utils.RelativeDateFormat;
import com.tanhua.server.utils.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovementsService {
    @Autowired
    private OssTemplate ossTemplate;
    @Reference
    private PublishApi publishApi;
    @Reference
    private UserInfoApi userInfoApi;
    ;
    /**
     * 接口名称：动态-发布
     */
    public ResponseEntity<Object> saveMovements(Publish publish, MultipartFile[] imageContent) throws IOException {
        //1. 获取登陆用户id
        Long userId = UserHolder.getUserId();

        //2. 处理文件上传，并封装上传的图片数组对象
        List<String> medias = new ArrayList<>();
        if (imageContent != null) {
            for (MultipartFile multipartFile : imageContent) {
                String url = ossTemplate.upload(
                        multipartFile.getOriginalFilename(), multipartFile.getInputStream());
                medias.add(url);
            }
        }

        //3. 设置参数
        publish.setMedias(medias);
        publish.setUserId(userId);

        //4. （发布）保存动态
        publishApi.save(publish);
        return ResponseEntity.ok(null);
    }

    /**
     * 接口名称：好友动态
     */
    public ResponseEntity<Object> queryPublishList(Integer page, Integer pagesize) {
        //1. 获取登陆用户id
        Long userId = UserHolder.getUserId();

        //2. 分页查询好友动态（自己的时间线表）
        PageResult pageResult = publishApi.findByTimeLine(page,pagesize,userId);
        List<Publish> publishList = (List<Publish>) pageResult.getItems();

        //3. 封装返回的结果MovementsVo集合
        //3.1 创建返回结果
        List<MovementsVo> voList = new ArrayList<>();
        //3.2 遍历查询的发布动态集合
        if (publishList != null) {
            for (Publish publish : publishList) {
                //3.2.1 创建vo对象
                MovementsVo vo = new MovementsVo();
                //3.2.2 封装数据：发布动态
                BeanUtils.copyProperties(publish,vo);
                //3.2.3 封装数据：先查询用户详情，再封装
                UserInfo userInfo = userInfoApi.findById(publish.getUserId());
                if (userInfo != null) {
                    BeanUtils.copyProperties(userInfo,vo);
                    if (userInfo.getTags() != null) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                //3.2.3 封装数据：其他参数
                vo.setId(publish.getId().toString());
                vo.setUserId(publish.getUserId());
                vo.setImageContent(publish.getMedias().toArray(new String[]{}));
                vo.setDistance("50米");
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));

                vo.setHasLiked(0);
                vo.setHasLoved(0);

                //3.2.4 对象添加到集合
                voList.add(vo);
            }
        }

        //4. 在把封装好的vo集合设置到分页对象中
        pageResult.setItems(voList);
        return ResponseEntity.ok(pageResult);
    }
}
