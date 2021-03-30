package com.alex.wx.hualuo.config;

import com.alex.wx.hualuo.handler.route.KfSessionHandler;
import com.alex.wx.hualuo.handler.route.LocationHandler;
import com.alex.wx.hualuo.handler.route.LogHandler;
import com.alex.wx.hualuo.handler.route.MenuHandler;
import com.alex.wx.hualuo.handler.route.MsgHandler;
import com.alex.wx.hualuo.handler.route.NullHandler;
import com.alex.wx.hualuo.handler.route.ScanHandler;
import com.alex.wx.hualuo.handler.route.StoreCheckNotifyHandler;
import com.alex.wx.hualuo.handler.route.SubscribeHandler;
import com.alex.wx.hualuo.handler.route.UnsubscribeHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import static me.chanjar.weixin.common.api.WxConsts.EventType.*;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.KF_CLOSE_SESSION;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.KF_CREATE_SESSION;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.KF_SWITCH_SESSION;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/16 18:25
 */
@Configuration
@EnableConfigurationProperties({WxMpProperties.class})
public class WxMpConfiguration {

    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LogHandler logHandler;
    @Autowired
    private NullHandler nullHandler;
    @Autowired
    private KfSessionHandler kfSessionHandler;
    @Autowired
    private StoreCheckNotifyHandler storeCheckNotifyHandler;
    @Autowired
    private LocationHandler locationHandler;
    @Autowired
    private MenuHandler menuHandler;
    @Autowired
    private MsgHandler msgHandler;
    @Autowired
    private UnsubscribeHandler unsubscribeHandler;
    @Autowired
    private SubscribeHandler subscribeHandler;
    @Autowired
    private ScanHandler scanHandler;

    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl configStorage;
        if (wxMpProperties.isRedisEnabled()) {
            StringRedisTemplate redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
            configStorage = new WxMpRedisConfigImpl(new RedisTemplateWxRedisOps(redisTemplate),
                    wxMpProperties.getAppId());
        } else {
            configStorage = new WxMpDefaultConfigImpl();
        }
        configStorage.setAppId(wxMpProperties.getAppId());
        configStorage.setSecret(wxMpProperties.getSecret());
        configStorage.setToken(wxMpProperties.getToken());
        configStorage.setAesKey(wxMpProperties.getAesKey());
        //configStorage.setHostConfig(new WxMpHostConfig("http://127.0.0.1:443", null, null));
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(configStorage);
        return wxService;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
                .handler(kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(CLICK).handler(menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(VIEW).handler(nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.LOCATION).handler(locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION).handler(locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(SCAN).handler(scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();

        return newRouter;
    }



}
