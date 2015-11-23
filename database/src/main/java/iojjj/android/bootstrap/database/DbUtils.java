package iojjj.android.bootstrap.database;

/**
 * Database utils
 */
public class DbUtils {

    /**
     * Generate placeholders for SQL query (like ?,?,...,?)
     * @param len number of placeholders
     * @return placeholders string
     * @throws IllegalArgumentException if len less or equals zero
     */
    public static String makePlaceholders(final int len) {
        if (len < 1)
            throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(len * 2 - 1);
        sb.append("?");
        for (int i = 1; i < len; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }
}
