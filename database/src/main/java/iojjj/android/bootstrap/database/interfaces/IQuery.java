package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQuery<T extends IEntity> extends IQueryMain<T>, IQueryWherePart<T>, IQueryGroupPart<T>, IQueryHavingPart<T>, IQueryOrderPart<T>, IQueryLimitPart<T> {

}
