package iojjj.android.bootstrap.database.interfaces;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IQueryLimitPart<T extends IEntity> extends IQueryMain<T> {

    /**
     * Set amount of rows to skip
     * @param offset amount of rows to skip
     * @param limit amount of rows to take
     * @throws AssertionError if value is negative
     */
    IQueryLimitPart<T> limit(int offset, int limit);
}
