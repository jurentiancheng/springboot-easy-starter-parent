package com.easyapi.suprerule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.easyapi.suprerule.config.RuleConfig;
import com.easyapi.suprerule.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RuleConfig.class})
class SupreRuleApplicationTests {

    private static ExpressRunner runner = SupreExpressRunner.getRunner();;

    @Autowired
    private RequestUtil requestUtil;

    @Test
    void getWithParamsTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("city", 310100);
        params.put("key", "5c581fa077662580180cf3e646194bc6");
        JSONObject result = requestUtil.get("https://restapi.amap.com/v3/weather/weatherInfo?city={city}&key={key}", null, params);
        log.info("result  ==> {}", result);
    }


    @Test
    void getWithParamsUserAliasTest() {
        String express = "params = new HashMap(); " +
                "params.put(\"city\", \"310100\"); " +
                "params.put(\"key\", \"5c581fa077662580180cf3e646194bc6\"); " +
                "result = GET请求(\"https://restapi.amap.com/v3/weather/weatherInfo?city={city}&key={key}\", null, params); " +
                "return result;";
        DefaultContext<String, Object> context = new DefaultContext<>();
        List<String> errorList = new ArrayList<>();
        try {
            Object result = runner.execute(express, context, errorList, true, true);
            log.info("result ==> {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("error ==> {}", JSON.toJSONString(errorList));
    }

    @Test
    void contextLoads() {
        ExpressRunner runner = SupreExpressRunner.getRunner();
        String express = "data = new HashMap(); " +
                "data.put(\"username\", \"admin\"); " +
                "data.put(\"password\", \"admin123\"); " +
                "result = POST请求(\"http://100.100.142.194:3000/login\", null, data, null); " +
                "return result;";
        DefaultContext<String, Object> context = new DefaultContext<>();
        List<String> errorList = new ArrayList<>();
        try {
            Object result = runner.execute(express, context, errorList, true, true);
            log.info("result ==> {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("error ==> {}", JSON.toJSONString(errorList));
    }

    @Test
    void test1() {
        Map<String, Object> data = new HashMap<>();
        ExpressRunner runner = SupreExpressRunner.getRunner();
        String express = "data.put(\"username\", \"admin\"); " +
                "data.put(\"password\", \"admin123\"); ";
        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("data", data);
        List<String> errorList = new ArrayList<>();
        try {
            Object result = runner.execute(express, context, errorList, true, true);
            log.info("result ==> {}", result);
            log.info("data ==> {}", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("error ==> {}", JSON.toJSONString(errorList));
    }


    @Test
    void DatetimeToDatetimeTest() throws Exception {
        String express = "toDateString = 日期转换(\"2022-06-15 23:23:30\", 年月日时分秒, 年月日时分秒中文);";
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, true);
        Assertions.assertEquals("2022年06月15日23点23分30秒", result.toString());
    }

    @Test
    void timestampToDatetimeTest() throws Exception {
        String express = "toDateString = 时间戳转日期(1655364571, 年月日时分秒);";
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, true);
        Assertions.assertEquals("2022-06-16 15:29:31", result.toString());
    }

    @Test
    void timestampToDatetimeCnTest() throws Exception {
        String express = "toDateString = 时间戳转日期(1655364571, 年月日时分秒中文);";
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, true);
        Assertions.assertEquals("2022年06月16日15点29分31秒", result.toString());
    }

    @Test
    void timestampToDatetimeMillisTest() throws Exception {
        String express = "toDateString = 时间戳转日期(1655364571235, 年月日时分秒毫秒);";
        IExpressContext<String, Object> context = new DefaultContext<>();
        Object result = runner.execute(express, context, null, true, true);
        Assertions.assertEquals("2022-06-16 15:29:31.235", result.toString());
    }

}
