package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;
import iojjj.android.bootstrap.database.interfaces.IEntity;

/**
 * Helper class for models implemented {@link IEntity} interface.
 * @see #save()
 * @see #delete()
 */
public final class EntityHelper {

    private final DbContext dbContext;

    /**
     * Create new instance
     * @return new instance
     */
    static EntityHelper newInstance(@NonNull DbContext dbContext) {
        return new EntityHelper(dbContext);
    }

    private EntityHelper(@NonNull DbContext dbContext) {
        AssertionUtils.assertNotNull(dbContext, "Database context");
        this.dbContext = dbContext;
    }

    public final void save() {
        if (dbContext.isClosed())
            throw new IllegalStateException("Database is closed");
        // todo: save
    }

    public final void delete() {
        if (dbContext.isClosed())
            throw new IllegalStateException("Database is closed");
        // todo: delete
    }
}
