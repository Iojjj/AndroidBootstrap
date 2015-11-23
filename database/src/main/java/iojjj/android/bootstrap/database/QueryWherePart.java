package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQueryGroupPart;
import iojjj.android.bootstrap.database.interfaces.IQueryHavingPart;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;
import iojjj.android.bootstrap.database.interfaces.IQueryOrderPart;
import iojjj.android.bootstrap.database.interfaces.IQueryWherePart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class QueryWherePart<T extends IEntity> extends QueryPart<T> implements IQueryWherePart<T> {

    QueryWherePart(@NonNull Query<T> parent) {
        super(parent);
    }

    @Override
    public IQueryWherePart<T> where(String whereClause) {
        setClause("WHERE " + whereClause);
        return this;
    }

    @Override
    public IQueryWherePart<T> where(String whereClause, String... params) {
        setClause("WHERE " + whereClause, params);
        return this;
    }

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        return getParent().limit(offset, limit);
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause) {
        return getParent().groupBy(groupByClause);
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause, String... params) {
        return getParent().groupBy(groupByClause, params);
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause) {
        return getParent().having(havingClause);
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause, String... params) {
        return getParent().having(havingClause, params);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause) {
        return getParent().orderBy(orderClause);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause, String... params) {
        return getParent().orderBy(orderClause, params);
    }
}
