package cn.dexter.poker.redmine.dingding.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class StringUtil {

    private static final String ISO_8859_1 = "ISO-8859-1";

    private static final String UTF_8 = "UTF-8";

    public static <T> boolean isEmpty(@SuppressWarnings("unchecked") T... objs) {
        if (Objects.isNull(objs) || objs.length == 0) {
            return true;
        }
        for (Object obj : objs) {
            if (obj instanceof JSONObject) {
                return ((JSONObject) obj).isEmpty();
            }
            if (obj == null || obj.toString().length() == 0)
                return true;
        }
        return false;
    }

    public static String convertEncoding(String str, String srcEncode, String tarEncode) {
        String encodingString = str;
            try {
                encodingString = new String(str.getBytes(srcEncode), tarEncode);
            } catch (Exception e) {
            }
        return encodingString;
    }

    public static String convertEncoding(String str) {
        return convertEncoding(str, ISO_8859_1, UTF_8);
    }
















}
