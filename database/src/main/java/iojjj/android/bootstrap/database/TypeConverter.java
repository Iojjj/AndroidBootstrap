package iojjj.android.bootstrap.database;

import android.support.annotation.Nullable;

/**
 * Created by Alexander Vlasov on 09.10.2015.
 */
public abstract class TypeConverter<From, To> {

    @Nullable
    public abstract To serialize(@Nullable From from);

    @Nullable
    public abstract From deserialize(@Nullable To from);

}
