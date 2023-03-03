package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.bo.BasePageParam;
import com.zhf.common.enumType.SubscribeStateEnum;
import com.zhf.common.exception.Asserts;
import com.zhf.webfont.bo.UserSubscribeNotificationParam;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.User;
import com.zhf.webfont.po.UserSubscribe;
import com.zhf.webfont.service.NotificationUnreadService;
import com.zhf.webfont.service.UserSubscribeService;
import com.zhf.webfont.mapper.UserSubscribeMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* @author 10276
* @description 针对表【wf_user_subscribe】的数据库操作Service实现
* @createDate 2023-02-03 01:32:31
*/
@Service
public class UserSubscribeServiceImpl extends ServiceImpl<UserSubscribeMapper, UserSubscribe>
    implements UserSubscribeService{

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserSubscribeMapper userSubscribeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private NotificationUnreadService notificationUnreadService;

    @Override
    public void subscribeUser(String userId) {
        checkCanSubscribe(userId);

        UserSubscribe userSubscribe = getUserSubscribe(userId);
        User user = jwtTokenUtil.getCurrentUserFromHeader();

        if (userSubscribe == null){
            UserSubscribe userSubscribe1 = new UserSubscribe();
            userSubscribe1.setBeSubscribedId(userId);
            userSubscribe1.setSubscribedId(user.getUuid());
            userSubscribe1.setCreateTime(new Date());
            userSubscribe1.setUpdateTime(new Date());
            userSubscribe1.setIsDel(SubscribeStateEnum.SUBSCRIBE.getValue());
            userSubscribeMapper.insert(userSubscribe1);
        }else{
            userSubscribe.setIsDel(SubscribeStateEnum.SUBSCRIBE.getValue());
            userSubscribe.setUpdateTime(new Date());
            userSubscribeMapper.updateById(userSubscribe);
        }

        notificationUnreadService.addFocusUnreadCount(userId);
    }

    @Override
    public void unSubscribeUser(String userId) {
        checkCanSubscribe(userId);

        UserSubscribe userSubscribe = getUserSubscribe(userId);

        if (userSubscribe != null){
            userSubscribe.setIsDel(SubscribeStateEnum.UNSUBSCRIBE.getValue());
            userSubscribe.setUpdateTime(new Date());
            userSubscribeMapper.updateById(userSubscribe);
        }
    }

    @Override
    public UserSubscribe getNowUserSubscribe(String userId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        if (user == null){
            return null;
        }else{
            return getUserSubscribe(userId);
        }
    }

    @Override
    public Map<String, Object> getUserSubscribeNotification(BasePageParam pageParam) {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();

        IPage<UserSubscribe> iPage = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        IPage<UserSubscribe> page = userSubscribeMapper.selectFocusPageByCurrentUser(iPage,curUser.getUuid(),SubscribeStateEnum.SUBSCRIBE.getValue());
        List<UserSubscribeNotificationParam> result = new ArrayList<>();
        for (UserSubscribe record : page.getRecords()) {
            UserSubscribeNotificationParam param = new UserSubscribeNotificationParam();
            User user = userMapper.selectById(record.getSubscribedId());
            param.setUserId(user.getUuid());
            param.setAvatar(user.getAvatar());
            param.setUsername(user.getUsername());
            param.setUserSubscribe(record);

            QueryWrapper<UserSubscribe> wrapper = new QueryWrapper<>();
            wrapper.eq("be_subscribed_id",user.getUuid())
                    .eq("subscribed_id",curUser.getUuid());
            UserSubscribe userSubscribe = userSubscribeMapper.selectOne(wrapper);
            if (userSubscribe != null && userSubscribe.getIsDel().equals(SubscribeStateEnum.SUBSCRIBE.getValue())){
                param.setIsFocus(true);
            }else{
                param.setIsFocus(false);
            }
            result.add(param);
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("total",page.getTotal());
        map.put("current",page.getCurrent());
        map.put("size",page.getSize());
        map.put("data",result);

        // 清空未读的关注消息数
        notificationUnreadService.clearFocusUnreadCount();

        return map;
    }

    private UserSubscribe getUserSubscribe(String userId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        QueryWrapper<UserSubscribe> wrapper = new QueryWrapper<>();
        wrapper.eq("subscribed_id",user.getUuid())
                .eq("be_subscribed_id",userId);
        return userSubscribeMapper.selectOne(wrapper);
    }

    private void checkCanSubscribe(String userId) {
        Asserts.failIsTrue(jwtTokenUtil.getCurrentUserFromHeader().getUuid().equals(userId),"不能对自己操作");
    }
}




