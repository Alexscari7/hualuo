package com.alex.wx.hualuo.handler;

import com.alex.wx.hualuo.model.result.Result;
//import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author wusd
 * @description 空
 * @create 2021/03/17 15:05
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WxErrorException.class)
    @ResponseBody
    @CrossOrigin
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleWxErrorException(WxErrorException ex) {
        log.error("\n【微信服务器调用异常：{}】", ex.getError().toString());
        return Result.failed(String.valueOf(ex.getError().getErrorCode()), ex.getError().getErrorMsg(), null);
    }

}
