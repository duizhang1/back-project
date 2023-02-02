package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.enumType.SubscribeStateEnum;
import com.zhf.common.exception.Asserts;
import com.zhf.webfont.po.User;
import com.zhf.webfont.po.UserSubscribe;
import com.zhf.webfont.service.UserSubscribeService;
import com.zhf.webfont.mapper.UserSubscribeMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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




