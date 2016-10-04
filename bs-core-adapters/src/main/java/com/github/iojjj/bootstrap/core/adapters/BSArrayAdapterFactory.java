package com.github.iojjj.bootstrap.core.adapters;

public class BSArrayAdapterFactory {


    public <T> BSArrayAdapterBuilder<T> newAdapter() {
        return new BSArrayAdapterBuilder<>();
    }
}
