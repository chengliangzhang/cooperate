package com.maoding.CoreUtils;

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

    public static <K,V> Map<K,V> createMapFrom(final Object input,Class<? extends K> keyClass,Class<? extends V> valClass){
        assert (keyClass != null) && (!keyClass.isPrimitive()) && (!keyClass.isArray());
        assert (valClass != null) && (!valClass.isPrimitive()) && (!valClass.isArray());
        if (input == null) return null;

        MethodAccess inputMethodAccess = methodMap.get(input.getClass());
        if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

        Map<K,V> output = new HashMap<>();

        List<String> fieldList = fieldMap.get(input.getClass());
        if (fieldList != null) {
            for (String field : fieldList) {
                K key = createFrom(field,keyClass,false);
                String getKey = input.getClass().getName() + DOT + GET + field;
                Integer getIndex = methodIndexMap.get(getKey);
                if (getIndex != null){
                    V val = createFrom(inputMethodAccess.invoke(input, getIndex),valClass,false);
                    if (val != null) {
                        output.put(key,val);
                    }
                }
            }
        }
        return output;
    }
    public static Map<String,Object> createMapFrom(final Object input){
        return createMapFrom(input,String.class,Object.class);
    }

    @Deprecated
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
    @Deprecated
    public static void copyProperties(final Object input, Map<String,Object> output) {
        copyProperties(input,output,false);
    }
    @Deprecated
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
                    callSetMethod(outputMethodAccess,output,setIndex,outputFieldClass,val,isClean);
                }
            }
        }
    }

    public static void copyProperties(final Object input, Object[] output, boolean isClean) {
        if (output.getClass().isArray()){
            int n = Array.getLength(output);
            if (input.getClass().isArray()){
                int m = Array.getLength(input);
                if (n > m) n = m;
                for (int i=0; i<n; i++){
                    copyProperties(Array.get(input,i),Array.get(output,i));
                }
            } else if (input instanceof List){
                List<?> inputList = (List<?>) input;
                int i = 0;
                for (Object inputElement : inputList){
                    if (inputElement == null) continue;
                    copyProperties(inputElement,Array.get(output,i));
                    if (++i >= n) break;
                }
            } else {
                copyProperties(input,Array.get(output,0));
            }
        }
    }

    public static void copyProperties(final Object input, Object output, boolean isClean) {
        assert (output != null) && (!output.getClass().isPrimitive());
        if (input == null) return;

        if (output.getClass().isArray()) {
            copyProperties(input,(Object[])output,isClean);
        } else if (input instanceof Map) {
            copyProperties((Map<?, ?>) input, output, isClean);
        } else if (input.getClass().isArray()) {
            copyProperties(Array.get(input,0),output,isClean);
        } else {
            MethodAccess outputMethodAccess = methodMap.get(output.getClass());
            if (outputMethodAccess == null) outputMethodAccess = cache(output.getClass());
            MethodAccess inputMethodAccess = methodMap.get(input.getClass());
            if (inputMethodAccess == null) inputMethodAccess = cache(input.getClass());

            List<String> fieldList = fieldMap.get(input.getClass());
            if (fieldList != null) {
                assert (outputMethodAccess != null);
                Class[][] outputParameterTypes = outputMethodAccess.getParameterTypes();
                for (String field : fieldList) {
                    String getKey = input.getClass().getName() + DOT + GET + field;
                    Integer getIndex = methodIndexMap.get(getKey);
                    if (getIndex != null) {
                        String setKey = output.getClass().getName() + DOT + SET + field;
                        Integer setIndex = methodIndexMap.get(setKey);
                        if (setIndex != null) {
                            //获取设置属性方法的参数类型
                            assert (outputParameterTypes != null);
                            Class<?> outputFieldClass = outputParameterTypes[setIndex][0];
                            //设置属性
                            callSetMethod(outputMethodAccess, output, setIndex, outputFieldClass, inputMethodAccess.invoke(input, getIndex), isClean);
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

    private static void callSetMethod(MethodAccess outputMethodAccess,Object output,Integer setIndex,Class<?> outputFieldClass,Object inputValue,boolean isClean){
        //如果输入为空，不做处理
        if (inputValue != null) {
            if (outputFieldClass.isAssignableFrom(inputValue.getClass())) {
                if ((!isClean) || ObjectUtils.isNotEmpty(inputValue)) { //如果isClean为真，仅设置非空值
                    outputMethodAccess.invoke(output, setIndex, inputValue);
                }
            } else if (outputFieldClass == boolean.class) {
                outputMethodAccess.invoke(output, setIndex, DigitUtils.parseBoolean(inputValue));
            } else if (outputFieldClass == char.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseChar(inputValue));
            } else if (outputFieldClass == byte.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseByte(inputValue));
            } else if (outputFieldClass == short.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseShort(inputValue));
            } else if (outputFieldClass == int.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseInt(inputValue));
            } else if (outputFieldClass == long.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseLong(inputValue));
            } else if (outputFieldClass == float.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseFloat(inputValue));
            } else if (outputFieldClass == double.class) {
                outputMethodAccess.invoke(output,setIndex,DigitUtils.parseDouble(inputValue));
            } else {
                outputMethodAccess.invoke(output,setIndex,createFrom(inputValue,outputFieldClass,isClean));
            }
        }
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
                return outputClass.cast(createArrayObjectFrom(input, outputClass.getComponentType()));
            }
        } else if (input instanceof Map) {
            D output = constructNull(outputClass);
            copyProperties(input, output, isClean);
            return output;
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

    public static <D> List<D> createListFrom(Object input, Class<? extends D> elementClass){
        assert (elementClass != null) && (!elementClass.isPrimitive());
        List<D> outputList = null;
        if (input != null) {
            outputList = new ArrayList<>();
            if (input.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(input); i++) {
                    outputList.add(createFrom(Array.get(input, i), elementClass));
                }
            } else if (input instanceof List) {
                List<?> inputList = (List<?>) input;
                for (Object inputElement : inputList) {
                    if (inputElement == null) continue;
                    if (elementClass.isAssignableFrom(inputElement.getClass())) {
                        outputList.add(elementClass.cast(inputElement));
                    } else {
                        outputList.add(createFrom(inputElement, elementClass));
                    }
                }
            } else {
                if (elementClass.isInstance(input)) {
                    outputList.add(elementClass.cast(input));
                } else {
                    outputList.add(createFrom(input, elementClass));
                }
            }
        }
        return outputList;
    }

    @SuppressWarnings("unchecked")
    public static <D> D[] createArrayFrom(Object input, Class<? extends D> elementClass){
        assert (elementClass != null) && (!elementClass.isPrimitive());
        D[] outputArray = null;
        if (input != null){
            if (input.getClass().isArray()) {
                outputArray = (D[])Array.newInstance(elementClass,Array.getLength(input));
                for (int i=0; i<Array.getLength(input); i++){
                    outputArray[i] = createFrom(Array.get(input,i),elementClass);
                }
            } else {
                outputArray = (D[])Array.newInstance(elementClass,1);
                if (elementClass.isInstance(input)) {
                    outputArray[0] = elementClass.cast(input);
                } else {
                    outputArray[0] = createFrom(input,elementClass);
                }
            }
        }
        return outputArray;
    }

    public static Object createArrayObjectFrom(Object input,Class<?> elementClass){
        assert (elementClass != null);
        if (elementClass == boolean.class) return createBooleanArrayFrom(input);
        else if (elementClass == char.class) return createCharArrayFrom(input);
        else if (elementClass == byte.class) return createByteArrayFrom(input);
        else if (elementClass == short.class) return createShortArrayFrom(input);
        else if (elementClass == int.class) return createIntArrayFrom(input);
        else if (elementClass == long.class) return createLongArrayFrom(input);
        else if (elementClass == float.class) return createFloatArrayFrom(input);
        else if (elementClass == double.class) return createDoubleArrayFrom(input);
        else return createArrayFrom(input,elementClass);
    }

    public static boolean[] createBooleanArrayFrom(Object input){
        Boolean[] tmp = createArrayFrom(input,Boolean.class);
        boolean[] output = null;
        if (tmp != null){
            output = new boolean[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : false;
            }
        }
        return output;
    }

    public static char[] createCharArrayFrom(Object input){
        Character[] tmp = createArrayFrom(input,Character.class);
        char[] output = null;
        if (tmp != null){
            output = new char[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (char)0;
            }
        }
        return output;
    }

    public static byte[] createByteArrayFrom(Object input){
        Byte[] tmp = createArrayFrom(input,Byte.class);
        byte[] output = null;
        if (tmp != null){
            output = new byte[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (byte)0;
            }
        }
        return output;
    }

    public static short[] createShortArrayFrom(Object input){
        Short[] tmp = createArrayFrom(input,Short.class);
        short[] output = null;
        if (tmp != null){
            output = new short[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (short)0;
            }
        }
        return output;
    }

    public static int[] createIntArrayFrom(Object input){
        Integer[] tmp = createArrayFrom(input,Integer.class);
        int[] output = null;
        if (tmp != null){
            output = new int[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (int)0;
            }
        }
        return output;
    }

    public static long[] createLongArrayFrom(Object input){
        Long[] tmp = createArrayFrom(input,Long.class);
        long[] output = null;
        if (tmp != null){
            output = new long[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (long)0;
            }
        }
        return output;
    }

    public static float[] createFloatArrayFrom(Object input){
        Float[] tmp = createArrayFrom(input,Float.class);
        float[] output = null;
        if (tmp != null){
            output = new float[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (float)0;
            }
        }
        return output;
    }

    public static double[] createDoubleArrayFrom(Object input){
        Double[] tmp = createArrayFrom(input,Double.class);
        double[] output = null;
        if (tmp != null){
            output = new double[tmp.length];
            for (int i=0; i<tmp.length; i++){
                output[i] = (tmp[i] != null) ? tmp[i] : (double)0;
            }
        }
        return output;
    }

    public static <D> D constructFrom(Object input,Class<? extends D> outputClass, boolean isClean){
        assert (outputClass != null) && (!outputClass.isPrimitive()) && (!outputClass.isArray());
        if (input == null) {
            return (isClean) ? null : constructNull(outputClass);
        }

        //使用以input、string为参数构造
        D output = null;

        //如果有以input类型为参数的构造函数，直接调用
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

        //如果outputClass是基本数字类型，使用基本数字类型调用构造函数
        if ((output == null) && (DigitUtils.isDigitalClass(outputClass))) {
            if (outputClass.isAssignableFrom(Boolean.class)) {
                output = outputClass.cast(DigitUtils.parseBoolean(input));
            } else if (outputClass.isAssignableFrom(Character.class)) {
                output = outputClass.cast(DigitUtils.parseChar(input));
            } else if (outputClass.isAssignableFrom(Byte.class)) {
                output = outputClass.cast(DigitUtils.parseByte(input));
            } else if (outputClass.isAssignableFrom(Short.class)) {
                output = outputClass.cast(DigitUtils.parseShort(input));
            } else if (outputClass.isAssignableFrom(Integer.class)) {
                output = outputClass.cast(DigitUtils.parseInt(input));
            } else if (outputClass.isAssignableFrom(Long.class)) {
                output = outputClass.cast(DigitUtils.parseLong(input));
            } else if (outputClass.isAssignableFrom(Float.class)) {
                output = outputClass.cast(DigitUtils.parseFloat(input));
            } else if (outputClass.isAssignableFrom(Double.class)) {
                output = outputClass.cast(DigitUtils.parseDouble(input));
            } else {
                output = outputClass.cast(0);
            }
        }

        //如果outputClass有以String为参数的构造函数，使用字符串调用构造函数
        if ((output == null) && (stringConstructor != null)){
            try {
                output = outputClass.cast(stringConstructor.newInstance(input.toString()));
            } catch ( InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.warn(outputClass.getName() + "不能以" + input.toString() + "为参数调用构造方法");
                output = null;
            }
        }

        //其他情况，先构造一个空对象，再复制属性
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
        if (obj == null) {
            return null;
        } else if (obj.getClass().isPrimitive()) {
            return obj;
        } else if (obj.getClass().isArray()){
            return (Array.getLength(obj) > 0) ? obj : null;
        } else if (ObjectUtils.isEmpty(obj)) {
            return null;
        } else if (DigitUtils.isDigitalClass(obj.getClass())) {
            return obj;
        }

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
                if ((setIndex != null) && (getIndex != null) && (ObjectUtils.isEmpty(objMethodAccess.invoke(obj,getIndex)))) {
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
            if (ObjectUtils.isEmpty(v)){
                obj.remove(k);
            }
        }
        return obj;
    }
}
