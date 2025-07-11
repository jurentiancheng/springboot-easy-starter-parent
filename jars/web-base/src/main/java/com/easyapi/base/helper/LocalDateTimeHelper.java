package com.easyapi.base.helper;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.easyapi.base.CircleLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LocalDateTime的工具类，其内容只会考虑在项目上常用的方法。并不会去实现功能特别强大且范围特别广的内容。
 * 本方法只是在hu tool工具包中的LocalDateTime时间处理类上补加基于项目上常用并且hu tool工具包未直接提供的功能。
 * 常用内容：</br>
 * <ui>
 * <li>获取LocalDateTime的时间戳{@link LocalDateTimeUtil#toEpochMilli(TemporalAccessor)}</li>
 * <li>将字符串按时间格式解析成LocalDateTime类型{@link LocalDateTimeUtil#parse(CharSequence, String)};format的格式可以使用{@link DateConstantHelper}</li>
 * <li>将LocalDateTime类型按格式化成字符串类型{@link LocalDateTimeUtil#format(LocalDate, String)};format的格式可以使用{@link DateConstantHelper}</li>
 * <li>LocalDateTime本身自带对时间加减操作，可以参考hutool的{@link LocalDateTimeUtil#offset(LocalDateTime, long, TemporalUnit)},也可以直接使用时间单位方法来计算{@link LocalDateTime#plusHours(long)}等方法</li>
 * <li>时间周期计算--{@link LocalDateTimeHelper#offsetCircle(LocalDateTime, LocalDateTime, int)}</li>
 * <li>在LocalDateTime1，LocalDateTime2之间按相同时间间隔返回一系列时间--{@link LocalDateTimeHelper#rangeToList(LocalDateTime, LocalDateTime, int, TemporalUnit, boolean, boolean)}系列方法。
 * 也可以使用时间间隔单位方法（hutool未提供，DateHelper补加方法）{@link LocalDateTimeHelper#rangeDay(LocalDateTime, LocalDateTime)}(对于天单位操作，也可以对秒、分、小时等) 等，不提供月份的range方法（会跟预期效果存在偏差比如2月29日）
 * </li>
 * <li>{@link LocalDateTimeHelper#rangeDayPartTime(LocalDateTime, LocalDateTime, LocalTime, LocalTime)}获取提供范围时间[start,end]内每一天对应的时间段[partStart,partEnd]结果</li>
 * </ui>
 *
 * @Author: jurentiancheng
 * @date 2022/9/19
 */
public class LocalDateTimeHelper extends LocalDateTimeUtil {

    /**
     * 根据指定周期，获取下一个周期
     *
     * @param date 周期时间(开始时间和结束时间)
     * @return 返回下一个新的周期时间
     */
    public static CircleLocalDateTime nextCircle(CircleLocalDateTime date) {
        return offsetCircle(date, 1);
    }

    /**
     * 根据指定周期，获取下一个周期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回下一个新的周期时间
     */
    public static CircleLocalDateTime nextCircle(LocalDateTime start, LocalDateTime end) {
        return offsetCircle(start, end, 1);
    }

    /**
     * 根据指定周期，获取上一个周期
     *
     * @param date 周期时间(开始时间和结束时间)
     * @return 返回上一个新的周期时间
     */
    public static CircleLocalDateTime prevCircle(CircleLocalDateTime date) {
        return offsetCircle(date, -1);
    }

    /**
     * 根据指定周期，获取上一个周期
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回上一个新的周期时间
     */
    public static CircleLocalDateTime prevCircle(LocalDateTime start, LocalDateTime end) {
        return offsetCircle(start, end, -1);
    }

    /**
     * 提供的周期（开始时间和结束时间），计算出新的周期
     *
     * @param circle 周期（开始时间和结束时间）
     * @param step   步进数量
     * @return 返回一个新周期
     */
    public static CircleLocalDateTime offsetCircle(CircleLocalDateTime circle, int step) {
        return offsetCircle(circle.getStart(), circle.getEnd(), step);
    }


    /**
     * 根据start～end周期，对该周期进行+amount或者-amount，返回一个新的周期。
     * <p>周期时间的计算逻辑：</p>
     * 以end时间跟start时间的相差值（时间戳）为一个周期circle = [start,end]；并按circle周期以step步数进行计算对应的周期
     * <blockquote>
     * <pre>
     *              周期 circle = end-start
     *              newStart = start + step * circle
     *              newEnd   = end   + step * circle
     *          </pre>
     * 最终结果为 [newStart,newEnd]
     * </blockquote>
     *
     * @param start 周期时间的开始时间
     * @param end   周期时间的结束时间
     * @param step  该周期+step个或者-step个周期
     * @return 返回一个新的周期时间
     */
    public static CircleLocalDateTime offsetCircle(LocalDateTime start, LocalDateTime end, int step) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("two localDateTime must not be null!");
        }
        long startMilli = LocalDateTimeUtil.toEpochMilli(start);
        long endMilli = LocalDateTimeUtil.toEpochMilli(end);
        long totalStep = (endMilli - startMilli) * step;
        LocalDateTime circleStart = start.plus(totalStep, ChronoUnit.MILLIS);
        LocalDateTime circleEnd = end.plus(totalStep, ChronoUnit.MILLIS);
        return CircleLocalDateTime.builder().start(circleStart).end(circleEnd).build();
    }


    /**
     * 根据固定间隔(1秒)生成对应结果
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeSecond(LocalDateTime start, LocalDateTime end) {
        return rangeSecond(start, end, 1);
    }

    /**
     * 根据固定间隔(interval秒)生成对应结果
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔数(单位秒)
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeSecond(LocalDateTime start, LocalDateTime end, int interval) {
        return rangeToList(start, end, interval, ChronoUnit.SECONDS, true, true);
    }

    /**
     * 根据固定间隔(1分)生成对应结果
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeMinute(LocalDateTime start, LocalDateTime end) {
        return rangeMinute(start, end, 1);
    }

    /**
     * 根据固定间隔(interval分)生成对应结果
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔数(单位分)
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeMinute(LocalDateTime start, LocalDateTime end, int interval) {
        return rangeToList(start, end, interval, ChronoUnit.MINUTES, true, true);
    }

    /**
     * 根据固定间隔(1小时)生成对应结果
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeHours(LocalDateTime start, LocalDateTime end) {
        return rangeHours(start, end, 1);
    }

    /**
     * 根据固定间隔(interval小时)生成对应结果
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔数(单位小时)
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeHours(LocalDateTime start, LocalDateTime end, int interval) {
        return rangeToList(start, end, interval, ChronoUnit.HOURS, true, true);
    }

    /**
     * 根据固定间隔(1天)生成对应结果
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeDay(LocalDateTime start, LocalDateTime end) {
        return rangeDay(start, end, 1);
    }

    /**
     * 根据固定间隔(interval天)生成对应结果
     *
     * @param start    开始时间
     * @param end      结束时间
     * @param interval 时间间隔数(单位天)
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeDay(LocalDateTime start, LocalDateTime end, int interval) {
        return rangeToList(start, end, interval, ChronoUnit.DAYS, true, true);
    }

    /**
     * 根据固定间隔和个数生成对应结果
     *
     * @param start          开始时间
     * @param end            结束时间
     * @param interval       时间间隔数
     * @param unit           时间单位{@link ChronoUnit}
     * @param isIncludeStart 是否包含开始时间
     * @param isIncludeEnd   是否包含结束时间
     * @return 根据时间间隔数返回满足条件所有时间集合
     */
    public static List<LocalDateTime> rangeToList(LocalDateTime start, LocalDateTime end, int interval, TemporalUnit unit, boolean isIncludeStart, boolean isIncludeEnd) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return new ArrayList<>(0);
        }
        //初步设置长度
        List<LocalDateTime> dates = new ArrayList((int) unit.between(start, end) / interval + 2);
        //复制开始时间
        LocalDateTime mov = start.plusSeconds(0);
        if (isIncludeStart) {
            dates.add(mov);
        }
        while ((mov = mov.plus(interval, unit)).isBefore(end)) {
            dates.add(mov);
        }
        if (isIncludeEnd) {
            //复制结束时间
            dates.add(end.plusSeconds(0));
        }
        return dates;
    }

    /**
     * 在时间范围内返回每一天所在时间段【partStart,partEnd】的集合
     *
     * @param start     开始时间
     * @param end       结束时间
     * @param partStart 开始时间段
     * @param partEnd   结束时间段
     * @return 返回每天对应时间段[partStart, partEnd]
     */
    public static List<CircleLocalDateTime> rangeDayPartTime(LocalDateTime start, LocalDateTime end, LocalTime partStart, LocalTime partEnd) {
        if (start == null || end == null || partStart == null || partEnd == null) {
            throw new IllegalArgumentException("There is an empty error in one parameter of the LocalDateHelper#rangeDayPartTime method");
        }
        long between = ChronoUnit.DAYS.between(start, end);
        List<CircleLocalDateTime> dates = new ArrayList<>((int) between);
        LocalDateTime mov = start.toLocalTime().isAfter(partStart) ? LocalDateTimeUtil.beginOfDay(start.plusDays(1)) : start;
        LocalDateTime realEnd = end.toLocalTime().isBefore(partEnd) ? LocalDateTimeUtil.beginOfDay(end) : end;
        while (mov.isBefore(realEnd)) {
            if (mov.toLocalTime().isBefore(partStart) || mov.toLocalTime().equals(partStart)) {
                LocalDateTime pStart = mov.with(partStart);
                LocalDateTime pEnd = mov.with(partEnd);
                dates.add(CircleLocalDateTime.builder().start(pStart).end(pEnd).build());
            }
            mov = mov.plusDays(1);
        }
        return dates;
    }
}
