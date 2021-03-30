package com.alex.wx.hualuo.controller;

//import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/16 15:20
 */
@SpringBootTest
class WxBaseControllerTest {

    private static final Logger log = LoggerFactory.getLogger(WxBaseControllerTest.class);

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void test1() {
        log.info("12341234124==========");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    }
}