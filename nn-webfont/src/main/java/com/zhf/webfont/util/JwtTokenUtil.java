package com.zhf.webfont.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zhf.common.util.RequestUtil;
import com.zhf.common.util.ThreadLocalUtil;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * @Author 10276
 * @Date 2023/1/3 12:38
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Resource
    private UserService userService;
    @Resource
    private ThreadLocalUtil threadLocalUtil;


    public String getTokenFromHeader(){
        HttpServletRequest currentRequest = RequestUtil.getCurrentRequest();
        String authHeader = currentRequest.getHeader(tokenHeader);
        if (StrUtil.isEmpty(authHeader) || authHeader.length() <= tokenHead.length()){
            return null;
        }
        return authHeader.substring(tokenHead.length());
    }

    public String getAccountFromHeader(){
        HttpServletRequest currentRequest = RequestUtil.getCurrentRequest();
        String authHeader = currentRequest.getHeader(tokenHeader);
        if (StrUtil.isEmpty(authHeader) || authHeader.length() <= tokenHead.length()){
            return null;
        }
        String authToken = authHeader.substring(tokenHead.length());
        return getAccountFromToken(authToken);
    }

    public User getCurrentUserFromHeader(){
        if (threadLocalUtil.get(ThreadLocalUtil.CURRENT_USER) == null){
            String accountFromHeader = getAccountFromHeader();
            User user = userService.getUserFromEmailAddress(accountFromHeader);
            threadLocalUtil.set(ThreadLocalUtil.CURRENT_USER,user);
        }
        return (User) threadLocalUtil.get(ThreadLocalUtil.CURRENT_USER);
    }

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims  getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getAccountFromToken(String token) {
        String account;
        try {
            Claims claims = getClaimsFromToken(token);
            account = claims.getSubject();
        } catch (Exception e) {
            account = null;
        }
        return account;
    }

    /**
     * 判断请求中的token是否已经失效
     */
    public boolean isTokenExpired() {
        String token = getTokenFromHeader();
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 判断token是否已经失效
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(String account) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, account);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    public String refreshHeadToken(String oldToken) {
        if(StrUtil.isEmpty(oldToken)){
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if(StrUtil.isEmpty(token)){
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsFromToken(token);
        if(claims==null){
            return null;
        }
        //如果token已经过期，不支持刷新
        if(isTokenExpired(token)){
            return null;
        }
        //如果token在30分钟之内刚刷新过，返回原token
        if(tokenRefreshJustBefore(token,30*60)){
            return token;
        }else{
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        return refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time));
    }
}
