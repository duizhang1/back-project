package com.zhf.webfont.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.ImRecordItemParam;
import com.zhf.webfont.mapper.ImRecordMapper;
import com.zhf.webfont.po.ImRecord;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ImUnreadRecordService;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.util.BeanUtil;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerEndpoint创建的实例是原型模式
 * @Author 10276
 * @Date 2023/2/17 12:38
 */
@ServerEndpoint("/chatroom")
@Component
public class ChatRoomServer {

    private JwtTokenUtil jwtTokenUtil = BeanUtil.getBean(JwtTokenUtil.class);
    private UserService userService = BeanUtil.getBean(UserService.class);
    private ImRecordMapper imRecordMapper = BeanUtil.getBean(ImRecordMapper.class);
    private ImUnreadRecordService imUnreadRecordService = BeanUtil.getBean(ImUnreadRecordService.class);

    private static Map<String, Map<ChatRoomServer,Object>> webSocketSet = new ConcurrentHashMap<>();

    private Session session;
    private User user;

    /**
     * 需要传入的参数
     * token,用户的token
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        if (!parameterMap.containsKey("token")){
            broadcastToCurUser(CommonResult.unauthorized(null));
            return;
        }
        // 从路径参数中获得token，并转换成提取User信息
        String authHeader = parameterMap.get("token").get(0);
        String token = jwtTokenUtil.getTokenFromAuthHeader(authHeader);
        String account = jwtTokenUtil.getAccountFromToken(token);
        User user = userService.getUserFromEmailAddress(account);
        if (user == null){
            broadcastToCurUser(CommonResult.unauthorized(null));
            return;
        }
        this.user = user;

        // 将webSocket以及session存入
        webSocketSet.putIfAbsent(user.getUuid(),new ConcurrentHashMap<>(5));
        Map<ChatRoomServer, Object> userMap = webSocketSet.get(user.getUuid());
        userMap.put(this,null);

        broadcastToCurUser(CommonResult.successWithMsg("success"));
    }

    @OnClose
    public void onClose(){
        // 移除对象
        Map<ChatRoomServer, Object> serverMap = webSocketSet.get(user.getUuid());
        serverMap.remove(this);
    }

    @OnMessage
    public void onMessage(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        // 自定义日期的格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            Map<String,Object> map = objectMapper.readValue(message, Map.class);
            String toUserId = (String) map.get("toUserId");
            String content = (String) map.get("content");
            String uuid = (String) map.get("uuid");
            // 首先将消息存入数据库
            ImRecord imRecord = new ImRecord();
            imRecord.setUuid(uuid);
            imRecord.setContent(content);
            imRecord.setToUserId(toUserId);
            imRecord.setUserId(user.getUuid());
            imRecord.setCreateTime(new Date());
            imRecord.setUpdateTime(new Date());
            int insert = imRecordMapper.insert(imRecord);

            // 组装返回参数
            ImRecordItemParam imRecordItemParam = new ImRecordItemParam();
            imRecordItemParam.setImRecord(imRecord);
            imRecordItemParam.setAvatar(user.getAvatar());
            imRecordItemParam.setUsername(user.getUsername());
            imRecordItemParam.setCount(1);
            imRecordItemParam.setToUserId(toUserId);
            if (insert < 1) {
                broadcastFailToSender(imRecordItemParam);
                return;
            }else{
                imRecordItemParam.setOwn(true);
                broadcastSuccessToSender(imRecordItemParam);
            }
            // 判断是否在websocket集合中,在集合中就直接发送消息
            if (webSocketSet.containsKey(toUserId)){
                imRecordItemParam.setOwn(false);
                Map<ChatRoomServer, Object> serverMap = webSocketSet.get(toUserId);
                for (ChatRoomServer chatRoomServer : serverMap.keySet()) {
                    String returnMsg = objectMapper.writeValueAsString(CommonResult.success(imRecordItemParam,"addmsg"));
                    chatRoomServer.session.getAsyncRemote().sendText(returnMsg);
                }
            }
            // 更新消息通知，如果在websocket中就会直接清空消息通知
            imUnreadRecordService.incrUnreadCount(user.getUuid(),toUserId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息给当前用户
     * @param object
     */
    private void broadcastToCurUser(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.session.getAsyncRemote().sendText(result);
    }

    /**
     * 将失败的消息返回给发送者
     * @param imRecordItemParam
     * @throws JsonProcessingException
     */
    private void broadcastFailToSender(ImRecordItemParam imRecordItemParam) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(CommonResult.failed("fail", imRecordItemParam));
        this.session.getAsyncRemote().sendText(result);
    }

    /**
     * 将发送成功的消息返回给发送者
     * @param imRecordItemParam
     */
    private void broadcastSuccessToSender(ImRecordItemParam imRecordItemParam) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(CommonResult.success(imRecordItemParam,"addmsg"));
        this.session.getAsyncRemote().sendText(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatRoomServer that = (ChatRoomServer) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
