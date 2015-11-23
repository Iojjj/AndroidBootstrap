package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class QueryLimitPart<T extends IEntity> extends QueryPart<T> implements IQueryLimitPart<T> {

    private int offset;

    QueryLimitPart(@NonNull Query<T> parent) {
        super(parent);
    }

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        setClause(String.format("LIMIT %d, %d", this.offset = offset, limit));
        return this;
    }

    IQueryLimitPart<T> limit(int limit) {
        setClause(String.format("LIMIT %d, %d", offset, limit));
        return this;
    }
}
