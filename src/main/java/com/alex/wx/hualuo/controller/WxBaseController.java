package com.alex.wx.hualuo.controller;

import com.alex.wx.hualuo.utils.JsonUtils;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/16 14:26
 */
@RestController
@RequestMapping("/wx")
public class WxBaseController {

    private static final Logger log = LoggerFactory.getLogger(WxBaseController.class);

    @Autowired
    private WxMpService wxService;
    @Autowired
    private WxMpMessageRouter messageRouter;

    // 微信服务器认证请求
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String verify(@RequestParam(name = "signature", required = false) String signature,
                         @RequestParam(name = "timestamp", required = false) String timestamp,
                         @RequestParam(name = "nonce", required = false) String nonce,
                         @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n==========【收到认证请求：signature={}，timestamp={}，nonce={}，echostr={}】==========",
                signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            log.info("\n==========【认证成功，返回码:{}】==========", echostr);
            return echostr;
        }
        return "请求非法";
    }

    // 用户信息接收回复请求
    @PostMapping(produces = "application/xml; charset=utf-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam(name = "timestamp", required = false) String timestamp,
                       @RequestParam(name = "nonce", required = false) String nonce,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n==========【接收请求，原始内容：\nbody={}，\nencType={}，\nmsgSignature={}】==========",
                requestBody, msgSignature, encType);
        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    null, null, msgSignature);
            log.info("\n========== 消息解密后内容为：{} ==========", inMessage.toString());
            WxMpXmlOutMessage outMessage = route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        log.info("\n=========【组装回复信息：{}】 ==========", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }
}
