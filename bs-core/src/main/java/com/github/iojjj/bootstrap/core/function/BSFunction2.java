package com.github.iojjj.bootstrap.core.function;

/**
 * Represents a function that accepts two arguments and produces a result.
 * <p>This is a functional interface whose functional method is {@link #apply(Object, Object)}.</p>
 *
 * @param <T1> the type of the input to the function
 * @param <T2> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 1.0
 */
public interface BSFunction2<T1, T2, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t1 the function argument
     * @param t2 the function argument
     * @return the function result
     */
    R apply(T1 t1, T2 t2);
}
