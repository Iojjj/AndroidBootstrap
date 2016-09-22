package com.github.iojjj.bootstrap.core.functions;

/**
 * Represents a function that accepts zero arguments and produces a result.
 * <p>This is a functional interface whose functional method is {@link #apply()}.</p>
 *
 * @param <R> the type of the result of the function
 * @since 1.0
 */
public interface BSFunction0<R> {

    /**
     * Applies this function.
     *
     * @return the function result
     */
    R apply();
}
