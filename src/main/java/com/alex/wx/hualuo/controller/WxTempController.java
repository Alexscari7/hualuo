package com.alex.wx.hualuo.controller;

import com.alex.wx.hualuo.model.result.Result;
import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustry;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustryEnum;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/17 14:20
 */
@RestController
@RequestMapping("/wx")
public class WxTempController {

    @Autowired
    private WxMpService wxService;

    // 设置行业类型
    @GetMapping("/industry/set")
    public Result<String> setIndustry(@RequestParam(value = "industry_id1") int industry1,
                                      @RequestParam(value = "industry_id2") int industry2) throws WxErrorException {
        // TODO: 2021/3/17 是否能改成单例
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        WxMpTemplateIndustryEnum industryEnum1 = WxMpTemplateIndustryEnum.findByCode(industry1);
        WxMpTemplateIndustryEnum industryEnum2 = WxMpTemplateIndustryEnum.findByCode(industry2);
        WxMpTemplateIndustry industry = new WxMpTemplateIndustry(industryEnum1, industryEnum2);
        if (templateService.setIndustry(industry)) {
            return Result.succeed("行业类型设置成功");
        } else {
            return Result.failed("行业类型设置失败");
        }
    }

    // 获取公众号的行业类型
    @GetMapping("/industry/get")
    public Result<String> getIndustry() throws WxErrorException {
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        WxMpTemplateIndustry industry = templateService.getIndustry();;
        return Result.succeed(industry.toJson());
    }

    // 从公众号所属行业模板库中选择模板添加，测试号无法使用
    @GetMapping("/template/add")
    public Result<String> addTemplate(@RequestParam("template_id_short") String tempShortId) throws WxErrorException {
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        String tempId = templateService.addTemplate(tempShortId);
        return Result.succeed(tempId);
    }

    // 删除公众号下的模板
    @GetMapping("/template/delete")
    public Result<String> delTemplate(@RequestParam("template_id") String tempId) throws WxErrorException {
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        if (templateService.delPrivateTemplate(tempId)) {
            return Result.succeed("删除成功");
        } else {
            return Result.failed("删除失败");
        }
    }

    // 获取公众号下所有模板的详细信息
    @GetMapping("template/getAll")
    public Result<List<WxMpTemplate>> getAllTemplate() throws WxErrorException {
        boolean notEmpty = StringUtils.isNotEmpty("1212");
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        List<WxMpTemplate> allPrivateTemplate = templateService.getAllPrivateTemplate();
        return Result.succeed(allPrivateTemplate);
    }

    // 发送模板消息
    @GetMapping("template/sendMsg")
    public Result<String> sendMessage(@RequestParam("open_id") String openId,
                                      @RequestParam("template_id") String templateId) throws WxErrorException {
        WxMpTemplateMsgService templateService = wxService.getTemplateMsgService();
        WxMpTemplateData first = new WxMpTemplateData("first", "本次购买获得一次抽奖机会!\n");
        WxMpTemplateData keyWord1 = new WxMpTemplateData("keyword1", "MacBook Pro M1芯片 13.3 英寸");
        WxMpTemplateData keyWord2 = new WxMpTemplateData("keyword2", "14208823981323");
        WxMpTemplateData keyWord3 = new WxMpTemplateData("keyword3", "9999元", "#F70000");
        WxMpTemplateData keyWord4 = new WxMpTemplateData("keyword4",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        WxMpTemplateData remark = new WxMpTemplateData("remark", "欢迎再次购买！");
        ArrayList<WxMpTemplateData> wxMpTemplateData = Lists.newArrayList(first, keyWord1, keyWord2, keyWord3, keyWord4, remark);
        String url = "https://item.m.jd.com/product/100009464821.html?wxa_abtest=a&utm_source=io" +
                "sapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=CopyURL&ad_od=share&utm" +
                "_user=plusmember&gx=RnEwx25daTXcztTBJzmi0jifwGJXugX3UO4HNVQ";
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage(openId, templateId,
                url ,null, wxMpTemplateData);
        String msgId = templateService.sendTemplateMsg(wxMpTemplateMessage);
        return Result.succeed(msgId);
    }

}
