package iojjj.android.bootstrap.database.annotations;

/**
 * Created by Alexander Vlasov on 03.11.2015.
 */
public enum Collate {
    /**
     * Default collate. SQLite will use its rules for determining it.
     */
    NONE,

    /**
     * Compares string data using memcmp(), regardless of text encoding.
     */
    BINARY,

    /**
     * The same as binary, except the 26 upper case characters of ASCII are folded to their lower case equivalents before the comparison is performed. Note that only ASCII characters are case folded. SQLite does not attempt to do full UTF case folding due to the size of the tables required.
     */
    NOCASE,

    /**
     * The same as binary, except that trailing space characters are ignored.
     */
    RTRIM,

    /**
     * Using comparison algorithm that uses the system's current locale.
     */
    LOCALIZED,

    /**
     * Using the Unicode Collation Algorithm that not tailored to the current locale.
     */
    UNICODE
}
