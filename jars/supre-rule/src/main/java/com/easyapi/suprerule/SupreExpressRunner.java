package com.easyapi.suprerule;

import com.ql.util.express.ExpressRunner;
import com.easyapi.suprerule.util.DateFormatUtil;
import com.easyapi.suprerule.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Guo YuLong
 * @description: TODO
 * @date 2022/2/28 4:53 下午
 */
@Component
public class SupreExpressRunner {

    private static final ExpressRunner runner = new ExpressRunner();

    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private DateFormatUtil dateFormatUtil;

    public static ExpressRunner getRunner() {
        return runner;
    }

    @PostConstruct
    public void init() {
        addMacro();
        // 添加操作符和关键字的别名
        addOperatorWithAlias();
        // 绑定对象的method
        addFunctionOfServiceMethod();
        // 绑定java类的method
        addFunctionOfClassMethod();
    }


    private void addMacro() {
        try {
            runner.addMacro("年月日时分秒", "\"yyyy-MM-dd HH:mm:ss\"");
            runner.addMacro("年月日T时分秒", "\"yyyy-MM-dd'T'HH:mm:ss\"");
            runner.addMacro("年月日时分秒毫秒", "\"yyyy-MM-dd HH:mm:ss.SSS\"");
            runner.addMacro("年月日T时分秒毫秒", "\"yyyy-MM-dd'T'HH:mm:ss.SSS\"");
            runner.addMacro("年月日时分秒中文", "\"yyyy年MM月dd日HH点mm分ss秒\"");
            runner.addMacro("年月日", "\"yyyy-MM-dd\"");
            runner.addMacro("年月日紧凑", "\"yyyyMMdd\"");
            runner.addMacro("年月日时分秒紧凑", "\"yyyyMMddHHmmss\"");
            runner.addMacro("年月日时分秒毫秒紧凑", "\"yyyyMMddHHmmssSSS\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOperatorWithAlias() {
        try {
            runner.addOperatorWithAlias("如果", "if", null);
            runner.addOperatorWithAlias("则", "then", null);
            runner.addOperatorWithAlias("否则", "else", null);
            runner.addOperatorWithAlias("遍历", "for", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFunctionOfServiceMethod() {
        try {
            runner.addFunctionOfServiceMethod("GET请求", requestUtil, "get",
                    new Class[]{String.class, Map.class, Map.class}, null);
            runner.addFunctionOfServiceMethod("POST请求", requestUtil, "post",
                    new Class[]{String.class, Map.class, Object.class, Map.class}, null);
            runner.addFunctionOfServiceMethod("PUT请求", requestUtil, "put",
                    new Class[]{String.class, Map.class, Object.class, Map.class}, null);
            runner.addFunctionOfServiceMethod("DELETE请求", requestUtil, "delete",
                    new Class[]{String.class, Map.class, Object.class, Map.class}, null);
            runner.addFunctionOfServiceMethod("时间戳转日期", dateFormatUtil, "timestampToDatetime",
                    new Class[]{Long.class, String.class}, null);
            runner.addFunctionOfServiceMethod("日期转换", dateFormatUtil, "datetimeToDatetime",
                    new Class[]{String.class, String.class, String.class}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFunctionOfClassMethod() {
        try {
            runner.addFunctionOfClassMethod("取绝对值", Math.class.getName(), "abs", new String[]{"double"}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
