package iojjj.android.bootstrap.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;
import iojjj.android.bootstrap.core.utils.misc.Config;
import iojjj.android.bootstrap.core.utils.misc.Optional;
import iojjj.android.bootstrap.core.utils.threading.ThreadUtils;
import iojjj.android.bootstrap.database.interfaces.IEntity;
import iojjj.android.bootstrap.database.interfaces.IQuery;

/**
 * Created by Alexander Vlasov on 07.10.2015.
 */
public class DbContext {

    private static final String PREFERENCES_NAME = "database_info";
    private static final String ENTRY_VERSION = "db_version_";
    private static final Map<String, DbContext> DATABASE_MAP = new HashMap<>();
    private static final Object lock = new Object();
    private static final ExecutorService DB_EXECUTOR = Executors.newFixedThreadPool(Config.CORES_COUNT);
    private final SQLiteHelper sqliteHelper;


    private final SharedPreferences preferences;
    private final String name;
    private final int version;
    private boolean closed;
    private Optional<Cache> cache;

    private DbContext(@NonNull Context context, @NonNull String name) {
        AssertionUtils.assertNotNull(context, "Context");
        AssertionUtils.assertNotEmpty(name, "Database name");
        this.name = name;
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.sqliteHelper = new SQLiteHelper(context, name, null, version = getLastVersion(name));
        this.cache = Optional.from(new Cache());
        this.sqliteHelper.getWritableDatabase();
    }

    private int getLastVersion(String name) {
        return preferences.getInt(ENTRY_VERSION + name, 1);
    }

    private void usingThis(@NonNull final SQLRunnable runnable) {
        AssertionUtils.assertNotNull(runnable, "SQLRunnable");
        ThreadUtils.runOnExecutor(new Runnable() {
            @Override
            public void run() {
                runnable.with(DbContext.this).run();
                DbContext dbContext = DATABASE_MAP.get(name);
                if (dbContext != null) {
                    synchronized (lock) {
                        dbContext = DATABASE_MAP.get(name);
                        if (dbContext != null) {
                            sqliteHelper.close();
                            DATABASE_MAP.put(name, null);
                            closed = true;
                        }
                    }
                }
            }
        }, DB_EXECUTOR);
    }

    SQLiteHelper getSqliteHelper() {
        return sqliteHelper;
    }

    Optional<Cache> getCache() {
        return cache;
    }

    boolean isClosed() {
        return closed || !sqliteHelper.getReadableDatabase().isOpen();
    }

//    public int version() {
//        return version;
//    }
//
//    public String name() {
//        return name;
//    }


    /**
     * Start new query from specified table.
     * @param clazz table class
     */
    public <T extends IEntity> IQuery<T> queryFrom(Class<T> clazz) {
        return new Query<>(this, clazz);
    }

    /**
     * Create new entity associated with database context.
     * @param clazz table class
     * @return new entity
     */
    public <T extends IEntity> T newEntity(Class<T> clazz) {
        try {
            T model = clazz.newInstance();
            model.init(EntityHelper.newInstance(this));
            return model;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Can't instantiate entity of class " + clazz.getSimpleName());
    }


    /**
     * Get instance of database, creating new on if necessary
     * @param context instance of context
     * @param name database name
     * @return instance of database
     * @throws AssertionError
     */
    @NonNull
    public static DbContext instance(@NonNull Context context, @NonNull String name) throws AssertionError {
        AssertionUtils.assertNotEmpty(name, "Name");
        name = name.trim();
        DbContext dbContext = DATABASE_MAP.get(name);
        if (dbContext == null) {
            synchronized (lock) {
                dbContext = DATABASE_MAP.get(name);
                if (dbContext == null) {
                    dbContext = new DbContext(context, name);
                    DATABASE_MAP.put(name, dbContext);
                }
            }
        }
        return dbContext;
    }

    /**
     * This method opens connection to database, runs SQLRunnable on non-main thread and then closes connection
     * @param context instance of context
     * @param name database name
     * @param runnable instance of runnable
     * @throws AssertionError
     */
    public static void using(@NonNull Context context, @NonNull String name, @NonNull SQLRunnable runnable) throws AssertionError {
        new DbContext(context, name).usingThis(runnable);
    }

    /**
     * Implementation of runnable that has instance of {@link DbContext}
     */
    public static abstract class SQLRunnable implements Runnable {

        private DbContext dbContext;

        public SQLRunnable() {
        }

        private SQLRunnable with(@NonNull DbContext dbContext) {
            this.dbContext = dbContext;
            return this;
        }

        @Override
        public final void run() {
            runImpl(dbContext);
        }

        protected abstract void runImpl(DbContext dbContext);
    }
}
