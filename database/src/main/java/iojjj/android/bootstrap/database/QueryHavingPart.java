package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQueryHavingPart;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;
import iojjj.android.bootstrap.database.interfaces.IQueryOrderPart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class QueryHavingPart<T extends IEntity> extends QueryPart<T> implements IQueryHavingPart<T> {

    QueryHavingPart(@NonNull Query<T> parent) {
        super(parent);
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause) {
        setClause("HAVING " + havingClause);
        return this;
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause, String... params) {
        setClause("HAVING " + havingClause, params);
        return this;
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause) {
        return getParent().orderBy(orderClause);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause, String... params) {
        return getParent().orderBy(orderClause, params);
    }

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        return getParent().limit(offset, limit);
    }
}
