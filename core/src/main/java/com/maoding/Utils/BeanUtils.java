package com.maoding.Utils;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * 类描述：类操作定义，可用于复制类属性
 * Created by Chengliang.zhang on 2017/6/26.
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils{
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String DOT = ".";

    private static Map<Class, MethodAccess> methodMap = new HashMap<>();
    private static Map<String, Integer> methodIndexMap = new HashMap<>();
    private static Map<Class, List<String>> fieldMap = new HashMap<>();

    // 缓存类的读写方法
    private static MethodAccess cache(Class<?> clazz) {
        synchronized (clazz) {
            MethodAccess methodAccess = MethodAccess.get(clazz);
            List<Field> fieldList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            for (Class<?> c=clazz; c != Object.class; c = c.getSuperclass()){
                Field[] fields = c.getDeclaredFields();
                Collections.addAll(fieldList, fields);
                Method[] methods = c.getDeclaredMethods();
                Collections.addAll(methodList, methods);
            }

            if ((fieldList.size() > 0) && (methodList.size() > 0)) {
                List<String> fieldNameList = new ArrayList<>(fieldList.size());
                for (Field field : fieldList) {
                    if (!Modifier.isStatic(field.getModifiers())) { //是否是静态的
                        String fieldName = StringUtils.capitalize(field.getName()); // 获取属性名称
                        String getKey = GET + fieldName;
                        String setKey = SET + fieldName;
                        int n = 0;
                        for (Method method : methodList) {
                            if (StringUtils.isSame(method.getName(),getKey) && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0){
                                int getIndex = methodAccess.getIndex(getKey,0); // 获取get方法的下标
                                methodIndexMap.put(clazz.getName() + DOT + getKey, getIndex); // 将类名get方法名，方法下标注册到map中
                                if (++n > 2) break;
                            } else if (StringUtils.isSame(method.getName(),setKey) && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 1) {
                                int setIndex = methodAccess.getIndex(setKey,method.getParameterTypes()); // 获取set方法的下标
                                methodIndexMap.put(clazz.getName() + DOT + setKey, setIndex); // 将类名set方法名，方法下标注册到map中
                                if (++n > 2) break;
                            }
                        }
                        fieldNameList.add(fieldName); // 将属性名称放入集合里
                    }
                }
                fieldMap.put(clazz, fieldNameList); // 将类名列表放入到map中
            }
            assert(methodAccess != null);
            methodMap.put(clazz, methodAccess); // 将类的方法列表放入到map中
            return methodAccess;
        }
    }

    public static void copyProperties(final Object input, Map<String,Object> output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive()) && (!output.getClass().isArray());
        if ((input == null) || (input.getClass().isPrimitive()) || input.getClass().isArray()) return;

        MethodAccess inputMethodAccess = methodMap.get(input.getClass());
        if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

        List<String> fieldList = fieldMap.get(input.getClass());
        if (fieldList != null) {
            for (String field : fieldList) {
                String getKey = input.getClass().getName() + DOT + GET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                if (getIndex != null){
                    Object val = inputMethodAccess.invoke(input, getIndex);
                    if (val != null) {
                        if (!isClean || !val.getClass().isAssignableFrom(String.class) || !StringUtils.isEmpty(val.toString()))
                            output.put(field,val);
                    }
                }
            }
        }
    }
    public static void copyProperties(final Object input, Map<String,Object> output) {
        copyProperties(input,output,false);
    }
    public static void copyCleanProperties(final Object input, Map<String,Object> output) {
        copyProperties(input,output,true);
    }

    public static <K,V> void copyProperties(final Map<K,V> input, Object output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive()) && (!output.getClass().isArray());
        if ((input == null) || (input.getClass().isPrimitive()) || input.getClass().isArray()) return;

        MethodAccess outputMethodAccess = methodMap.get(output.getClass());
        if (outputMethodAccess == null) outputMethodAccess = cache(output.getClass());

        List<String> fieldList = fieldMap.get(output.getClass());
        if (fieldList != null) {
            assert (outputMethodAccess != null);
            Class[][] outputParameterTypes = outputMethodAccess.getParameterTypes();
            for (K k : input.keySet()) {
                Object val = input.get(k);
                if (val != null) {
                    String setKey = output.getClass().getName() + DOT + SET + StringUtils.capitalize(k.toString());
                    Integer setIndex = methodIndexMap.get(setKey);
                    assert (outputParameterTypes != null);
                    Class<?> outputFieldClass = outputParameterTypes[setIndex][0];
                    Class<?> inputFieldClass = val.getClass();
                    // 参数意义:1-需要反射的对象,2-对应方法的index,3-对象集合
                    if (outputFieldClass.isAssignableFrom(inputFieldClass)) {
                        if (!isClean || !outputFieldClass.isAssignableFrom(String.class)){
                            outputMethodAccess.invoke(output,setIndex,val);
                        } else if (!StringUtils.isEmpty(val.toString())) {
                            outputMethodAccess.invoke(output,setIndex,val.toString());
                        }
                    } else if (outputFieldClass == boolean.class) 
                        outputMethodAccess.invoke(output,setIndex,parseBoolean(val));
                    else if (outputFieldClass == char.class) 
                        outputMethodAccess.invoke(output,setIndex,parseChar(val));
                    else if (outputFieldClass == byte.class) 
                        outputMethodAccess.invoke(output,setIndex,parseByte(val));
                    else if (outputFieldClass == short.class) 
                        outputMethodAccess.invoke(output,setIndex,parseShort(val));
                    else if (outputFieldClass == int.class) 
                        outputMethodAccess.invoke(output,setIndex,parseInt(val));
                    else if (outputFieldClass == long.class) 
                        outputMethodAccess.invoke(output,setIndex,parseLong(val));
                    else if (outputFieldClass == float.class) 
                        outputMethodAccess.invoke(output,setIndex,parseFloat(val));
                    else if (outputFieldClass == double.class) 
                        outputMethodAccess.invoke(output,setIndex,parseDouble(val));
                    else if (isClean && outputFieldClass.isAssignableFrom(String.class)) {
                        Object tmp = createFrom(val, outputFieldClass, true);
                        if ((tmp != null) && (!StringUtils.isEmpty(tmp.toString()))){
                            outputMethodAccess.invoke(output,setIndex,tmp.toString());
                        }
                    } else {
                        outputMethodAccess.invoke(output,setIndex,
                                createFrom(val,outputFieldClass,isClean));
                    }
                }
            }
        }

    }
    public static void copyProperties(final Map<String,Object> input, Object output) {
        copyProperties(input,output,false);
    }
    public static void copyCleanProperties(final Map<String,Object> input, Object output) {
        copyProperties(input,output,true);
    }

    public static void copyProperties(final Object input, Object output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive()) && (!output.getClass().isArray());
        if ((input == null) || (input.getClass().isPrimitive()) || input.getClass().isArray()) return;

        MethodAccess outputMethodAccess = methodMap.get(output.getClass());
        if (outputMethodAccess == null) outputMethodAccess = cache(output.getClass());
        MethodAccess inputMethodAccess = methodMap.get(input.getClass());
        if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

        List<String> fieldList = fieldMap.get(input.getClass());
        if (fieldList != null) {
            assert (outputMethodAccess != null);
            Class[] inputReturnTypes = inputMethodAccess.getReturnTypes();
            Class[][] outputParameterTypes = outputMethodAccess.getParameterTypes();
            for (String field : fieldList) {
                String getKey = input.getClass().getName() + DOT + GET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                if (getIndex != null) {
                    String setKey = output.getClass().getName() + DOT + SET + field;
                    Integer setIndex = methodIndexMap.get(setKey);
                    if (setIndex != null) {
                        //获取参数类型
                        assert (inputReturnTypes != null);
                        Class<?> inputFieldClass = inputReturnTypes[getIndex];
                        assert (outputParameterTypes != null);
                        Class<?> outputFieldClass = outputParameterTypes[setIndex][0];
                        // 参数意义:1-需要反射的对象,2-对应方法的index,3-对象集合
                        if (outputFieldClass.isAssignableFrom(inputFieldClass)) {
                            if ((isClean) && !(outputFieldClass.isPrimitive())) {
                                Object tmp = inputMethodAccess.invoke(input, getIndex);
                                outputMethodAccess.invoke(output, setIndex, cleanProperties(tmp));
                            } else {
                                outputMethodAccess.invoke(output, setIndex, inputMethodAccess.invoke(input, getIndex));
                            }
                        } else if (outputFieldClass == boolean.class)
                            outputMethodAccess.invoke(output,setIndex,parseBoolean(inputMethodAccess.invoke(input,getIndex)));
                        else if (outputFieldClass == char.class)
                            outputMethodAccess.invoke(output,setIndex,parseChar(inputMethodAccess.invoke(input,getIndex)));
                        else if (outputFieldClass == byte.class)
                            outputMethodAccess.invoke(output,setIndex,parseByte(inputMethodAccess.invoke(input, getIndex)));
                        else if (outputFieldClass == short.class)
                            outputMethodAccess.invoke(output,setIndex,parseShort(inputMethodAccess.invoke(input, getIndex)));
                        else if (outputFieldClass == int.class)
                            outputMethodAccess.invoke(output,setIndex,parseInt(inputMethodAccess.invoke(input, getIndex)));
                        else if (outputFieldClass == long.class)
                            outputMethodAccess.invoke(output,setIndex,parseLong(inputMethodAccess.invoke(input, getIndex)));
                        else if (outputFieldClass == float.class)
                            outputMethodAccess.invoke(output,setIndex,parseFloat(inputMethodAccess.invoke(input, getIndex)));
                        else if (outputFieldClass == double.class)
                            outputMethodAccess.invoke(output,setIndex,parseDouble(inputMethodAccess.invoke(input, getIndex)));
                        else {
                            Object tmp = createFrom(inputMethodAccess.invoke(input, getIndex),outputFieldClass,isClean);
                            if (outputFieldClass.isPrimitive() && (tmp == null)) tmp = 0;
                            outputMethodAccess.invoke(output,setIndex,tmp);
                        }
                    }
                }
            }
        }
    }
    public static void copyProperties(final Object input, Object output) {
        copyProperties(input,output,false);
    }
    public static void copyCleanProperties(final Object input, Object output){
        copyProperties(input,output,true);
    }

    public static <D> D createFrom(Object input,Class<? extends D> outputClass,boolean isClean){
        assert ((outputClass != null) && (!outputClass.isPrimitive()));
        if (input == null) {
            return null;
        } else if (outputClass.isInstance(input)) {
            D output = outputClass.cast(input);
            return (isClean) ? cleanProperties(output) : output;
        } else if (outputClass.isArray()) {
            Class<?> elementClass = outputClass.getComponentType();
            if (input.getClass().isArray() && elementClass.isAssignableFrom(input.getClass().getComponentType())) {
                return outputClass.cast(input);
            } else {
                List<?> outputList = createListFrom(input,outputClass.getComponentType());
                return outputClass.cast(outputList.toArray());
            }
        } else {
            return constructFrom(input,outputClass, isClean);
        }
    }
    public static <D> D createFrom(Object input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,false);
    }
    public static <D> D createCleanFrom(Object input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,true);
    }

    public static <K,V,D> D createFrom(Map<K,V> input, Class<? extends D> outputClass,boolean isClean){
        D output = constructNull(outputClass);
        copyProperties(input,output,isClean);
        return output;
    }
    public static <K,V,D> D createFrom(Map<K,V> input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,false);
    }
    public static <K,V,D> D createCleanFrom(Map<K,V> input, Class<? extends D> outputClass){
        return createFrom(input,outputClass,true);
    }

    public static <D> List<D> createListFrom(Object input, Class<? extends D> elementClass){
        assert (elementClass != null) && (!elementClass.isPrimitive());
        List<D> outputList = null;
        if (input != null) {
            outputList = new ArrayList<>();
            if (elementClass.isInstance(input)) {
                outputList.add(elementClass.cast(input));
            } else if (input.getClass().isArray()) {
                for (int i=0; i<Array.getLength(input); i++){
                    outputList.add(createFrom(Array.get(input,i),elementClass));
                }
            } else {
                outputList.add(createFrom(input,elementClass));
            }
        }
        return outputList;
    }

    public static <D> D constructFrom(Object input,Class<? extends D> outputClass, boolean isClean){
        assert (outputClass != null) && (!outputClass.isPrimitive()) && (!outputClass.isArray());
        if (input == null) {
            return (isClean) ? null : constructNull(outputClass);
        }

        //使用以input、string为参数构造
        D output = null;

        Constructor<?>[] constructors = outputClass.getConstructors();
        Constructor<?> stringConstructor = null;
        Class<?>[] classes = input.getClass().getClasses();
        Boolean found = false;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1) {
                Class<?> ptype = (c.getParameterTypes())[0];
                if (!found) {
                    for (Class<?> ic : classes) {
                        if (ptype == ic) {
                            try {
                                output = outputClass.cast(c.newInstance(input));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                log.warn(outputClass.getName() + "没有以" + input.getClass().getName() + "为参数的构造方法");
                                output = null;
                            }
                            found = true;
                            break;
                        }
                    }
                }
                if ((stringConstructor == null) && (ptype == String.class)) stringConstructor = c;
                if ((found) && (stringConstructor != null)) break;
            }
        }

        //如果outputClass有以String为参数的构造函数
        if ((output == null) && (stringConstructor != null)){
            try {
                output = outputClass.cast(stringConstructor.newInstance(input.toString()));
            } catch ( InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.warn(outputClass.getName() + "没有以" + String.class.getName() + "为参数的构造方法");
                output = null;
            }
        }

        if (output == null){
            output = constructNull(outputClass);
            if (isClean) output = cleanProperties(output);
            copyProperties(input,output,isClean);
        }

        return output;
    }
    public static <D> D constructFrom(Object input,Class<? extends D> outputClass){
        return constructFrom(input,outputClass,false);
    }
    public static <D> D cleanConstructFrom(Object input,Class<? extends D> outputClass){
        return constructFrom(input,outputClass,true);
    }

    public static <D> D constructNull(Class<? extends D> outputClass){
        assert (outputClass != null) && (!outputClass.isPrimitive()) && (!outputClass.isArray());
        try {
            return outputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn("无法创建" + outputClass.getName() + "对象");
        }
        return null;
    }

    /**
     * 清理Bean内为空字符串的属性，将其替换为null
     */
    public static <T> T cleanProperties(T obj){
        if (obj == null) return null;
        if (obj.getClass().isPrimitive()) return obj;
        if (obj.getClass().isArray()){
            for (int i=0; i<Array.getLength(obj); i++){
                cleanProperties(Array.get(obj,i));
            }
        }
        if (isEmpty(obj)) return null;

        MethodAccess objMethodAccess = methodMap.get(obj.getClass());
        if (objMethodAccess == null) objMethodAccess = cache(obj.getClass());

        List<String> fieldList = fieldMap.get(obj.getClass());
        if (fieldList != null) {
            assert (objMethodAccess != null);
            Class[][] objParameterTypes = objMethodAccess.getParameterTypes();
            for (String field : fieldList) {
                String getKey = obj.getClass().getName() + DOT + GET + field;
                String setKey = obj.getClass().getName() + DOT + SET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                Integer setIndex = methodIndexMap.get(setKey);
                if ((setIndex != null) && (getIndex != null) && (isEmpty(objMethodAccess.invoke(obj,getIndex)))) {
                    Class<?> fieldClass = objParameterTypes[setIndex][0];
                    if (!fieldClass.isPrimitive())
                        objMethodAccess.invoke(obj,setIndex,fieldClass.cast(null));
                }
            }
        }
        return obj;
    }

    public static <K,V> Map<K,V> cleanProperties(Map<K,V> obj){
        for (K k : obj.keySet()) {
            V v = obj.get(k);
            if (isEmpty(v)) obj.remove(k);
        }
        return obj;
    }

    public static <T> boolean isEmpty(T obj) {
        return (obj == null)
                || ((obj.getClass().isAssignableFrom(String.class)) && (StringUtils.isEmpty(String.class.cast(obj))))
                || ((DigitUtils.isDigitalClass(obj.getClass())) && (DigitUtils.isSame(obj,0)));
    }

    /**
     * 写入Bean属性
     */
    public static void putProperty(Object obj, final String ptyName, final Object value){
        assert (obj != null) && (!obj.getClass().isPrimitive()) && (!obj.getClass().isArray());
        assert (!StringUtils.isEmpty(ptyName));

        MethodAccess objMethodAccess = methodMap.get(obj.getClass());
        if (objMethodAccess == null) objMethodAccess = cache(obj.getClass());

        List<String> fieldList = fieldMap.get(obj.getClass());
        if (fieldList != null) {
            assert (objMethodAccess != null);
            String setKey = obj.getClass().getName() + DOT + SET + StringUtils.capitalize(ptyName);
            Integer setIndex = methodIndexMap.get(setKey);
            if (setIndex != null) {
                //获取参数类型
                Class[][] objParameterTypes = objMethodAccess.getParameterTypes();
                assert (objParameterTypes != null);
                Class<?> fieldClass = objParameterTypes[setIndex][0];
                if (fieldClass == boolean.class)
                    objMethodAccess.invoke(obj,setIndex,parseBoolean(value));
                else if (fieldClass == char.class)
                    objMethodAccess.invoke(obj,setIndex,parseChar(value));
                else if (fieldClass == byte.class)
                    objMethodAccess.invoke(obj,setIndex,parseByte(value));
                else if (fieldClass == short.class)
                    objMethodAccess.invoke(obj,setIndex,parseShort(value));
                else if (fieldClass == int.class)
                    objMethodAccess.invoke(obj,setIndex,parseInt(value));
                else if (fieldClass == long.class)
                    objMethodAccess.invoke(obj,setIndex,parseLong(value));
                else if (fieldClass == float.class)
                    objMethodAccess.invoke(obj,setIndex,parseFloat(value));
                else if (fieldClass == double.class)
                    objMethodAccess.invoke(obj,setIndex,parseDouble(value));
                else
                    objMethodAccess.invoke(obj,setIndex,createFrom(value,fieldClass));
            }
        }
    }

    /**
     * 读取Bean属性
     */
    public static Object getProperty(final Object obj, final String ptyName) {
        assert (obj != null) && (!obj.getClass().isPrimitive()) && (!obj.getClass().isArray());
        assert (!StringUtils.isEmpty(ptyName));

        MethodAccess objMethodAccess = methodMap.get(obj.getClass());
        if (objMethodAccess == null) objMethodAccess = cache(obj.getClass());

        List<String> fieldList = fieldMap.get(obj.getClass());
        if (fieldList != null) {
            assert (objMethodAccess != null);
            String getKey = obj.getClass().getName() + DOT + GET + StringUtils.capitalize(ptyName);
            Integer getIndex = methodIndexMap.get(getKey);
            if (getIndex != null) {
                //获取参数类型
                return objMethodAccess.invoke(obj,getIndex);
            }
        }
        return null;
    }

    /** 获得数组对象的长度 */
    @Deprecated
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
        else
            return !(value instanceof Float)
                    && !(value instanceof Double) || (!DigitUtils.isSame(value, 0));
    }

    public static <T> boolean parseBoolean(final T[] value, final int n){
        assert (value != null);
        assert (n < value.length);
        return parseBoolean(value[n]);
    }

    @Deprecated
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
