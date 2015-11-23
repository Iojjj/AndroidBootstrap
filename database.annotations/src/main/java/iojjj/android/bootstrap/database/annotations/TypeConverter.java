package iojjj.android.bootstrap.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Alexander Vlasov on 09.10.2015.
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.SOURCE)
public @interface TypeConverter {
}
