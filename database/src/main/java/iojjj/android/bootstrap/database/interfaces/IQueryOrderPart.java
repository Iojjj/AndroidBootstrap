package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryOrderPart<T extends IEntity> extends IQueryLimitPart<T> {
    IQueryOrderPart<T> orderBy(String orderClause);
    IQueryOrderPart<T> orderBy(String orderClause, String... params);
}
