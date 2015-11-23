package iojjj.android.bootstrap.database;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import iojjj.android.bootstrap.core.utils.misc.Optional;
import iojjj.android.bootstrap.database.interfaces.IEntity;

/**
 * Custom implementation of {@link iojjj.android.bootstrap.core.utils.misc.Optional} for cursor
 */
public class OptionalCursor<T extends IEntity> extends Optional<Cursor> {

    private final Query<T> query;

    OptionalCursor(@Nullable Cursor cursor, @NonNull Query<T> query) {
        super(cursor);
        this.query = query;
    }

    @NonNull
    public List<T> toList() {
        return query.toList(get());
    }
}
