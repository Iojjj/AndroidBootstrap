package com.github.iojjj.bootstrap.core.functions;

/**
 * Represents a function that accepts three arguments and produces a result.
 * <p>This is a functional interface whose functional method is {@link #apply(Object, Object, Object)}.</p>
 *
 * @param <T1> the type of the input to the function
 * @param <T2> the type of the input to the function
 * @param <T3> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 1.0
 */
public interface BSFunction3<T1, T2, T3, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t1 the function argument
     * @param t2 the function argument
     * @param t3 the function argument
     * @return the function result
     */
    R apply(T1 t1, T2 t2, T3 t3);
}
