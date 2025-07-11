package com.easyapi.base.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.*;
import com.easyapi.base.CircleDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date的工具类，其内容只会考虑在项目上常用的方法。并不会去实现功能特别强大且范围特别广的内容。
 * 本方法只是在hu tool工具包中的时间处理类补 加基于项目上常用并且hu tool工具包未直接提供的功能。
 * 常用内容：</br>
 * <ui>
 * <li>将字符串按时间格式解析成Date类型{@link DateUtil#parse};format的格式可以使用{@link DateConstantHelper}</li>
 * <blockquote>
 * <pre>
 *  Date parse = DateUtil.parse(format, DateConstantHelper.DATE_TIME);
 *  或者可以使用{@link DateConstantHelper#dateTimeReplace}来替换需要格式
 *  //format = "2022/01/01 00/00/01"
 *  //DateConstantHelper.dateTimeReplace("/", "/")处理后就是yyyy/MM/dd HH/mm/ss
 *  Date parse1 = DateUtil.parse(format, DateConstantHelper.dateTimeReplace("/", "/"));
 *     </pre>
 * </blockquote>
 *
 * <li>将Date类型按格式化成字符串类型--{@link DateUtil#format}</li>
 * <li>时间加减操作;需要注意在使用{@link DateUtil#offset(Date, DateField, int)}方法.如果Date是null；默认使用是当前时间(需要特别注意)
 * <pre>
 *          * 最基本方法可以直接使用{@link DateUtil#offset(Date, DateField, int)}，可以参考对应版本的官方指导使用
 *          * 也可以直接使用的时间单位方法操作（年月日时分秒）
 *              年-{@link DateHelper#offsetYear(Date, int)}（hutool未提供，DateHelper补加方法）
 *              月-{@link DateHelper#offsetMonth(Date, int)}
 *              日-{@link DateHelper#offsetDay(Date, int)}
 *              时-{@link DateHelper#offsetHour(Date, int)}
 *              分-{@link DateHelper#offsetMinute(Date, int)}
 *              ...
 *      </pre>
 * </li>
 * <li>时间周期计算--{@link DateHelper#offsetCircle(CircleDate, int)}（hutool未提供，DateHelper补加方法）</li>
 * <li>将Date转LocalDateTime\LocalDate\LocalTime--toLocal***()</li>
 * <li>在Date1，Date2之间按相同时间间隔返回一系列时间--{@link DateUtil#range(Date, Date, DateField)}系列方法。
 * 也可以使用时间间隔单位方法（hutool未提供，DateHelper补加方法）{@link DateHelper#rangeDay(Date, Date)}(对于天单位操作，也可以对秒、分、小时等) 等
 * <pre>
 *          * {@link DateHelper#rangeSpecialWeek(Date, Date, Week)}方法来获取对应时间段中返回指定的周几的集合
 *          * 根据给定的周期（含有开始时间start和结束时间end）生成前n个周期或者后n周期的集合{@link DateHelper#rangeCircle(Date, Date, int)}
 *          * 对于不满足可以使用全参数方法来实现具体逻辑：{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
 *      </pre>
 * </li>
 * <li>判断时间是否相等{@link DateUtil#isSameDay(Date, Date)}等系列方法</li>
 * <li>计算两个时间差具体值{@link DateUtil#between(Date, Date, DateUnit)} 等系列方法</li>
 * <li>等其他功能请参考hu tool时间工具包</li>
 * </ui>
 *
 * @Author: jurentiancheng
 * @date 2021/12/7
 */
public class DateHelper extends DateUtil {

    /**
     * 将Date类型转化为LocalDate
     *
     * @param date 时间
     * @return 返回date是的LocalDate类型
     */
    public static LocalDate toLocalDate(Date date) {
        //DateUtil.toLocalDateTime中做了date==null的逻辑
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date类型转化为LocalTime
     *
     * @param date 时间
     * @return 返回date是的LocalTime类型
     */
    public static LocalTime toLocalTime(Date date) {
        //DateUtil.toLocalDateTime中做了date==null的逻辑
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toLocalTime();
    }

    /**
     * 按年维度累计；按date时间+amount年或-amount年；返回一个新时间；date值本身不会变换
     *
     * @param date   时间
     * @param amount 累加时间数值；正数以date之后时间；负数以date之前时间
     * @return 累加后时间
     */
    public static DateTime offsetYear(Date date, int amount) {
        return DateUtil.offset(date, DateField.YEAR, amount);
    }

    /**
     * 请参考{@link DateHelper#offsetCircle(Date, Date, int)}
     *
     * @param date 周期时间(开始时间和结束时间)
     * @param step 该周期+amount或者-amount周期
     * @return 返回一个新的周期时间
     */
    public static CircleDate offsetCircle(CircleDate date, int step) {
        if (date == null || date.getStart() == null || date.getEnd() == null) {
            throw new IllegalArgumentException("date must not be null!");
        }
        return DateHelper.offsetCircle(date.getStart(), date.getEnd(), step);
    }

    /**
     * 根据指定周期，获取下一个周期
     *
     * @param date 周期时间(开始时间和结束时间)
     * @return 返回下一个新的周期时间
     */
    public static CircleDate nextCircle(CircleDate date) {
        return offsetCircle(date, 1);
    }

    /**
     * 根据指定周期，获取下一个周期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回下一个新的周期时间
     */
    public static CircleDate nextCircle(Date start, Date end) {
        return offsetCircle(start, end, 1);
    }

    /**
     * 根据指定周期，获取上一个周期
     *
     * @param date 周期时间(开始时间和结束时间)
     * @return 返回上一个新的周期时间
     */
    public static CircleDate prevCircle(CircleDate date) {
        return offsetCircle(date, -1);
    }

    /**
     * 根据指定周期，获取上一个周期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回上一个新的周期时间
     */
    public static CircleDate prevCircle(Date start, Date end) {
        return offsetCircle(start, end, -1);
    }

    /**
     * 根据start～end周期，对该周期进行+amount或者-amount，返回一个新的周期。
     * <p>周期时间的计算逻辑：</p>
     * 以end时间跟start时间的相差值（时间戳）为一个周期circle = [start,end]；并按circle周期以step步数进行计算对应的周期
     * <pre>
     *    周期 circle = end-start
     *    newStart = start + step * circle
     *    newEnd   = end   + step * circle
     * </pre>
     * 最终结果为 [newStart,newEnd]
     *
     * @param start 周期时间的开始时间
     * @param end   周期时间的结束时间
     * @param step  该周期+step个或者-step个周期
     * @return 返回一个新的周期时间
     */
    public static CircleDate offsetCircle(Date start, Date end, int step) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("two date must not be null!");
        }
        //计算两个时间差（时间戳）
        long diff = DateUtil.between(start, end, DateUnit.MS);
        long circleDiff = step * diff;
        DateTime newStart = DateUtil.offsetMillisecond(start, (int) circleDiff);
        DateTime newEnd = DateUtil.offsetMillisecond(end, (int) circleDiff);
        return CircleDate.builder().start(newStart).end(newEnd).build();
    }

    /**
     * 根据固定间隔和个数生成对应结果
     * 以date为开始时间；按interval为间隔，生成count个值
     *
     * @param date     指定操作时间
     * @param interval 间隔(单位秒)
     * @param count    需要执行个数;count必须大于0
     * @return 返回结果集合
     */
    public static List<DateTime> rangeSecond(Date date, int interval, int count) {
        DateTime dateTime = DateUtil.offsetSecond(date, interval * count);
        return date.before(dateTime) ? rangeSecond(date, dateTime, Math.abs(interval)) : rangeSecond(dateTime, date, Math.abs(interval));
    }

    /**
     * 从start开始到end结束（含start），每相差一秒记录数据，不超过end（如果end是满足条件的最后一个，那么end也是含到结果中）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间数组
     */
    public static List<DateTime> rangeSecond(Date start, Date end) {
        return rangeSecond(start, end, 1);
    }

    /**
     * 从start开始到end结束（含start），每相差interval秒记录数据,不超过end（如果end是满足条件的最后一个，那么end也是含到结果中）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔（秒）
     * @return 时间数组
     */
    public static List<DateTime> rangeSecond(Date start, Date end, int interval) {
        return DateUtil.rangeToList(start, end, DateField.SECOND, interval);
    }

    /**
     * 从start开始（含start），每相差一分钟记录数据。不超过end（如果end是满足条件的最后一个，那么end也是含到结果中）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间数组
     */
    public static List<DateTime> rangeMinute(Date start, Date end) {
        return rangeMinute(start, end, 1);
    }

    /**
     * 从start开始（含start），每相差interval分钟记录数据。不超过end（如果end是满足条件的最后一个，那么end也是含到结果中）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔（分）
     * @return 时间数组
     */
    public static List<DateTime> rangeMinute(Date start, Date end, int interval) {
        return DateUtil.rangeToList(start, end, DateField.MINUTE, interval);
    }

    /**
     * 从start开始（含start），每相差一个小时记录数据。不超过end（可以等于end即end是满足条件的最后一个）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间数组
     */
    public static List<DateTime> rangeHour(Date start, Date end) {
        return rangeHour(start, end, 1);
    }

    /**
     * 从start开始（含start），每相差interval个小时记录数据。不超过end（可以等于end即end是满足条件的最后一个）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔（时）
     * @return 时间数组
     */
    public static List<DateTime> rangeHour(Date start, Date end, int interval) {
        return DateUtil.rangeToList(start, end, DateField.HOUR, interval);
    }


    /**
     * 从start开始（含start），每相差一天记录数据。不超过end（可以等于end即end是满足条件的最后一个）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间数组
     */
    public static List<DateTime> rangeDay(Date start, Date end) {
        return rangeDay(start, end, 1);

    }

    /**
     * 从start开始（含start），每相差interval天记录数据。不超过end（可以等于end即end是满足条件的最后一个）时间即可。
     * 对于start和end如果要强制不要包含到结果中，可以使用{@link DateHelper#rangToList(Date, Date, DateField, int, boolean, boolean)}
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔（天）
     * @return 时间数组
     */
    public static List<DateTime> rangeDay(Date start, Date end, int interval) {
        return DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR, interval);

    }

    /**
     * 以[start,end]为周期。周期时间定义：circle = end - start;circle就是时间戳差
     * <p>count为正数，则以[start,end]为界限，向后推进；
     * <p><pre>
     *     结果：[[start,end],[start+1*circle,end+1*circle],[start+2*circle,end+2*circle],...,[start+count*circle,end+count*circle]]
     * </pre></p>
     * <p>
     * count为负数，则以【start,end】为界限，向前推进；
     *
     * <p><pre>
     *      结果：[[start-count*circle,end-count*circle],...,[start-2*circle,end-2*circle],[start-1*circle,end-1*circle],[start,end]]
     *  </pre></p>
     *
     * @param start 周期开始时间
     * @param end   周期结束时间
     * @param count 生成count个[start,end]
     * @return 返回具有count个周期的结果集
     */
    public static List<CircleDate> rangeCircle(Date start, Date end, int count) {
        List<CircleDate> circles = new ArrayList<>(count);
        //计算两个时间差（时间戳）
        long diff = DateUtil.between(start, end, DateUnit.MS);
        Date movStart = start;
        Date movEnd = end;
        for (int i = 0; i < count; i++) {
            movStart = DateUtil.offsetMillisecond(movStart, (int) diff);
            movEnd = DateUtil.offsetMillisecond(movEnd, (int) diff);
            CircleDate build = CircleDate.builder().start(movStart).end(movEnd).build();
            circles.add(build);
        }
        return circles;
    }

    /**
     * 请参考{@link DateHelper#rangeCircle(Date, Date, int)}说明
     *
     * @param circleDate 周期（开始时间和结束时间）
     * @param count      生成count个[start,end]
     * @return 返回具有count个周期的结果集
     */
    public static List<CircleDate> rangeCircle(CircleDate circleDate, int count) {
        return rangeCircle(circleDate.getStart(), circleDate.getEnd(), count);
    }

    /**
     * 从start开始（含start)到end时间范围。返回与指定的week的值相同的一系列结果。不超过end（可以等于end即end是满足条件的最后一个）时间即可。
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param week  指定具体周几{@link Week}
     * @return 时间数组
     */
    public static List<DateTime> rangeSpecialWeek(Date start, Date end, Week week) {
        if (start.after(end)) {
            return new ArrayList<>();
        }
        Date mov = start;
        while (DateUtil.dayOfWeek(mov) != week.getValue()) {
            mov = DateUtil.offsetDay(mov, 1);
        }
        if (mov.after(end)) {
            return new ArrayList<>();
        }
        return rangeDay(mov, end, 7);
    }

    /**
     * 根据步 进单位获取起始日期时间和结束日期时间的时间区间集合;
     * 其实{@link DateUtil#rangeToList(Date, Date, DateField)}以及提供了相关内容；
     * <p>本方法只是扩展了是否包含开始时间和结束时间；{@link DateUtil#rangeToList(Date, Date, DateField)}默认包含了开始和结束时间。
     * 基于业务考虑，可能不需要开始时间或者结束时间.故加了该方法
     * </p>
     *
     * @param start          起始日期时间（包括）
     * @param end            结束日期时间
     * @param unit           步 进 单位
     * @param step           步 进 数
     * @param isIncludeStart 是否包含开始的时间
     * @param isIncludeEnd   是否包含结束的时间
     * @return {@link List}<{@link DateTime}>
     */
    public static List<DateTime> rangToList(Date start, Date end, final DateField unit, int step, boolean isIncludeStart, boolean isIncludeEnd) {
        return CollUtil.newArrayList((Iterable<DateTime>) new DateRange(start, end, unit, step, isIncludeStart, isIncludeEnd));
    }
}
