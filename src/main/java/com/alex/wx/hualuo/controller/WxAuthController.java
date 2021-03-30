package com.alex.wx.hualuo.controller;

import com.alex.wx.hualuo.model.result.Result;
//import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPairGenerator;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/18 17:53
 */
@RestController
@RequestMapping("/wx/auth")
@CrossOrigin(origins = "*")
public class WxAuthController {

    private static final Logger log = LoggerFactory.getLogger(WxAuthController.class);


    @Autowired
    private WxMpService wxService;

    @GetMapping("/getOpenId")
    public Result<WxOAuth2UserInfo> getOpenId(@RequestParam(value = "code" ,required = false) String code,
                                              @RequestParam(value = "state", required = false) String state)
                                              throws WxErrorException {
        WxOAuth2Service oAuth2Service = wxService.getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(code);
        WxOAuth2UserInfo userInfo = oAuth2Service.getUserInfo(accessToken, null);
        log.info("\n==========【获取用户信息成功：nickname={}，openid={}】==========",
                userInfo.getNickname(), userInfo.getOpenid());
        return Result.succeed(userInfo);
    }

    @GetMapping("/getOpenIdMock")
    public Result<WxOAuth2UserInfo> getOpenIdMock(@RequestParam(value = "code") String code,
                                                  @RequestParam(value = "state", required = false) String state) {
        WxOAuth2UserInfo userInfo = new WxOAuth2UserInfo();
        userInfo.setOpenid("mock-wsd001");
        userInfo.setNickname("东");
        userInfo.setCity("武汉");
        userInfo.setProvince("湖北");
        userInfo.setSex(1);
        userInfo.setHeadImgUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/QR9lM9m1dZhia4RaoMYiaXpZe7L4qp3Siah2tHBNf4anzSdWx8U1bwFMPqwcOqCbhb7YEAJwzGicAjOAwqdhMgjibKQ/132");
        userInfo.setCountry("中国");
        log.info("\n==========【获取Mock用户信息成功：nickname={}，openid={}】==========",
                userInfo.getNickname(), userInfo.getOpenid());
        return Result.succeed(userInfo);
    }
}
