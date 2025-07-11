package com.easyapi.suprerule.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * @author Guo YuLong
 * @description: TODO
 * @date 2022/2/28 5:40 下午
 */
@Component
public class RequestUtil {

    @Autowired(required = false)
    private RestTemplate restTemplate;

    public JSONObject get(String url, Map<String, String> header, Map<String, Object> params) {
        return request(url, HttpMethod.GET, header, null, params);
    }

    public JSONObject post(String url, Map<String, String> header, Object body, Map<String, Object> params) {
        return request(url, HttpMethod.POST, header, body, params);
    }

    public JSONObject put(String url, Map<String, String> header, Object body, Map<String, Object> params) {
        return request(url, HttpMethod.PUT, header, body, params);
    }

    public JSONObject delete(String url, Map<String, String> header, Object body, Map<String, Object> params) {
        return request(url, HttpMethod.DELETE, header, body, params);
    }

    private JSONObject request(String url, HttpMethod httpMethod, Map<String, String> headers,
                               Object body, Map<String, Object> params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<JSONObject> result;
        if (CollectionUtils.isEmpty(params)) {
            result = restTemplate.exchange(url, httpMethod, httpEntity, JSONObject.class);
        } else {
            // exchange此重载方法会校验uriVariables，不允许为空
            result = restTemplate.exchange(url, httpMethod, httpEntity, JSONObject.class, params);
        }
        if (result.getStatusCode() != HttpStatus.OK) {
            // TODO 异常处理
            throw new RuntimeException("code: " + result.getStatusCode() + ", message: " + result.getBody());
        }
        return result.getBody();
    }

    /**
     * 如果上层应用已声明 restTemplate Bean，则直接注入，否则重新构建
     */
    @PostConstruct
    private void initRestTemplate() {
        if (Objects.nonNull(restTemplate)) {
            return;
        }
        // TODO 此处后续可考虑添加其他配置项
        restTemplate = new RestTemplate();
    }

}
