package com.github.iojjj.bootstrap.core.function;

/**
 * Represents a function that accepts one argument and produces a result.
 * <p>This is a functional interface whose functional method is {@link #apply(Object)}.</p>
 *
 * @param <T1> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 1.0
 */
public interface BSFunction1<T1, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t1 the function argument
     * @return the function result
     */
    R apply(T1 t1);
}
