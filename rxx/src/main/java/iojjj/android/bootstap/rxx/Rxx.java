package iojjj.android.bootstap.rxx;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.assertions.AssertionUtils;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Func0;

/**
 * Extensions for Rx library.
 */
public class Rxx {

    /**
     * Returns an Observable that invokes an {@link Observer}'s {@link Observer#onError onError} method when the
     * Observer subscribes to it.
     * <p>
     * <img width="640" height="190" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/error.png" alt="">
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>{@code error} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param function the function that returns particular Throwable to pass to {@link Observer#onError onError}
     * @param <T>      the type of the items (ostensibly) emitted by the Observable
     * @return an Observable that invokes the {@link Observer}'s {@link Observer#onError onError} method when
     * the Observer subscribes to it
     * @see <a href="http://reactivex.io/documentation/operators/empty-never-throw.html">ReactiveX operators documentation: Throw</a>
     */
    public static <T> Observable<T> error(@NonNull Func0<Throwable> function) {
        AssertionUtils.assertNotNull(function, "Callable");
        return new LazyThrowObservable<>(function);
    }
}
