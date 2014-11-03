package iojjj.androidbootstrap.appcompat.utils;

import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    public static class Invoker<T> {

        private final Method m;

        private Invoker(@NonNull Method m) {
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            this.m = m;
        }

        public Method getMethod() {
            return m;
        }

        @SuppressWarnings("unchecked")
        public <R> R invokeFor(T receiver, Object... args) {
            try {
                return (R) m.invoke(receiver, args);
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (InvocationTargetException e) {
                throw new Error(e);
            }
        }
    }

    public static <T> Invoker<T> invoker(@NonNull String methodName, @NonNull Class<T> clazz, Class<?>... args) {
        try {
            return new Invoker<T>(clazz.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    public static <T> Invoker<T> invokerWithException(@NonNull String methodName, @NonNull Class<T> clazz, Class<?>... args) throws NoSuchMethodException{
        return new Invoker<T>(clazz.getDeclaredMethod(methodName, args));
    }

    public static class Creator<T> {

        private final Constructor<T> c;

        private Creator(@NonNull Constructor<T> c) {
            if (!c.isAccessible()) {
                c.setAccessible(true);
            }
            this.c = c;
        }

        public Constructor<T> getConstructor() {
            return c;
        }

        @SuppressWarnings("unchecked")
        public T newInstanceFor(Object... args) {
            try {
                return c.newInstance(args);
            } catch (InstantiationException e) {
                throw new Error(e);
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (InvocationTargetException e) {
                throw new Error(e);
            }
        }
    }

    public static <T> Creator<T> creator(@NonNull Class<T> clazz, Class<?>... args) {
        try {
            return new Creator<T>(clazz.getDeclaredConstructor(args));
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }
}
