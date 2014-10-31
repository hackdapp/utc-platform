package com.cmk.utc.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * instance classobject
 * 
 * @author LIFE2014
 * @version 2014-5-16
 * @see ClassUtil
 * @since
 */
public class ClassUtil {
    /**
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see
     */
    public static Object createObject(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createObject(className, null);
    }

    /**
     * @param className
     * @return
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see
     */
    public static <T> T createObject(Class<T> className) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createObject(className, null);
    }

    /**
     * @param className
     * @param param
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see
     */
    public static Object createObject(String className, Object... param) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createObject(createClass(className), param);
    }

    /**
     * @param className
     * @param classLoader
     * @param param
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see
     */
    public static Object createObject(String className, ClassLoader classLoader, Object... param) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createObject(createClass(className, classLoader), param);
    }

    /**
     * @param className
     * @param param
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @see
     */
    public static <T> T createObject(Class<T> className, Object... param) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (className != null) {
            if (param != null && param.length > 0) {
                for (Constructor constructors : className.getConstructors()) {
                    if (constructors.getParameterTypes().length == param.length) {
                        return (T)constructors.newInstance(param);
                    }
                }
            } else {
                return (T)className.newInstance();
            }
        }
        return null;
    }

    /**
     * @param className
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     * @see
     */
    public static Class createClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e1) {
            return createClass(className);
        }
    }

    /**
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @see
     */
    public static Class createClass(String className) throws ClassNotFoundException {
        try {
            ClassLoader classLoader = ClassUtil.class.getClassLoader();
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e1) {
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("class does not exist", e);
            }
        }
    }
}
