package iojjj.android.bootstrap.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;
import iojjj.android.bootstrap.core.utils.misc.MiscellaneousUtils;
import iojjj.android.bootstrap.core.utils.misc.Optional;
import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQuery;
import iojjj.android.bootstrap.database.interfaces.IQueryGroupPart;
import iojjj.android.bootstrap.database.interfaces.IQueryHavingPart;
import iojjj.android.bootstrap.database.interfaces.IQueryLimitPart;
import iojjj.android.bootstrap.database.interfaces.IQueryOrderPart;
import iojjj.android.bootstrap.database.interfaces.IQueryWherePart;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
class Query<T extends IEntity> implements IQuery<T> {

    private final DbContext dbContext;
    private final Class<T> tableClass;
    private final QueryWherePart<T> whereQuery;
    private final QueryLimitPart<T> limitQuery;
    private final QueryGroupPart<T> groupQuery;
    private final QueryHavingPart<T> havingQuery;
    private final QueryOrderPart<T> orderQuery;

    Query(@NonNull DbContext dbContext, @NonNull Class<T> tableClass) {
        AssertionUtils.assertNotNull(dbContext, "Database context");
        AssertionUtils.assertNotNull(tableClass, "Table class");
        this.dbContext = dbContext;
        this.tableClass = tableClass;
        this.whereQuery = new QueryWherePart<>(this);
        this.limitQuery = new QueryLimitPart<>(this);
        this.groupQuery = new QueryGroupPart<>(this);
        this.havingQuery = new QueryHavingPart<>(this);
        this.orderQuery = new QueryOrderPart<>(this);
    }

    @NonNull
    @Override
    public List<T> toList() {
        return toList(getCursor());
    }

    @NonNull
    @Override
    public OptionalCursor<T> toCursor() {
        return new OptionalCursor<>(getCursor(), this);
    }

    @Override
    public int count() {
        return getCount();
    }


    @NonNull
    @Override
    public Optional<T> first() {
        limitQuery.limit(1);
        List<T> results = toList();
        return Optional.from(results.isEmpty() ? null : results.get(0));
    }

    @Nullable
    private Cursor getCursor() {
        AssertionUtils.check(dbContext.isClosed(), "Database is closed");
        return dbContext.getSqliteHelper().getReadableDatabase().rawQuery(getSQL(false), getArgs());
    }

    @NonNull
    public List<T> toList(@Nullable Cursor cursor) {
        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return Collections.emptyList();
            }
            List<T> list = new ArrayList<>();
            do {
                T inst = dbContext.newEntity(tableClass);
                Field fieldId = tableClass.getSuperclass().getDeclaredField("id");
                Field fieldName = tableClass.getDeclaredField("name");
                fieldId.setAccessible(true);
                fieldName.setAccessible(true);
                fieldId.set(inst, cursor.getLong(cursor.getColumnIndex("id")));
                fieldName.set(inst, cursor.getString(cursor.getColumnIndex("name")));
                list.add(inst);
            } while (cursor.moveToNext());
            return list;
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            MiscellaneousUtils.close(cursor);
        }
        return Collections.emptyList();
    }

    private String getSQL(boolean count) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if (count)
            builder.append("COUNT(*) ");
        else
            builder.append("* ");
        builder.append("FROM ").append(getTableName());
        if (whereQuery.used())
            builder.append(" ").append(whereQuery.getClause());
        if (groupQuery.used())
            builder.append(" ").append(groupQuery.getClause());
        if (havingQuery.used())
            builder.append(" ").append(havingQuery.getClause());
        if (orderQuery.used())
            builder.append(" ").append(orderQuery.getClause());
        if (limitQuery.used())
            builder.append(" ").append(limitQuery.getClause());
        Log.d("DB", "sql: " + builder.toString() + "; args: " + Arrays.toString(getArgs()));
        return builder.toString();
    }

    private String getTableName() {
        return tableClass.getSimpleName().toLowerCase();
    }

    private String[] getArgs() {
        List<String> params = new ArrayList<>();
        if (whereQuery.used())
            Collections.addAll(params, whereQuery.getArgs());
        if (groupQuery.used())
            Collections.addAll(params, groupQuery.getArgs());
        if (havingQuery.used())
            Collections.addAll(params, havingQuery.getArgs());
        if (orderQuery.used())
            Collections.addAll(params, orderQuery.getArgs());
        return params.toArray(new String[params.size()]);
    }

    private int getCount() {
        AssertionUtils.check(dbContext.isClosed(), "Database is closed");
        int count = 0;
        Cursor cursor = dbContext.getSqliteHelper().getReadableDatabase().rawQuery(getSQL(true), getArgs());
        try {
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            MiscellaneousUtils.close(cursor);
        }
        return count;
    }

    @Override
    public IQueryWherePart<T> where(String whereClause) {
        return whereQuery.where(whereClause);
    }

    @Override
    public IQueryWherePart<T> where(String whereClause, String... params) {
        return whereQuery.where(whereClause, params);
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause) {
        return groupQuery.groupBy(groupByClause);
    }

    @Override
    public IQueryGroupPart<T> groupBy(String groupByClause, String... params) {
        return groupQuery.groupBy(groupByClause, params);
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause) {
        return havingQuery.having(havingClause);
    }

    @Override
    public IQueryHavingPart<T> having(String havingClause, String... params) {
        return havingQuery.having(havingClause, params);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause) {
        return orderQuery.orderBy(orderClause);
    }

    @Override
    public IQueryOrderPart<T> orderBy(String orderClause, String... params) {
        return orderQuery.orderBy(orderClause, params);
    }

    @Override
    public IQueryLimitPart<T> limit(int offset, int limit) {
        return limitQuery.limit(offset, limit);
    }
}
