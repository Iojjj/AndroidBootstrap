package iojjj.android.bootstrap.database.interfaces;

import android.support.annotation.NonNull;

import java.util.List;

import iojjj.android.bootstrap.core.utils.misc.Optional;
import iojjj.android.bootstrap.database.OptionalCursor;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryMain<T extends IEntity> {

    @NonNull
    OptionalCursor<T> toCursor();

    @NonNull
    List<T> toList();

    /**
     * Get number of rows returned in query
     * @return number of rows
     */
    int count();

    /**
     * Get first row. This method ignores limit value set by {@link IQueryLimitPart#limit(int, int)}} method.
     * @return optional first row
     */
    Optional<T> first();
}
