package com.sshy.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class JsonResult {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer status;

    private String message;

    public Map<String, Object> extend = new HashMap<String, Object>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static JsonResult success() {
        JsonResult voResult = new JsonResult();
        voResult.setStatus(1);
        voResult.setMessage("request success");
        return voResult;
    }

    public static JsonResult fail() {
        JsonResult voResult = new JsonResult();
        voResult.setStatus(0);
        voResult.setMessage("request fail");
        return voResult;
    }

    public JsonResult add(String key, Object value) {
        this.extend.put(key, value);
        return this;
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> exceptionResult(Exception exception) {
        exception.printStackTrace();
        logger.error(exception.getMessage());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 500);
        result.put("message", "system err , please wait");
        return result;
    }

}
