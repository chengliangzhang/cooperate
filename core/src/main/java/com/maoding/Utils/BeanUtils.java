package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 类描述：类操作定义，可用于复制类属性
 * Created by Chengliang.zhang on 2017/6/26.
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils{
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 清理Bean内为空字符串的属性，将其替换为null
     */
    public static <T> T cleanProperties(T obj){
        if (obj == null) return null;
        assert isBean(obj.getClass());

        try {
            BeanInfo srcInfo = Introspector.getBeanInfo(obj.getClass(),Object.class);
            PropertyDescriptor[] srcProperties = srcInfo.getPropertyDescriptors();

            for (PropertyDescriptor srcPty : srcProperties) {
                if (srcPty.getPropertyType() == String.class){
                    String value = (String)srcPty.getReadMethod().invoke(obj);
                    if (StringUtils.isEmpty(value)){
                        srcPty.getWriteMethod().invoke(obj,(String)null);
                    }
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            ExceptionUtils.logError(log,e);
        }
        return obj;
    }

    /**
     * 根据某个Bean的属性创建另一个Bean
     */
    /** 根据Bean创建Map */
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Class<DK> dkClass,final Class<DV> dvClass, Boolean allowNull) {
        if (input == null) return (allowNull) ? null : new HashMap<>();

        Map<DK, DV> output = new HashMap<>();
        copyProperties(input,output,dkClass,dvClass);
        return output;
    }
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Class<DK> dkClass,final Class<DV> dvClass){
        return createMapFrom(input,dkClass,dvClass,true);
    }
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Class<Map<DK,DV>> outputClass, final Class<DK> dkClass,final Class<DV> dvClass, Boolean allowNull){
        return createMapFrom(input,dkClass,dvClass,allowNull);
    }
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Class<Map<DK,DV>> outputClass, final Class<DK> dkClass,final Class<DV> dvClass){
        return createMapFrom(input,outputClass,dkClass,dvClass,true);
    }
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Map<DK,DV> outputClass, Boolean allowNull){
        return createMapFrom(input,(Class<DK>)String.class,(Class<DV>)Object.class,allowNull);
    }
    public static <DK,DV> Map<DK,DV> createMapFrom(final Object input, final Map<DK,DV> outputClass){
        return createMapFrom(input,(Class<DK>)String.class,(Class<DV>)Object.class,true);
    }

    /** 创建List */
    public static <SV,DV> List<DV> createListFrom(final List<SV> input, final Class<DV> dvClass, final Boolean allowNull){
        if (input == null) return (allowNull) ? null : new ArrayList<>();
        List<DV> output = new ArrayList<>();
        for (SV item : input){
            DV v = createFrom(item, dvClass, allowNull);
            output.add(v);
        }
        return output;
    }
    public static <SV,DV> List<DV> createListFrom(final List<SV> input, final Class<List<DV>> outputClass, final Class<DV> dvClass, final Boolean allowNull){
        return createListFrom(input,dvClass,allowNull);
    }
    public static <SV,DV> List<DV> createListFrom(final List<SV> input, final Class<List<DV>> outputClass, final Class<DV> dvClass){
        return createListFrom(input,outputClass,dvClass,true);
    }
    public static <SV,DV> List<DV> createListFrom(final List<SV> input, final Class<List<DV>> outputClass){
        return createListFrom(input,(Class<DV>)String.class,true);
    }
    public static <DV> List<DV> createListFrom(final Object input,  final Class<List<DV>> outputClass, final Class<DV> dvClass, final Boolean allowNull){
        if (input == null) return (allowNull) ? null : new ArrayList<>();
        assert (input instanceof List);
        return createListFrom((List<?>)input,dvClass,true);
    }
    public static <DV> List<DV> createListFrom(final Object input,  final Class<List<DV>> outputClass, final Class<DV> dvClass){
        return createListFrom((List<?>)input,outputClass,dvClass,true);
    }
    public static <DV> List<DV> createListFrom(final Object input,  final Class<List<DV>> outputClass){
        return createListFrom((List<?>)input,(Class<DV>)String.class,true);
    }

    /** 创建数组 */
    public static <T,E> T createArrayFrom(final Object input, final Class<T> outputClass, final Class<E> eleClass, final Boolean allowNull) {
        assert ((outputClass != null) && (eleClass != null) && (outputClass.isArray()) && (outputClass.getComponentType() == eleClass));
        if (input == null) return (allowNull) ? null : (T)((new ArrayList<E>()).toArray());

        if (eleClass == boolean.class) return (T)parseBooleanArray(input);
        else if (eleClass == char.class) return (T)parseCharArray(input);
        else if (eleClass == byte.class) return (T)parseByteArray(input);
        else if (eleClass == short.class) return (T)parseShortArray(input);
        else if (eleClass == int.class) return (T)parseIntArray(input);
        else if (eleClass == long.class) return (T)parseLongArray(input);
        else if (eleClass == float.class) return (T)parseFloatArray(input);
        else if (eleClass == double.class) return (T)parseDoubleArray(input);

        assert (input.getClass().isArray());
        int length = getArrayLength(input);
        
        E[] output = (E[])((new ArrayList<E>()).toArray());
        for (int n=0;n<output.length;n++){
            output[n] = createFrom(((Object[])input)[n],eleClass,allowNull);
        }        
        return (T)output;
    }

    /** 创建普通类 */
    public static <T> T createFrom(final Object input, final Class<T> outputClass, final Boolean allowNull) {
        assert ((outputClass != null) && !outputClass.isPrimitive());

        //创建非单一元素
        if (outputClass.isArray()) {
            return (T)createArrayFrom(input,outputClass,outputClass.getComponentType(),allowNull);
        } else if (Map.class.isAssignableFrom(outputClass)){
            return (T)createMapFrom(input,(Class<?>)String.class,(Class<?>)Object.class,allowNull);
        } else if (List.class.isAssignableFrom(outputClass)) {
            assert (input instanceof List);
            return (T)createListFrom((List<?>)input,(Class<?>)Object.class,true);
        }

        //创建单一元素
        if (input == null) return createNullFrom(outputClass, allowNull);

        T output = null;
        //如果outputClass是input的基类
        if (outputClass.isInstance(input)) {
            output = createByCloneFrom(input,outputClass);
        }

        //如果outputClass有以input类型为参数,或String为参数的构造函数
        if (output == null) {
            output = createByConstructorFrom(input,outputClass);
        }

        //outputClass是Bean
        if (output == null) {
            if (isBean(outputClass)){
                try {
                    output = outputClass.newInstance();
                    copyProperties(input, output);
                } catch ( InstantiationException | IllegalAccessException e) {
                    ExceptionUtils.logWarn(log, e,false,"从" + input.getClass().getName() + "复制属性到" + outputClass.getName() + "时出错");
                    output = null;
                }
            }
        }

        //其他情况
        if (output == null) {
            output = outputClass.cast(input);
        }
        return output;
    }
    public static <T> T createFrom(final Object input, final Class<T> outputClass){
        return createFrom(input,outputClass,false);
    }

    //创建空对象
    private static <T> T createNullFrom(final Class<T> outputClass, final Boolean allowNull){
        T output = null;
        try {
            output = (allowNull) ? null : outputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionUtils.logWarn(log, e, false, "复制来源为空时无法创建");
            output = null;
        }
        return output;
    }

    //通过clone创建
    private static <T> T createByCloneFrom(final Object input, final Class<T> outputClass){
        assert ((input != null) && (outputClass != null));

        final String CLONE_INTERFACE_NAME = "Cloneable";
        final String CLONE_METHOD_NAME = "clone";

        T output = null;
        try {
            Class<?>[] interfaces = input.getClass().getInterfaces();
            for (Class<?> c : interfaces){
                if (CLONE_INTERFACE_NAME.equals(c.getName())){
                    Method m = input.getClass().getMethod(CLONE_METHOD_NAME);
                    output = outputClass.cast(m.invoke(input));
                    break;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            ExceptionUtils.logWarn(log, e, false, input.getClass().getName() + "没有clone方法");
            output = null;
        }
        return output;
    }

    //通过构造函数创建
    private static <T> T createByConstructorFrom(final Object input, final Class<T> outputClass){
        assert ((input != null) && (outputClass != null));

        T output = null;

        Constructor<?>[] constructors = outputClass.getConstructors();
        Class<?>[] classes = input.getClass().getClasses();
        Boolean found = false;
        Constructor<?> stringConstructor = null;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1) {
                Class<?> ptype = (c.getParameterTypes())[0];
                if ((stringConstructor == null) && (ptype == String.class)) stringConstructor = c;
                if (!found) {
                    for (Class<?> ic : classes) {
                        if (ptype == ic) {
                            try {
                                output = outputClass.cast(c.newInstance(input));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                ExceptionUtils.logWarn(log, e, false, outputClass.getName() + "没有以" + input.getClass().getName() + "为参数的构造方法");
                                output = null;
                            }
                            found = true;
                            break;
                        }
                    }
                }
                if ((found) && (stringConstructor != null)) break;
            }
        }

        //如果outputClass有以String为参数的构造函数
        if ((output == null) && (stringConstructor != null)){
            try {
                output = outputClass.cast(stringConstructor.newInstance(input.toString()));
            } catch ( InstantiationException | IllegalAccessException | InvocationTargetException e) {
                ExceptionUtils.logWarn(log, e, false,outputClass.getName() + "没有以" + String.class.getName() + "为参数的构造方法");
                output = null;
            }
        }
        return output;
    }

    /**
     * 复制Bean属性
     */
    /** 从Map复制到Bean */
    public static <SK,SV> void copyProperties(final Map<SK,SV> input, Object output, final Class<SK> skClass) {
        if (input == null || output == null) return;
        assert (isBean(output.getClass()));

        try {
            BeanInfo info = Introspector.getBeanInfo(output.getClass(),Object.class);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();

            for (SK sk : input.keySet()) {
                SV val = input.get(sk);
                for (PropertyDescriptor pty : properties) {
                    if (pty.getName().equals(sk.toString())) {
                        putProperty(output, pty.getWriteMethod(), pty.getPropertyType(), val);
                    }
                }
            }
        } catch(IntrospectionException e){
            ExceptionUtils.logError(log, e);
        }
    }
    public static <SK,SV> void copyProperties(final Map<SK,SV> input, Object output){
        copyProperties(input,output,(Class<SK>)String.class);
    }

    /** 从List复制到List */
    public static <SV,DV> void copyProperties(final List<SV> input, List<DV> output, final Class<SV> svClass, final Class<DV> dvClass) {
        if (input == null || output == null) return;

        if (svClass == dvClass) {
            output.addAll((List<DV>)input);
        } else {
            for (SV sv : input) {
                DV dv = createFrom(sv, dvClass,true);
                output.add(dv);
            }
        }
    }
    public static <SV,DV> void copyProperties(final List<SV> input, List<DV> output) {
        copyProperties(input,output,(Class<SV>)Object.class,(Class<DV>)Object.class);
    }

    /** 从Bean复制到List */
    public static <DV> void copyProperties(final Object input, List<DV> output, final Class<DV> dvClass) {
        if (input == null || output == null) return;

        if (input instanceof List) {
            copyProperties((List<?>)input,output);
        } else {
            DV dv = (dvClass == input.getClass()) ? (DV)input : createFrom(input,dvClass,true);
            output.add(dv);
        }
    }

    /** 从Map复制到Map */
    public static <SK,SV,DK,DV> void copyProperties(final Map<SK,SV> input, Map<DK,DV> output, final Class<SK> skClass, final Class<SV> svClass, final Class<DK> dkClass, final Class<DV> dvClass) {
        if (input == null || output == null) return;

        if ((skClass == dkClass) && (svClass == dvClass)) {
            output.putAll((Map<DK,DV>)input);
        } else {
            for (SK sk : input.keySet()) {
                SV sv = input.get(sk);
                DK dk = (skClass == dkClass) ? (DK) sk : createFrom(sk, dkClass,true);
                DV dv = (svClass == dvClass) ? (DV) sv : createFrom(sv, dvClass,true);
                output.put(dk, dv);
            }
        }
    }
    public static <SK,SV,DK,DV> void copyProperties(final Map<SK,SV> input, Map<DK,DV> output, final Class<SK> skClass, final Class<DK> dkClass, final Class<DV> dvClass) {
        copyProperties(input,output,skClass,(Class<SV>)Object.class,dkClass,dvClass);
    }
    public static <SK,SV,DK,DV> void copyProperties(final Map<SK,SV> input, Map<DK,DV> output, final Class<DK> dkClass, final Class<DV> dvClass) {
        copyProperties(input,output,(Class<SK>)String.class,dkClass,dvClass);
    }
    public static <SK,SV,DK,DV> void copyProperties(final Map<SK,SV> input, Map<DK,DV> output) {
        copyProperties(input,output,(Class<DK>)String.class,(Class<DV>)Object.class);
    }

    /** 从Bean复制到Map */
    public static <DK,DV> void copyProperties(final Object input, Map<DK,DV> output, final Class<DK> dkClass, final Class<DV> dvClass) {
        if (input == null || output == null) return;
        assert (isBean(input.getClass()));
        if (input instanceof Map) {
            copyProperties((Map<?, ?>) input, (Map<?, ?>) output);
        } else {
            try {
                BeanInfo info = Introspector.getBeanInfo(input.getClass(),Object.class);
                PropertyDescriptor[] properties = info.getPropertyDescriptors();

                for (PropertyDescriptor pty : properties) {
                    Object val = pty.getReadMethod().invoke(input);
                    if (val != null) {
                        DK key = createFrom(pty, dkClass,true);
                        DV value = createFrom(val, dvClass,true);
                        output.put(key, value);
                    }
                }
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                ExceptionUtils.logError(log, e);
            }
        }
    }
    public static <DK,DV> void copyProperties(final Object input, Map<DK,DV> output, final Class<DK> dkClass) {
        copyProperties(input,output,dkClass,(Class<DV>)Object.class);
    }
    public static <DK,DV> void copyProperties(final Object input, Map<DK,DV> output) {
        copyProperties(input,output,String.class);
    }


    /** 从Bean复制到Bean */
    public static void copyProperties(final Object input, Object output) {
        if ((input == null) || (output == null)) return;
        assert isBean(output.getClass());

        if (output instanceof Map) {
            copyProperties(input,(Map<?,?>)output);
        } else if (input instanceof Map) {
            copyProperties((Map<?,?>)input,output);
        } else {
            try {
                BeanInfo srcInfo = Introspector.getBeanInfo(input.getClass(),Object.class);
                PropertyDescriptor[] srcProperties = srcInfo.getPropertyDescriptors();
                BeanInfo desInfo = Introspector.getBeanInfo(output.getClass(),Object.class);
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
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                ExceptionUtils.logError(log,e);
            }
        }
    }

    /**
     * 写入Bean属性
     */
    public static <T> void putProperty(Object output, final Method m, final Class<T> ptyClass, final Object value){
        assert ((output != null) && isBean(output.getClass()) && (m != null) && (ptyClass != null));
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
            } else {
                m.invoke(output,createFrom(value,ptyClass,true));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            ExceptionUtils.logError(log,e);
        }
    }

    /**
     * 读取Bean属性
     */
    public static Object getProperty(final Object obj, final String ptyName) {
        if ((obj == null) || (ptyName == null) || !isBean(obj.getClass())) return null;

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

    /** 判断是否Bean */
    public static Boolean isBean(final Class<?> objClass){
        assert (objClass != null);
        return (!objClass.isPrimitive()
                && !objClass.isArray()
                && objClass != Boolean.class
                && objClass != Character.class
                && objClass != Byte.class
                && objClass != Short.class
                && objClass != Integer.class
                && objClass != Long.class
                && objClass != Float.class
                && objClass != Double.class
                && objClass != String.class
                && objClass != Date.class
                && !List.class.isAssignableFrom(objClass)
        );
    }

    /** 获得数组对象的长度 */
    public static int getArrayLength(final Object value){
        if (value == null) return 0;

        assert (value.getClass().isArray());
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return ((boolean[]) value).length;
        else if (eleClass == char.class) return ((char[]) value).length;
        else if (eleClass == byte.class) return ((byte[]) value).length;
        else if (eleClass == short.class) return ((short[]) value).length;
        else if (eleClass == int.class) return ((int[]) value).length;
        else if (eleClass == long.class) return ((long[]) value).length;
        else if (eleClass == float.class) return ((float[]) value).length;
        else if (eleClass == double.class) return ((double[]) value).length;
        else return ((Object[]) value).length;
    }

    /** 转换为boolean类型 */
    public static <T> boolean parseBoolean(final T value){
        if (value == null) return false;

        if (value instanceof Boolean) return (Boolean)value;
        else if (value instanceof Character
                || value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                ) return (!"0".equals(value.toString().trim()));
        else if (value instanceof Float
                || value instanceof Double
                ) return (!DigitUtils.isSame(value,0));
        else return false;
    }

    public static <T> boolean parseBoolean(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseBoolean(value[n]);
    }

    public static boolean parseBoolean(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseBoolean (((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseBoolean(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseBoolean (((byte[]) value)[n]);
        else if (eleClass == short.class) return parseBoolean (((short[]) value)[n]);
        else if (eleClass == int.class) return parseBoolean (((int[]) value)[n]);
        else if (eleClass == long.class) return parseBoolean (((long[]) value)[n]);
        else if (eleClass == float.class) return parseBoolean (((float[]) value)[n]);
        else if (eleClass == double.class) return parseBoolean (((double[]) value)[n]);
        else return parseBoolean (((Object[]) value)[n]);
    }

    public static <T> boolean[] parseBooleanArray(final T[] value){
        if (value == null) return null;

        boolean[] output = new boolean[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseBoolean(value,n);
        }
        return output;
    }

    public static boolean[] parseBooleanArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        boolean[] output = new boolean[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseBoolean(value,n);
        }
        return output;
    }

    /** 转换为char类型 */
    public static <T> char parseChar(final T value){
        if (value == null) return (char)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (char)1 : (char)0;
        else if (value instanceof Character) return (char)value.toString().charAt(0);
        else if (value instanceof Byte) return (char)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (char)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (char)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (char)Long.parseLong(value.toString());
        else if (value instanceof Float) return (char)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (char)Double.parseDouble(value.toString());
        else return (char)0;
    }

    public static <T> char parseChar(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseChar(value[n]);
    }

    public static char parseChar(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseChar (((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseChar(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseChar (((byte[]) value)[n]);
        else if (eleClass == short.class) return parseChar (((short[]) value)[n]);
        else if (eleClass == int.class) return parseChar (((int[]) value)[n]);
        else if (eleClass == long.class) return parseChar (((long[]) value)[n]);
        else if (eleClass == float.class) return parseChar (((float[]) value)[n]);
        else if (eleClass == double.class) return parseChar (((double[]) value)[n]);
        else return parseChar (((Object[]) value)[n]);
    }

    public static <T> char[] parseCharArray(final T[] value){
        if (value == null) return null;

        char[] output = new char[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseChar(value,n);
        }
        return output;
    }

    public static char[] parseCharArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        char[] output = new char[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseChar(value,n);
        }
        return output;
    }

    /** 转换为byte类型 */
    public static <T> byte parseByte(final T value){
        if (value == null) return (byte)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (byte)1 : (byte)0;
        else if (value instanceof Character) return (byte)value.toString().charAt(0);
        else if (value instanceof Byte) return (byte)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (byte)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (byte)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (byte)Long.parseLong(value.toString());
        else if (value instanceof Float) return (byte)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (byte)Double.parseDouble(value.toString());
        else return (byte)0;
    }

    public static <T> byte parseByte(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseByte(value[n]);
    }

    public static byte parseByte(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseByte(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseByte(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseByte(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseByte(((short[]) value)[n]);
        else if (eleClass == int.class) return parseByte(((int[]) value)[n]);
        else if (eleClass == long.class) return parseByte(((long[]) value)[n]);
        else if (eleClass == float.class) return parseByte(((float[]) value)[n]);
        else if (eleClass == double.class) return parseByte(((double[]) value)[n]);
        else return parseByte (((Object[]) value)[n]);
    }

    public static <T> byte[] parseByteArray(final T[] value){
        if (value == null) return null;

        byte[] output = new byte[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseByte(value,n);
        }
        return output;
    }

    public static byte[] parseByteArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        byte[] output = new byte[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseByte(value,n);
        }
        return output;
    }

    /** 转换为short类型 */
    public static <T> short parseShort(final T value){
        if (value == null) return (short)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (short)1 : (short)0;
        else if (value instanceof Character) return (short)value.toString().charAt(0);
        else if (value instanceof Byte) return (short)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (short)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (short)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (short)Long.parseLong(value.toString());
        else if (value instanceof Float) return (short)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (short)Double.parseDouble(value.toString());
        else return (short)0;
    }

    public static <T> short parseShort(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseShort(value[n]);
    }

    public static short parseShort(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseShort(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseShort(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseShort(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseShort(((short[]) value)[n]);
        else if (eleClass == int.class) return parseShort(((int[]) value)[n]);
        else if (eleClass == long.class) return parseShort(((long[]) value)[n]);
        else if (eleClass == float.class) return parseShort(((float[]) value)[n]);
        else if (eleClass == double.class) return parseShort(((double[]) value)[n]);
        else return parseShort (((Object[]) value)[n]);
    }

    public static <T> short[] parseShortArray(final T[] value){
        if (value == null) return null;

        short[] output = new short[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseShort(value,n);
        }
        return output;
    }

    public static short[] parseShortArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        short[] output = new short[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseShort(value,n);
        }
        return output;
    }

    /** 转换为int类型 */
    public static <T> int parseInt(final T value){
        if (value == null) return (int)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (int)1 : (int)0;
        else if (value instanceof Character) return (int)value.toString().charAt(0);
        else if (value instanceof Byte) return (int)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (int)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (int)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (int)Long.parseLong(value.toString());
        else if (value instanceof Float) return (int)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (int)Double.parseDouble(value.toString());
        else return (int)0;
    }

    public static <T> int parseInt(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseInt(value[n]);
    }

    public static int parseInt(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseInt(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseInt(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseInt(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseInt(((short[]) value)[n]);
        else if (eleClass == int.class) return parseInt(((int[]) value)[n]);
        else if (eleClass == long.class) return parseInt(((long[]) value)[n]);
        else if (eleClass == float.class) return parseInt(((float[]) value)[n]);
        else if (eleClass == double.class) return parseInt(((double[]) value)[n]);
        else return parseInt (((Object[]) value)[n]);
    }

    public static <T> int[] parseIntArray(final T[] value){
        if (value == null) return null;

        int[] output = new int[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseInt(value,n);
        }
        return output;
    }

    public static int[] parseIntArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        int[] output = new int[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseInt(value,n);
        }
        return output;
    }

    /** 转换为long类型 */
    public static <T> long parseLong(final T value){
        if (value == null) return (long)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (long)1 : (long)0;
        else if (value instanceof Character) return (long)value.toString().charAt(0);
        else if (value instanceof Byte) return (long)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (long)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (long)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (long)Long.parseLong(value.toString());
        else if (value instanceof Float) return (long)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (long)Double.parseDouble(value.toString());
        else return (long)0;
    }

    public static <T> long parseLong(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseLong(value[n]);
    }

    public static long parseLong(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseLong(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseLong(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseLong(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseLong(((short[]) value)[n]);
        else if (eleClass == int.class) return parseLong(((int[]) value)[n]);
        else if (eleClass == long.class) return parseLong(((long[]) value)[n]);
        else if (eleClass == float.class) return parseLong(((float[]) value)[n]);
        else if (eleClass == double.class) return parseLong(((double[]) value)[n]);
        else return parseLong(((Object[]) value)[n]);
    }

    public static <T> long[] parseLongArray(final T[] value){
        if (value == null) return null;

        long[] output = new long[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseLong(value,n);
        }
        return output;
    }

    public static long[] parseLongArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        long[] output = new long[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseLong(value,n);
        }
        return output;
    }    
    
    /** 转换为float类型 */
    public static <T> float parseFloat(final T value){
        if (value == null) return (float)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (float)1 : (float)0;
        else if (value instanceof Character) return (float)value.toString().charAt(0);
        else if (value instanceof Byte) return (float)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (float)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (float)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (float)Long.parseLong(value.toString());
        else if (value instanceof Float) return (float)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (float)Double.parseDouble(value.toString());
        else return (float)0;
    }

    public static <T> float parseFloat(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseFloat(value[n]);
    }

    public static float parseFloat(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseFloat(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseFloat(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseFloat(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseFloat(((short[]) value)[n]);
        else if (eleClass == int.class) return parseFloat(((int[]) value)[n]);
        else if (eleClass == long.class) return parseFloat(((long[]) value)[n]);
        else if (eleClass == float.class) return parseFloat(((float[]) value)[n]);
        else if (eleClass == double.class) return parseFloat(((double[]) value)[n]);
        else return parseFloat(((Object[]) value)[n]);
    }

    public static <T> float[] parseFloatArray(final T[] value){
        if (value == null) return null;

        float[] output = new float[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseFloat(value,n);
        }
        return output;
    }

    public static float[] parseFloatArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        float[] output = new float[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseFloat(value,n);
        }
        return output;
    }
    
    /** 转换为double类型 */
    public static <T> double parseDouble(final T value){
        if (value == null) return (double)0;

        if (value instanceof Boolean) return ((Boolean)value) ? (double)1 : (double)0;
        else if (value instanceof Character) return (double)value.toString().charAt(0);
        else if (value instanceof Byte) return (double)Byte.parseByte(value.toString());
        else if (value instanceof Short) return (double)Short.parseShort(value.toString());
        else if (value instanceof Integer) return (double)Integer.parseInt(value.toString());
        else if (value instanceof Long) return (double)Long.parseLong(value.toString());
        else if (value instanceof Float) return (double)Float.parseFloat(value.toString());
        else if (value instanceof Double) return (double)Double.parseDouble(value.toString());
        else return (double)0;
    }

    public static <T> double parseDouble(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseDouble(value[n]);
    }

    public static double parseDouble(final Object value, final int n){
        assert (value != null);
        assert (value.getClass().isArray());
        assert (n < getArrayLength(value));
        Class<?> eleClass = value.getClass().getComponentType();
        if (eleClass == boolean.class) return parseDouble(((boolean[]) value)[n]);
        else if (eleClass == char.class) return parseDouble(((char[]) value)[n]);
        else if (eleClass == byte.class) return parseDouble(((byte[]) value)[n]);
        else if (eleClass == short.class) return parseDouble(((short[]) value)[n]);
        else if (eleClass == int.class) return parseDouble(((int[]) value)[n]);
        else if (eleClass == long.class) return parseDouble(((long[]) value)[n]);
        else if (eleClass == float.class) return parseDouble(((float[]) value)[n]);
        else if (eleClass == double.class) return parseDouble(((double[]) value)[n]);
        else return parseDouble(((Object[]) value)[n]);
    }

    public static <T> double[] parseDoubleArray(final T[] value){
        if (value == null) return null;

        double[] output = new double[value.length];
        for (int n=0;n<output.length;n++){
            output[n] = parseDouble(value,n);
        }
        return output;
    }

    public static double[] parseDoubleArray(final Object value){
        if (value == null) return null;

        assert (value.getClass().isArray());
        int length = getArrayLength(value);

        double[] output = new double[length];
        for (int n=0;n<output.length;n++){
            output[n] = parseDouble(value,n);
        }
        return output;
    }
}
