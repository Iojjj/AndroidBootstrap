package com.github.iojjj.bootstrap.mvp;

// TODO: 21.09.2016 documentation
interface LoaderDelegate<TPresenter extends BSMvpPresenter> {


    boolean takeContentChanged();

    void forceLoad();

    void deliverResult(TPresenter presenter);

    void cancelLoadImpl();
}
