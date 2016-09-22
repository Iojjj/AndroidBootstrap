package com.github.iojjj.bootstrap.mvp;

// TODO: 21.09.2016 documentation
interface LoaderDelegate<P extends BSMvpPresenter> {

    boolean takeContentChanged();

    void forceLoad();

    void deliverResult(P presenter);

    void cancelLoadImpl();
}
