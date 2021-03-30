package com.alex.wx.hualuo.handler.route;

import com.alex.wx.hualuo.utils.JsonUtils;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LogHandler extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context,
                                    WxMpService wxService,
                                    WxSessionManager sessionManager) {
        logger.info("\n==========【请求解析，内容为：{}】==========", JsonUtils.toJson(wxMessage));
        return null;
    }

}
