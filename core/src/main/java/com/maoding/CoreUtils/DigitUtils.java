package com.maoding.CoreUtils;

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
        return ((-LIMIT_0 < (d2 - d1)) && ((d2 - d1) < LIMIT_0));
    }

    public static Boolean isDigitalClass(final Class<?> clazz){
        return (clazz.isAssignableFrom(Byte.class)
                || clazz.isAssignableFrom(Short.class)
                || clazz.isAssignableFrom(Integer.class)
                || clazz.isAssignableFrom(Long.class)
                || clazz.isAssignableFrom(Float.class)
                || clazz.isAssignableFrom(Double.class)
        );
    }

    public static int parseInt(final Object value){
        if (value == null) {
            return 0;
        } else if (value.getClass().isPrimitive()) {
            return (int) value;
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? 1 : 0;
        } else if (value instanceof Character) {
            return (int)value.toString().charAt(0);
        } else if (value instanceof Byte) {
            return (int)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (int)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (int)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (int)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (int)Double.parseDouble(value.toString());
        } else {
            return 0;
        }
    }
    
}
