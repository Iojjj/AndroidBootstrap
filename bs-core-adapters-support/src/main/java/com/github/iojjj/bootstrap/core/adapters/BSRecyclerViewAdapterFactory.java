package com.github.iojjj.bootstrap.core.adapters;

/**
 * Factory class that creates new builders.
 *
 * @since 1.0
 */
public class BSRecyclerViewAdapterFactory {

    private BSRecyclerViewAdapterFactory() {
        //no instance
    }

    /**
     * Create a new builder of {@link BSRecyclerViewAdapter}.
     *
     * @param <T> type of items
     *
     * @return new builder
     */
    public <T> BSRecyclerViewAdapterBuilder<T> newAdapter() {
        return new BSRecyclerViewAdapterBuilder<>();
    }

}
