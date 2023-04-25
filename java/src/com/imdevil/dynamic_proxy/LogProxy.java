package com.imdevil.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class LogProxy{

    public static <T> T getInstance(T target) {
        return getInstance(target, getDefaultInvocationHandler(target));
    }

    public static <T> T getInstance(T target, InvocationHandler handler) {
        Object proxyObject = Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
        return (T) proxyObject;
    }

    private static InvocationHandler getDefaultInvocationHandler(Object target) {
        return (proxy, method, args) -> {
            System.out.println(method.getName());

            Type[] parameterTypes = method.getGenericParameterTypes();
            for (Type parameterType : parameterTypes) {
                System.out.println(parameterType.getTypeName());
            }

            System.out.println(method.getGenericReturnType());

            System.out.println("+++++");
            Object result = method.invoke(target, args);
            System.out.println("-----");
            return result;
        };
    }
}
