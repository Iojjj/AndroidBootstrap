package com.github.iojjj.bootstrap.core.adapters;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

public class BSRecyclerViewAdapterBuilder<T> extends AdapterBuilder<T, BSRecyclerViewAdapterBuilder<T>> {

    BSRecyclerViewAdapterBuilder() {

    }

    /**
     * Create a new adapter.
     *
     * @return new adapter object
     */
    public BSRecyclerViewAdapter<T> build() {
        BSAssertions.assertNotNull(getRenderer(), "renderer");
        return new BSRecyclerViewAdapter<>(getRenderer(), getFilterPredicate());
    }

    @Override
    protected BSRecyclerViewAdapterBuilder<T> getThis() {
        return this;
    }
}
