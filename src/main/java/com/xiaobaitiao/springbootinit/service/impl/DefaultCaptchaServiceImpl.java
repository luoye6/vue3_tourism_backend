//package com.xiaobaitiao.springbootinit.service.impl;
//
//import com.anji.captcha.model.common.RepCodeEnum;
//import com.anji.captcha.model.common.ResponseModel;
//import com.anji.captcha.model.vo.CaptchaVO;
//import com.anji.captcha.service.CaptchaService;
//import com.anji.captcha.service.impl.AbstractCaptchaService;
//import com.anji.captcha.service.impl.CaptchaServiceFactory;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Properties;
//
//public class DefaultCaptchaServiceImpl extends AbstractCaptchaService {
//    DefaultCaptchaServiceImpl() {
//        //document why this constructor is empty
//    }
//
//    //这个需要实现，如果返回 redis 那就是使用 redis 那套
//    public String captchaType() {return "default";}
//    @Override
//    public void init(Properties config) {
//        for (String s : CaptchaServiceFactory.instances.keySet()) {
//            if (!this.captchaType().equals(s)) {
//                this.getService(s).init(config);
//            }
//        }
//    }
//    @Override
//    public void destroy(Properties config) {
//        for (String s : CaptchaServiceFactory.instances.keySet()) {
//            if (!this.captchaType().equals(s)) {
//                this.getService(s).destroy(config);
//            }
//        }
//    }
//    private CaptchaService getService(String captchaType) {return CaptchaServiceFactory.instances.get(captchaType);}
//    @Override
//    public ResponseModel get(CaptchaVO captchaVO) {
//        if (captchaVO == null) {
//            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
//        } else {
//            return StringUtils.isEmpty(captchaVO.getCaptchaType()) ? RepCodeEnum.NULL_ERROR.parseError("类型") : this.getService(captchaVO.getCaptchaType()).get(captchaVO);
//        }
//    }
//
//    @Override
//    public ResponseModel check(CaptchaVO captchaVO) {
//        if (captchaVO == null) {
//            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
//        } else if (StringUtils.isEmpty(captchaVO.getCaptchaType())) {
//            return RepCodeEnum.NULL_ERROR.parseError("类型");
//        } else {
//            return StringUtils.isEmpty(captchaVO.getToken()) ? RepCodeEnum.NULL_ERROR.parseError("token") : this.getService(captchaVO.getCaptchaType()).check(captchaVO);
//        }
//    }
//    @Override
//    public ResponseModel verification(CaptchaVO captchaVO) {
//        if (captchaVO == null) {
//            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
//        } else if (StringUtils.isEmpty(captchaVO.getCaptchaVerification())) {
//            return RepCodeEnum.NULL_ERROR.parseError("二次校验参数");
//        } else {
//            try {
//                String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
//                if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
//                    return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
//                }
//
//                CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
//            } catch (Exception var3) {
//                this.logger.error("验证码坐标解析失败", var3);
//                return ResponseModel.errorMsg(var3.getMessage());
//            }
//
//            return ResponseModel.success();
//        }
//    }
//}