package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryHavingPart<T extends IEntity> extends IQueryOrderPart<T> {
    IQueryHavingPart<T> having(String havingClause);
    IQueryHavingPart<T> having(String havingClause, String... params);
}
