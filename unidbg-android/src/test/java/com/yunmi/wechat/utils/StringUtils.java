package com.yunmi.wechat.utils;

public class StringUtils {
    public static boolean hasText(String in) {
        return !StringUtils.isEmpty(in);
    }

    public static boolean isEmpty(String in) {
        return in == null ? true : in.trim().length() < 1;
    }

    public static String timeLeft(long offset) {
        StringBuilder buffer = new StringBuilder();
        int b = 0;
        long days = offset / 86400000L;
        if(days > 0L) {
            b = 1;
            buffer.append(days).append('天');
        }

        long v14 = offset - 86400000L * days;
        long hour = v14 / 3600000L;
        if(hour > 0L || b != 0) {
            b = 1;
            buffer.append(hour).append("小时");
        }

        long v14_1 = v14 - 3600000L * hour;
        long minutes = v14_1 / 60000L;
        if(minutes > 0L || b != 0) {
            b = 1;
            buffer.append(minutes).append("分钟");
        }

        long seconds = (v14_1 - 60000L * minutes) / 1000L;
        if(seconds > 0L || b != 0) {
            buffer.append(seconds).append('秒');
        }

        if(buffer.length() < 1) {
            buffer.append("0秒");
        }

        return buffer.toString();
    }
}
