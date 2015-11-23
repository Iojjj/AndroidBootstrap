package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryGroupPart<T extends IEntity> extends IQueryHavingPart<T> {

    IQueryGroupPart<T> groupBy(String groupByClause);
    IQueryGroupPart<T> groupBy(String groupByClause, String... params);
}
