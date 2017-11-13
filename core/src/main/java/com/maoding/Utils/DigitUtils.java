package com.maoding.Utils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/11/9 9:41
 * 描    述 :
 */
public class DigitUtils {
    private static final double LIMIT_0 = 0.0000001; //归零值

    public static Boolean isSame(final Object num1, final Object num2){
        if ((num1 == null) && (num2 == null)) return true;
        if ((num1 == null) || (num2 == null)) return false;
        String n1 = num1.toString();
        String n2 = num2.toString();
        assert (StringUtils.isNumeric(n1) && StringUtils.isNumeric(n2));
        Double d1 = Double.parseDouble(n1);
        Double d2 = Double.parseDouble(n2);
        return ((d2 - d1) < -LIMIT_0) || (LIMIT_0 < (d2 - d1));
    }
}
