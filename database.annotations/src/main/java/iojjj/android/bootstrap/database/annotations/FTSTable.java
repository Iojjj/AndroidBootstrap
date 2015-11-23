package iojjj.android.bootstrap.database.annotations;

/**
 * Created by Alexander Vlasov on 03.11.2015.
 */
// TODO: 03.11.2015 maybe in future
public @interface FTSTable {

    String name() default "";
    FullTextSearch ftsType() default FullTextSearch.FTS3;

    enum FullTextSearch {
        FTS3,
        FTS4
    }
}
