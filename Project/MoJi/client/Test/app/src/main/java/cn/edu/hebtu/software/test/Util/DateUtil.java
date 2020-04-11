package cn.edu.hebtu.software.test.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil extends Object {

    public static DateComponents  calculateDateDeltaYMD(Date startDate, Date endDate){
        DateComponents deltaComp = new DateComponents();
        int flag = startDate.compareTo(endDate);
        if (null == startDate || null == endDate || flag > 0)
            return deltaComp;

        //1、 先转化成Calendar对象计算
        Calendar startCalendar = Calendar.getInstance(Locale.CHINA);
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance(Locale.CHINA);
        endCalendar.setTime(endDate);

        //2、提取年月日数据
        int sYear = startCalendar.get(Calendar.YEAR);
        int sMonth = startCalendar.get(Calendar.MONTH);
        int sDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        int eYear = endCalendar.get(Calendar.YEAR);
        int eMonth = endCalendar.get(Calendar.MONTH);
        int eDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        int eMonthDays = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //3、开始计算
        int deltaYear = eYear-sYear;
        int deltaMonth = eMonth-sMonth;
        int deltaDay = eDay-sDay+1;
        boolean eDayMonthEnd = eDay >= eMonthDays-1;
        //4、特殊处理
        //  1)先处理天
        if (deltaDay <= 0 && eDayMonthEnd){       //计算日期月末的情况
            deltaDay = eDay-eMonthDays+1;
        }

        if (deltaDay < 0){                       //当前天小于比较天
            //此时应该使用上月月末的日期计算差值，然后加上现在的天数
            Calendar ePreMonthCalendar = Calendar.getInstance(Locale.CHINA);
            ePreMonthCalendar.setTime(endDate);
            ePreMonthCalendar.add(Calendar.MONTH, -1);
            ePreMonthCalendar.set(Calendar.DAY_OF_MONTH,ePreMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            DateComponents preComp = calculateDateDeltaYMD(startDate, ePreMonthCalendar.getTime());
            deltaYear = preComp.year;
            deltaMonth = preComp.month;
            deltaDay = preComp.day+eDay;
        } else if (deltaDay >= eMonthDays){         //超月的情况 开始是月初一号，结束是月末的情况
            deltaDay -= eMonthDays;
            deltaMonth ++;
        }
        //  2)再处理月
        if (deltaMonth < 0){                        //月份小于的情况
            deltaMonth += 12;
            deltaYear --;
        } else if (deltaMonth > 12){                //月份超出的情况
            deltaMonth -= 12;
            deltaYear ++;
        }
        //5、组装数据
        deltaComp.year = deltaYear;
        deltaComp.month = deltaMonth;
        deltaComp.day = deltaDay;
        return deltaComp;
    }

    public static class DateComponents{
        public int year;
        public int month;
        public int day;

        public DateComponents(){
            year=month=day=0;
        }
    }

}