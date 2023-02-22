package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.webfont.bo.ImRecordItemParam;
import com.zhf.webfont.mapper.ImUnreadRecordMapper;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.ImRecord;
import com.zhf.webfont.po.ImUnreadRecord;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ImRecordService;
import com.zhf.webfont.mapper.ImRecordMapper;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 10276
 * @description 针对表【wf_im_record】的数据库操作Service实现
 * @createDate 2023-02-17 20:27:05
 */
@Service
public class ImRecordServiceImpl extends ServiceImpl<ImRecordMapper, ImRecord>
        implements ImRecordService {

    @Resource
    private ImRecordMapper imRecordMapper;
    @Resource
    private ImUnreadRecordMapper imUnreadRecordMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<ImRecordItemParam> getChatList() {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        List<ImUnreadRecord> list = imUnreadRecordMapper.getChatList(user.getUuid());
        List<ImRecordItemParam> result = new ArrayList<>();
        Set<String> userSet = new HashSet<>();
        for (ImUnreadRecord imUnreadRecord : list) {
            String toUserId = null;
            if (imUnreadRecord.getUserId().equals(user.getUuid())) {
                toUserId = imUnreadRecord.getToUserId();
            } else {
                toUserId = imUnreadRecord.getUserId();
            }
            if (userSet.contains(toUserId)) {
                continue;
            }
            userSet.add(toUserId);
            User toUser = userMapper.selectById(toUserId);
            ImRecord imRecord = imRecordMapper.selectNewMessage(user.getUuid(), toUserId);
            ImRecordItemParam imRecordItemParam = new ImRecordItemParam();
            imRecordItemParam.setCount(getUnReadCount(toUserId,user.getUuid()));
            imRecordItemParam.setImRecord(imRecord);
            imRecordItemParam.setAvatar(toUser.getAvatar());
            imRecordItemParam.setUsername(toUser.getUsername());
            imRecordItemParam.setToUserId(toUserId);
            result.add(imRecordItemParam);
        }
        return result;
    }

    private Integer getUnReadCount(String toUserId, String userId) {
        QueryWrapper<ImUnreadRecord> wrapper = new QueryWrapper<>();
        // 需要反向放置参数，因为要查询的是对面是否有向当前登陆用户是否有发送消息
        wrapper.eq("user_id",toUserId)
                .eq("to_user_id",userId);
        ImUnreadRecord imUnreadRecord = imUnreadRecordMapper.selectOne(wrapper);
        return imUnreadRecord == null ? 0 : imUnreadRecord.getCount();
    }

    @Override
    public ImRecordItemParam getOneChat(String toUserId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        User toUser = userMapper.selectById(toUserId);

        // 设置返回的消息
        ImRecord imRecord = imRecordMapper.selectNewMessage(user.getUuid(), toUserId);
        ImRecordItemParam imRecordItemParam = new ImRecordItemParam();
        imRecordItemParam.setImRecord(imRecord == null ? new ImRecord() : imRecord);
        imRecordItemParam.setAvatar(toUser.getAvatar());
        imRecordItemParam.setUsername(toUser.getUsername());
        imRecordItemParam.setToUserId(toUserId);

        return imRecordItemParam;
    }

    @Override
    public List<ImRecordItemParam> getChatContent(String toUserId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        User toUser = userMapper.selectById(toUserId);

        QueryWrapper<ImRecord> wrapper = new QueryWrapper<>();
        wrapper.and(e -> {
            e.eq("user_id", user.getUuid())
                    .eq("to_user_id", toUserId);
        }).or(e -> {
            e.eq("user_id", toUserId)
                    .eq("to_user_id", user.getUuid());
        })
                .orderByAsc("create_time");
        List<ImRecord> imRecords = imRecordMapper.selectList(wrapper);

        List<ImRecordItemParam> result = new ArrayList<>();
        for (ImRecord imRecord : imRecords) {
            ImRecordItemParam itemParam = new ImRecordItemParam();
            itemParam.setImRecord(imRecord);
            if (imRecord.getUserId().equals(user.getUuid())){
                itemParam.setUsername(user.getUsername());
                itemParam.setOwn(true);
                itemParam.setAvatar(user.getAvatar());
                itemParam.setToUserId(toUserId);
            }else{
                itemParam.setUsername(toUser.getUsername());
                itemParam.setOwn(false);
                itemParam.setAvatar(toUser.getAvatar());
                itemParam.setToUserId(user.getUuid());
            }
            result.add(itemParam);
        }

        //未读消息置为0
        QueryWrapper<ImUnreadRecord> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("to_user_id",user.getUuid())
                .eq("user_id",toUser.getUuid());
        ImUnreadRecord imUnreadRecord = imUnreadRecordMapper.selectOne(wrapper1);
        if (imUnreadRecord != null){
            imUnreadRecord.setCount(0);
            imUnreadRecordMapper.updateById(imUnreadRecord);
        }
        return result;
    }
}




