package iojjj.android.bootstrap.database;

import android.support.annotation.NonNull;

import java.util.List;

import iojjj.android.bootstrap.assertions.AssertionUtils;
import iojjj.android.bootstrap.core.utils.Optional;
import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQuery;
import iojjj.android.bootstrap.database.interfaces.IQueryMain;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
abstract class QueryPart<T extends IEntity> implements IQueryMain<T> {

    private final IQuery<T> parent;
    private String clause;
    private String[] args;
    private boolean used;

    QueryPart(@NonNull Query<T> parent) {
        AssertionUtils.assertNotNull(parent, "Parent");
        this.parent = parent;
    }

    @NonNull
    @Override
    public OptionalCursor<T> toCursor() {
        return parent.toCursor();
    }

    @NonNull
    @Override
    public List<T> toList() {
        return parent.toList();
    }

    @Override
    public int count() {
        return parent.count();
    }

    @Override
    public Optional<T> first() {
        return parent.first();
    }

    protected IQuery<T> getParent() {
        return parent;
    }

    boolean used() {
        return used;
    }

    protected void setClause(String clause, String... args) {
        this.clause = clause;
        this.args = args;
        used = true;
    }

    String getClause() {
        return clause;
    }

    String[] getArgs() {
        return args;
    }
}
