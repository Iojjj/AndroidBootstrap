package com.github.iojjj.bootstrap.core.adapters;

public class BSRecyclerViewAdapterFactory {


    public <T> BSRecyclerViewAdapterBuilder<T> newAdapter() {
        return new BSRecyclerViewAdapterBuilder<>();
    }

}
