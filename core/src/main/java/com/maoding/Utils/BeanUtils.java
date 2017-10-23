package com.maoding.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 类描述：类操作定义，可用于复制类属性
 * Created by Chengliang.zhang on 2017/6/26.
 */
public final class BeanUtils extends org.springframework.beans.BeanUtils{
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 复制Map内属性到Bean
     */
    public static void copyProperties(final Map<String, Object> input, final Object output) {
        if (input == null || output == null) return;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(output.getClass(), Object.class);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (String key : input.keySet()) {
                Object value = input.get(key);
                for (PropertyDescriptor pty : properties) {
                    if (pty.getName().equals(key) && pty.getPropertyType().equals(value.getClass())) {
                        pty.getWriteMethod().invoke(output, value);
                    }
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 复制Bean属性到Map
     */
    public static void copyProperties(final Object input, final Map<String, Object> output) {
        if (input == null || output == null) return;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor pty : properties) {
                Object val = pty.getReadMethod().invoke(input);
                if (val != null) {
                    output.put(pty.getName(),val);
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 复制Bean属性
     */
    public static void copyProperties(final Object input, final Object output) {
        if (input == null || output == null) return;

        try {
            BeanInfo sourceBeanInfo = Introspector.getBeanInfo(input.getClass(), Object.class);
            PropertyDescriptor[] sourceProperties = sourceBeanInfo.getPropertyDescriptors();
            BeanInfo destBeanInfo = Introspector.getBeanInfo(output.getClass(), Object.class);
            PropertyDescriptor[] destProperties = destBeanInfo.getPropertyDescriptors();

            for (PropertyDescriptor sourceProperty : sourceProperties) {
                for (PropertyDescriptor destProperty : destProperties) {
                    if (sourceProperty.getName().equals(destProperty.getName()) &&
                            sourceProperty.getPropertyType().equals(destProperty.getPropertyType())){
                        Object value = sourceProperty.getReadMethod().invoke(input);
                        if (value != null) {
                            destProperty.getWriteMethod().invoke(output, value);
                        }
                        break;
                    }
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage());
            e.printStackTrace();
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
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
