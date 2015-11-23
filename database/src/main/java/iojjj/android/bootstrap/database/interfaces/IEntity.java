package iojjj.android.bootstrap.database.interfaces;

import android.support.annotation.NonNull;

import iojjj.android.bootstrap.database.EntityHelper;

/**
 * Created by Alexander Vlasov on 10.10.2015.
 */
public interface IEntity {

    void save();

    void delete();

    void init(@NonNull EntityHelper entityHelper);
}
