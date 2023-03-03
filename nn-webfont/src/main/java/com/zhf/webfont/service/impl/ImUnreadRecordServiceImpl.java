package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.webfont.po.ImUnreadRecord;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ImUnreadRecordService;
import com.zhf.webfont.mapper.ImUnreadRecordMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 10276
 * @description 针对表【wf_im_unread_record】的数据库操作Service实现
 * @createDate 2023-02-19 14:24:34
 */
@Service
public class ImUnreadRecordServiceImpl extends ServiceImpl<ImUnreadRecordMapper, ImUnreadRecord>
        implements ImUnreadRecordService {

    @Resource
    private ImUnreadRecordMapper imUnreadRecordMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void incrUnreadCount(String userId, String toUserId) {
        ImUnreadRecord imUnreadRecord = getImUnreadRecordById(userId, toUserId);
        if (imUnreadRecord == null) {
            ImUnreadRecord unreadRecord = new ImUnreadRecord();
            unreadRecord.setUserId(userId);
            unreadRecord.setToUserId(toUserId);
            unreadRecord.setCount(1);
            unreadRecord.setCreateTime(new Date());
            unreadRecord.setUpdateTime(new Date());
            imUnreadRecordMapper.insert(unreadRecord);
        } else {
            imUnreadRecord.setCount(imUnreadRecord.getCount()+1);
            imUnreadRecordMapper.updateById(imUnreadRecord);
        }
    }

    @Override
    public Long getImUnreadCount() {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();
        QueryWrapper<ImUnreadRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("to_user_id",curUser.getUuid());
        List<ImUnreadRecord> imUnreadRecords = imUnreadRecordMapper.selectList(wrapper);
        Long count = 0L;
        for (ImUnreadRecord imUnreadRecord : imUnreadRecords) {
            count += imUnreadRecord.getCount();
        }
        return count;
    }

    private ImUnreadRecord getImUnreadRecordById(String userId, String toUserId) {
        QueryWrapper<ImUnreadRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("to_user_id", toUserId);
        return imUnreadRecordMapper.selectOne(wrapper);
    }
}




