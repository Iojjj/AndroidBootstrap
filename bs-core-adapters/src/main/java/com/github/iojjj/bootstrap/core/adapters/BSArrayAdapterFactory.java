package com.github.iojjj.bootstrap.core.adapters;

/**
 * Factory class that creates new builders.
 *
 * @since 1.0
 */
public class BSArrayAdapterFactory {

    private BSArrayAdapterFactory() {
        //no instance
    }

    /**
     * Create a new builder of {@link BSArrayAdapter}.
     *
     * @param <T> type of items
     *
     * @return new builder
     */
    public static <T> BSArrayAdapterBuilder<T> newAdapter() {
        return new BSArrayAdapterBuilder<>();
    }
}
