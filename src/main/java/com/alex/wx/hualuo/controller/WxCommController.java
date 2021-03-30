package com.alex.wx.hualuo.controller;

import com.alex.wx.hualuo.model.result.Result;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/23 10:18
 */
@RestController
@RequestMapping("/wx")
public class WxCommController {

    @Autowired
    private WxMpService wxService;

    @GetMapping("/getaddr")
    public Result<String> getAddr() throws WxErrorException {
        String callbackIP = wxService.get("https://api.weixin.qq.com/cgi-bin/get_api_domain_ip",
                "access_token=" + wxService.getAccessToken());
        return Result.succeed(callbackIP);
    }

}


