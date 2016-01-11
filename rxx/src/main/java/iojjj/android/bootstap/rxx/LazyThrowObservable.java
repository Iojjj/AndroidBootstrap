package iojjj.android.bootstap.rxx;

import rx.Observable;
import rx.Observer;
import rx.functions.Func0;

/**
 * An Observable that invokes {@link Observer#onError onError} when the {@link Observer} subscribes to it.
 *
 * @param <T>
 *            the type of item (ostensibly) emitted by the Observable
 */
class LazyThrowObservable<T> extends Observable<T> {
    public LazyThrowObservable(final Func0<Throwable> exception) {
        super(observer -> {
            try {
                observer.onError(exception.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
