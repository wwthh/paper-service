package com.wwt.paperservice.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Error
 */
public class Error {

    public static JSONObject errorResponse(int code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        JSONObject content = new JSONObject();
        content.put("error_code", code);
        jsonObject.put("content", content);
        return jsonObject;
    }
}