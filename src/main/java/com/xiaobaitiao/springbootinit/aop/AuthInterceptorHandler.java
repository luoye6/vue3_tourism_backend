package com.xiaobaitiao.springbootinit.aop;

import com.alibaba.fastjson.JSONObject;
import com.xiaobaitiao.springbootinit.common.JwtKit;
import com.xiaobaitiao.springbootinit.common.JwtProperties;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 程序员小白条
 */
public class AuthInterceptorHandler implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtKit jwtKit;

    /**
     * 前置拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // 放行 /api/sse 路径 SSE 的路径
        if (request.getRequestURI().startsWith("/api/sse/")) {
            return true; // 直接放行
        }

        HandlerMethod handlerMethod=(HandlerMethod)handler;
        //判断如果请求的类是swagger的控制器，直接通行。
        if(("springfox.documentation.swagger.web.ApiResourceController").equals(handlerMethod.getBean().getClass().getName())){
            return  true;
        }

        if ((CommonConstant.OPTIONS).equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        // 获取到JWT的Token
        String jwtToken = request.getHeader(jwtProperties.getTokenHeader());
        // 无敌令牌命中，直接放行，仅测试使用
        if(CommonConstant.INVINCIBLE_TOKEN.equals(jwtToken)){
            return true;
        }
        // 截取中间payload部分 +1是Bearer + 空格(1)
        String payloadToken = null;
        // 创建json对象
        JSONObject jsonObject = new JSONObject();

        if (jwtToken != null) {
            payloadToken = jwtToken.substring(jwtProperties.getTokenHead().length() + 1);
        }
        if (payloadToken != null && (!StringUtils.isBlank(payloadToken))) {

            // 解析Token，获取Claims = Map
            Claims claims = null;
            try {
                claims = jwtKit.parseJwtToken(payloadToken);
            } catch (Exception e) {
                //token过期会捕捉到异常
                jsonObject.put("status", 401);
                jsonObject.put("msg", "登录过期,请重新登录");
                String json1 = jsonObject.toJSONString();
                renderJson(response, json1);
            }
            return claims != null;
            // 获取payload中的报文，
        }
        // 如果token不存在
        jsonObject.put("status", 401);
        jsonObject.put("msg", "登录非法，无有效全局 Token");
        String json2 = jsonObject.toJSONString();
        renderJson(response, json2);

        return false;
    }

    private void renderJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
