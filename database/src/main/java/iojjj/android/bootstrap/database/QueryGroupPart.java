package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQueryGroupPart;
import iojjj.android.bootstrap.database.interfaces.IQueryHavingPart;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;
import iojjj.android.bootstrap.database.interfaces.IQueryOrderPart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class QueryGroupPart<T extends IEntity> extends QueryPart<T> implements IQueryGroupPart<T> {

    QueryGroupPart(@NonNull Query<T> parent) {
        super(parent);
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause) {
        setClause("GROUP BY " + groupByClause);
        return this;
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause, String... params) {
        setClause("GROUP BY " + groupByClause, params);
        return this;
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

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        return getParent().limit(offset, limit);
    }
}
