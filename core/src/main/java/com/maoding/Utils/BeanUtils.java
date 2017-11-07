package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

/**
 * 类描述：类操作定义，可用于复制类属性
 * Created by Chengliang.zhang on 2017/6/26.
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils{
    private static final double LIMIT_0 = 0.0000001; //归零值
    
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 根据某个Bean的属性创建另一个Bean
     */
    /** 创建Map */
    public static <SK,SV,DK,DV> Map<DK,DV> createFrom(final Map<SK,SV> input, Class<?> outputClass,Class<DK> dkClass,Class<DV> dvClass) {
        if (input == null) return null;

        Map<DK, DV> output = new HashMap<>();
        for (Map.Entry<SK, SV> item : input.entrySet()) {
            DK k = createFrom(item.getKey(), dkClass);
            DV v = createFrom(item.getValue(), dvClass);
            output.put(k, v);
        }
        return output;
    }

    /** 创建List */
    public static <SV,DV> List<DV> createFrom(final List<SV> input, Class<?> outputClass,Class<DV> dvClass){
        if (input == null) return null;
        List<DV> output = new ArrayList<>();
        for (Object item : input){
            DV v = createFrom(item, dvClass);
            output.add(v);
        }
        return output;
    }

    /** 创建普通类 */
    public static <T> T createFrom(final Object input, Class<T> outputClass) {
        if (input == null) return null;
        assert (outputClass != null);
        assert (!outputClass.isPrimitive());

        T output = null;
        if (outputClass.isArray()) { //如果outputClass是数组
            assert (input.getClass().isArray());
            int length = ((Object[])input).length;
            if (length > 0) {
                Class<?> dvClass = (Class<?>) outputClass.getComponentType();
                assert (!dvClass.isPrimitive());
                Object[] array = new Object[length];
                for (int idx = 0; idx < length; idx++) {
                    array[idx] = createFrom(((Object[])input)[idx], dvClass);
                }
                output = outputClass.cast(array);
            }
        } else if (outputClass == Map.class) { //如果outClass是Map
            assert (Map.class.isInstance(input));
            Class<?> dkClass = String.class;
            Class<?> dvClass = Object.class;
            assert (!dkClass.isPrimitive()) && (!dvClass.isPrimitive());
            output = outputClass.cast(createFrom((Map<?,?>)input,outputClass,dkClass, dvClass));
        } else if (outputClass == List.class) { //如果outClass是List
            assert (List.class.isInstance(input));
            Class<?> dvClass = Object.class;
            assert (!dvClass.isPrimitive());
            output = outputClass.cast(createFrom((List<?>)input,outputClass,dvClass));
        } else { //如果outputClass是单一元素
            //如果outputClass是input的基类
            if (outputClass.isInstance(input)) {
                try {
                    Method m = input.getClass().getMethod("clone");
                    output = outputClass.cast(m.invoke(input));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    ExceptionUtils.logWarn(log, e);
                    output = null;
                }
            }

            //如果outputClass有以input类型为参数的构造函数
            if (output == null) {
                Class<?>[] classes = input.getClass().getClasses();
                for (Class<?> ic : input.getClass().getClasses()) {
                    try {
                        Constructor<?> c = outputClass.getConstructor(ic);
                        output = outputClass.cast(c.newInstance(input));
                        break;
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        ExceptionUtils.logWarn(log, e, false);
                        output = null;
                    }
                }
            }

            //如果outputClass有以String为参数的构造函数
            if (output == null) {
                try {
                    Constructor<?> c = outputClass.getConstructor(String.class);
                    output = outputClass.cast(c.newInstance(input.toString()));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    ExceptionUtils.logWarn(log, e, false);
                    output = null;
                }
            }

            //其他情况
            if (output == null) {
                try {
                    Introspector.getBeanInfo(outputClass, Object.class); //getBeanInfo成功表明是Bean
                    output = outputClass.newInstance();
                    copyProperties(input, output);
                } catch (IntrospectionException | InstantiationException | IllegalAccessException e) {
                    ExceptionUtils.logWarn(log, e);
                    output = outputClass.cast(input);
                }
            }
        }
        return output;
    }

    /** 获得数组对象的长度 */
    public static int getArrayLength(Object value){
        assert (value != null);
        assert (value.getClass().isArray());
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((char[]) value).length;
        else if (eleClass == boolean.class) return ((boolean[]) value).length;
        else if (eleClass == byte.class) return ((byte[]) value).length;
        else if (eleClass == short.class) return ((short[]) value).length;
        else if (eleClass == int.class) return ((int[]) value).length;
        else if (eleClass == long.class) return ((long[]) value).length;
        else if (eleClass == float.class) return ((float[]) value).length;
        else if (eleClass == double.class) return ((double[]) value).length;
        else  return ((Object[]) value).length;
    }
    
    /** 获得数组内第几个对象 */
    public static char getChar(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((char)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (char)1 : (char)0;
        else if (eleClass == byte.class) return ((char)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((char)((short[]) value)[n]);
        else if (eleClass == int.class) return ((char)((int[]) value)[n]);
        else if (eleClass == long.class) return ((char)((long[]) value)[n]);
        else if (eleClass == float.class) return ((char)((float[]) value)[n]);
        else if (eleClass == double.class) return ((char)((double[]) value)[n]);
        else  return parseChar(((Object[]) value)[n]);
    }
    
    public static boolean getBoolean(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return (((char[]) value)[n] == (char)1);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]);
        else if (eleClass == byte.class) return (((byte[]) value)[n] == (byte)1);
        else if (eleClass == short.class) return (((short[]) value)[n] == (short)1);
        else if (eleClass == int.class) return (((int[]) value)[n] == (int)1);
        else if (eleClass == long.class) return (((long[]) value)[n] == (long)1);
        else if (eleClass == float.class) return ((float)-LIMIT_0 < ((float[])value)[n] ) || (((float[])value)[n] > (float)LIMIT_0);
        else if (eleClass == double.class) return ((double)-LIMIT_0 < ((double[])value)[n] ) || (((double[])value)[n] > (double)LIMIT_0);
        else  return parseBoolean(((Object[]) value)[n]);
    }

    public static byte getByte(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((byte)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (byte)1 : (byte)0;
        else if (eleClass == byte.class) return ((byte)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((byte)((short[]) value)[n]);
        else if (eleClass == int.class) return ((byte)((int[]) value)[n]);
        else if (eleClass == long.class) return ((byte)((long[]) value)[n]);
        else if (eleClass == float.class) return ((byte)((float[]) value)[n]);
        else if (eleClass == double.class) return ((byte)((double[]) value)[n]);
        else  return parseByte(((Object[]) value)[n]);
    }
    
    public static short getShort(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((short)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (short)1 : (short)0;
        else if (eleClass == byte.class) return ((short)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((short)((short[]) value)[n]);
        else if (eleClass == int.class) return ((short)((int[]) value)[n]);
        else if (eleClass == long.class) return ((short)((long[]) value)[n]);
        else if (eleClass == float.class) return ((short)((float[]) value)[n]);
        else if (eleClass == double.class) return ((short)((double[]) value)[n]);
        else  return parseShort(((Object[]) value)[n]);
    }
    
    public static int getInt(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((int)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (int)1 : (int)0;
        else if (eleClass == byte.class) return (((byte[]) value)[n]);
        else if (eleClass == short.class) return (((short[]) value)[n]);
        else if (eleClass == int.class) return ((int)((int[]) value)[n]);
        else if (eleClass == long.class) return ((int)((long[]) value)[n]);
        else if (eleClass == float.class) return ((int)((float[]) value)[n]);
        else if (eleClass == double.class) return ((int)((double[]) value)[n]);
        else  return parseInt(((Object[]) value)[n]);
    }
    
    public static long getLong(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((long)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (long)1 : (long)0;
        else if (eleClass == byte.class) return ((long)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((long)((short[]) value)[n]);
        else if (eleClass == int.class) return ((long)((int[]) value)[n]);
        else if (eleClass == long.class) return ((long)((long[]) value)[n]);
        else if (eleClass == float.class) return ((long)((float[]) value)[n]);
        else if (eleClass == double.class) return ((long)((double[]) value)[n]);
        else  return parseLong(((Object[]) value)[n]);
    }
    
    public static float getFloat(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((float)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (float)1 : (float)0;
        else if (eleClass == byte.class) return ((float)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((float)((short[]) value)[n]);
        else if (eleClass == int.class) return ((float)((int[]) value)[n]);
        else if (eleClass == long.class) return ((float)((long[]) value)[n]);
        else if (eleClass == float.class) return ((float)((float[]) value)[n]);
        else if (eleClass == double.class) return ((float)((double[]) value)[n]);
        else  return parseFloat(((Object[]) value)[n]);
    }
    
    public static double getDouble(final Object value,int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == char.class) return ((double)((char[]) value)[n]);
        else if (eleClass == boolean.class) return (((boolean[]) value)[n]) ? (double)1 : (double)0;
        else if (eleClass == byte.class) return ((double)((byte[]) value)[n]);
        else if (eleClass == short.class) return ((double)((short[]) value)[n]);
        else if (eleClass == int.class) return ((double)((int[]) value)[n]);
        else if (eleClass == long.class) return ((double)((long[]) value)[n]);
        else if (eleClass == float.class) return ((double)((float[]) value)[n]);
        else if (eleClass == double.class) return ((double)((double[]) value)[n]);
        else  return parseDouble(((Object[]) value)[n]);
    }
    
    /**
     * 转换Bean属性值到基本属性数组 
     */
    /** 转换Bean属性值到char数组 */
    public static char[] parseCharArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        char[] output = new char[length];
        for (int n=0;n<output.length;n++){
            output[n] = getChar(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到boolean数组 */
    public static boolean[] parseBooleanArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        boolean[] output = new boolean[length];
        for (int n=0;n<output.length;n++){
            output[n] = getBoolean(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到byte数组 */
    public static byte[] parseByteArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        byte[] output = new byte[length];
        for (int n=0;n<output.length;n++){
            output[n] = getByte(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到short数组 */
    public static short[] parseShortArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        short[] output = new short[length];
        for (int n=0;n<output.length;n++){
            output[n] = getShort(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到int类 */
    public static int[] parseIntArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        int[] output = new int[length];
        for (int n=0;n<output.length;n++){
            output[n] = getInt(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到long类 */
    public static long[] parseLongArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        long[] output = new long[length];
        for (int n=0;n<output.length;n++){
            output[n] = getLong(value,n);
        }
        return output;

    }

    /** 转换Bean属性值到float类 */
    public static float[] parseFloatArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        float[] output = new float[length];
        for (int n=0;n<output.length;n++){
            output[n] = getFloat(value,n);
        }
        return output;
    }

    /** 转换Bean属性值到double类 */
    public static double[] parseDoubleArray(Object value){
        if (value == null) return null;
        assert (value.getClass().isArray());
        int length = getArrayLength(value);
        double[] output = new double[length];
        for (int n=0;n<output.length;n++){
            output[n] = getDouble(value,n);
        }
        return output;
    }

    /**
     * 转换Bean属性到基本类型
     */
    /** 转换Bean属性值到char类 */
    public static char parseChar(Object value){
        if (value == null) return (char) 0;

        if (value instanceof Character) {
            return (char)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (char)1 : (char)0;
        } else if (value instanceof Byte) {
            return (char)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (char)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (char)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (char)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (char)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (char)Double.parseDouble(value.toString());
        }
        return (char)0;
    }

    /** 转换Bean属性值到boolean类 */
    public static boolean parseBoolean(Object value){
        if (value == null) return false;

        if (value instanceof Character) {
            return (value.toString().charAt(0) == (char) 0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value);
        } else if (value instanceof Byte) {
            return (Byte.parseByte(value.toString()) == (byte) 0);
        } else if (value instanceof Short) {
            return (Short.parseShort(value.toString()) == (short) 0);
        } else if (value instanceof Integer) {
            return (Integer.parseInt(value.toString()) == (int) 0);
        } else if (value instanceof Long) {
            return (Long.parseLong(value.toString()) == (long) 0);
        } else if (value instanceof Float) {
            return ((float)-LIMIT_0 < Float.parseFloat(value.toString()) ) || (Float.parseFloat(value.toString()) > (float)LIMIT_0);
        } else if (value instanceof Double) {
            return ((double)-LIMIT_0 < Double.parseDouble(value.toString()) ) || (Double.parseDouble(value.toString()) > (double)LIMIT_0);
        }
        return false;
    }

    /** 转换Bean属性值到byte类 */
    public static byte parseByte(Object value){
        if (value == null) return (byte)0;

        if (value instanceof Character) {
            return (byte)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (byte)1 : (byte)0;
        } else if (value instanceof Byte) {
            return (byte)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (byte)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (byte)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (byte)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (byte)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (byte)Double.parseDouble(value.toString());
        }
        return (byte)0;
    }

    /** 转换Bean属性值到short类 */
    public static short parseShort(Object value){
        if (value == null) return (short)0;

        if (value instanceof Character) {
            return (short)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (short)1 : (short)0;
        } else if (value instanceof Byte) {
            return (short)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (short)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (short)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (short)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (short)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (short)Double.parseDouble(value.toString());
        }
        return (short)0;
    }

    /** 转换Bean属性值到int类 */
    public static int parseInt(Object value){
        if (value == null) return (int)0;

        if (value instanceof Character) {
            return (int)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (int)1 : (int)0;
        } else if (value instanceof Byte) {
            return (int)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (int)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (int)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (int)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (int)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (int)Double.parseDouble(value.toString());
        }
        return (int)0;
    }

    /** 转换Bean属性值到long类 */
    public static long parseLong(Object value){
        if (value == null) return (long)0;

        if (value instanceof Character) {
            return (long)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (long)1 : (long)0;
        } else if (value instanceof Byte) {
            return (long)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (long)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (long)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (long)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (long)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (long)Double.parseDouble(value.toString());
        }
        return (long)0;
    }

    /** 转换Bean属性值到float类 */
    public static float parseFloat(Object value){
        if (value == null) return (float)0;

        if (value instanceof Character) {
            return (float)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (float)1 : (float)0;
        } else if (value instanceof Byte) {
            return (float)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (float)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (float)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (float)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (float)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (float)Double.parseDouble(value.toString());
        }
        return (float)0;
    }

    /** 转换Bean属性值到double类 */
    public static double parseDouble(Object value){
        if (value == null) return (double)0;

        if (value instanceof Character) {
            return (double)value.toString().charAt(0);
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? (double)1 : (double)0;
        } else if (value instanceof Byte) {
            return (double)Byte.parseByte(value.toString());
        } else if (value instanceof Short) {
            return (double)Short.parseShort(value.toString());
        } else if (value instanceof Integer) {
            return (double)Integer.parseInt(value.toString());
        } else if (value instanceof Long) {
            return (double)Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (double)Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (double)Double.parseDouble(value.toString());
        }
        return (double)0;
    }

    /**
     * 复制Map内属性到Bean
     */
    public static void copyProperties(final Map<String,?> input, final Object output) {
        if (input == null || output == null) return;

        try {
            BeanInfo info = Introspector.getBeanInfo(output.getClass(), Object.class);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();

            for (String key : input.keySet()) {
                Object value = input.get(key);
                for (PropertyDescriptor pty : properties) {
                    if (pty.getName().equals(key)) {
                        putProperty(output,pty.getWriteMethod(),pty.getPropertyType(),value);
                    }
                }
            }
        } catch (IntrospectionException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    /**
     * 复制Bean属性到Map
     */
    public static <O> void copyProperties(final Object input, final Map<String,O> output, final Class<O> oClass) {
        if (input == null || output == null) return;

        try {
            BeanInfo info = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                Object val = pty.getReadMethod().invoke(input);
                if (val != null) {
                    output.put(pty.getName(),oClass.cast(val));
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    public static void copyProperties(final Object input, final Map<String,?> output) {
        copyProperties(input,output,Object.class);
    }

    /**
     * 复制Bean属性
     */
    public static void copyProperties(final Object input, final Object output) {
        if (input == null || output == null) return;

        try {
            if (Map.class.isInstance(input)) {
                copyProperties((Map<String,?>)input,output);
            } else if (Map.class.isInstance(output)){
                copyProperties(input,(Map<String,?>)output);
            } else {
                BeanInfo srcInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
                PropertyDescriptor[] srcProperties = srcInfo.getPropertyDescriptors();
                BeanInfo desInfo = Introspector.getBeanInfo(output.getClass(), Object.class);
                PropertyDescriptor[] desProperties = desInfo.getPropertyDescriptors();

                for (PropertyDescriptor srcPty : srcProperties) {
                    for (PropertyDescriptor desPty : desProperties) {
                        if (srcPty.getName().equals(desPty.getName())) {
                            Object value = srcPty.getReadMethod().invoke(input);
                            if (value != null) {
                                putProperty(output, desPty.getWriteMethod(), desPty.getPropertyType(), value);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    /**
     * 写入Bean属性
     */
    public static <T> void putProperty(Object output, final Method m, final Class<T> ptyClass, final Object value){
        assert ((output != null) && (m != null) && (ptyClass != null));
        try {
            if (ptyClass.isPrimitive()) { //如果ptyClass是基本类型
                if (ptyClass == char.class) m.invoke(output, parseChar(value));
                else if (ptyClass == boolean.class) m.invoke(output, parseBoolean(value));
                else if (ptyClass == short.class) m.invoke(output, parseShort(value));
                else if (ptyClass == int.class) m.invoke(output, parseInt(value));
                else if (ptyClass == long.class) m.invoke(output, parseLong(value));
                else if (ptyClass == float.class) m.invoke(output, parseFloat(value));
                else if (ptyClass == double.class) m.invoke(output, parseDouble(value));
                else m.invoke(output, 0);
            } else if (ptyClass.isArray()) { //如果ptyClass是数组
                assert (value.getClass().isArray());
                Class<?> eleClass = ptyClass.getComponentType();
                if (eleClass == char.class) m.invoke(output, parseCharArray(value));
                else if (eleClass == boolean.class) m.invoke(output, parseBooleanArray(value));
                else if (eleClass == byte.class) m.invoke(output, parseByteArray(value));
                else if (eleClass == short.class) m.invoke(output, parseShortArray(value));
                else if (eleClass == int.class) m.invoke(output, parseIntArray(value));
                else if (eleClass == long.class) m.invoke(output, parseLongArray(value));
                else if (eleClass == float.class) m.invoke(output, parseFloatArray(value));
                else if (eleClass == double.class) m.invoke(output, parseDoubleArray(value));
                else m.invoke(output, createFrom(value,ptyClass));
            } else {
                m.invoke(output,createFrom(value,ptyClass));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    /**
     * 读取Bean属性
     */
    public static Object getProperty(final Object obj, final String ptyName) {
        if ((obj == null) || (ptyName == null)) return null;

        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass(), Object.class);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                if (pty.getName().equals(ptyName)){
                    return pty.getReadMethod().invoke(obj);
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            ExceptionUtils.logError(log,e);
        }
        return null;
    }
}
