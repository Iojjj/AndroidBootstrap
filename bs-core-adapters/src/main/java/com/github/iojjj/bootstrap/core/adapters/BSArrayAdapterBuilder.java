package com.github.iojjj.bootstrap.core.adapters;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

public class BSArrayAdapterBuilder<T> extends AdapterBuilder<T, BSArrayAdapterBuilder<T>> {

    BSArrayAdapterBuilder() {

    }

    /**
     * Create a new adapter.
     *
     * @return new adapter object
     */
    public BSArrayAdapter<T> build() {
        BSAssertions.assertNotNull(getRenderer(), "renderer");
        return new BSArrayAdapterImpl<>(getRenderer(), getFilterPredicate());
    }

    @Override
    protected BSArrayAdapterBuilder<T> getThis() {
        return this;
    }
}
