package com.sshy.api.utils.ARManaUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Auth {

    private static final String KEY_DATE = "date";
    private static final String KEY_APP_KEY = "appKey";
    private static final String KEY_SIGNATURE = "signature";

    private static String generateSignature(JSONObject jso, String appSecret) {
        String paramStr = jso.keySet().stream()
                .sorted()
                .map(key -> key + jso.getString(key))
                .collect(Collectors.joining());
        return DigestUtils.sha1Hex(paramStr + appSecret);
    }

    public static JSONObject signParam(JSONObject param, String appKey, String appSecret) {
        param.put(KEY_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now()));
        param.put(KEY_APP_KEY, appKey);
        param.put(KEY_SIGNATURE, generateSignature(param, appSecret));
        return param;
    }
}
