package iojjj.androidbootstrap.database;

/**
 * Database utils
 */
public class DatabaseUtils {

    /**
     * Generate placeholders for SQL query (like ?,?,...,?)
     * @param len number of placeholders
     * @return placeholders string
     */
    public static String makePlaceholders(final int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        }
        StringBuilder sb = new StringBuilder(len * 2 - 1);
        sb.append("?");
        for (int i = 1; i < len; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }
}
