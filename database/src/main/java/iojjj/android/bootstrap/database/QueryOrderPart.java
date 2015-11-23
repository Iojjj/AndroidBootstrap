package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;
import iojjj.android.bootstrap.database.interfaces.IQueryOrderPart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class QueryOrderPart<T extends IEntity> extends QueryPart<T> implements IQueryOrderPart<T> {

    QueryOrderPart(@NonNull Query<T> parent) {
        super(parent);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause) {
        setClause("ORDER BY " + orderClause);
        return this;
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause, String... params) {
        setClause("ORDER BY " + orderClause, params);
        return this;
    }

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        return getParent().limit(offset, limit);
    }
}
