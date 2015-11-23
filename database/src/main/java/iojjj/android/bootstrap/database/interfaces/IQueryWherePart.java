package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryWherePart<T extends IEntity> extends IQueryGroupPart<T> {

    IQueryWherePart<T> where(String whereClause);
    IQueryWherePart<T> where(String whereClause, String... params);
}
