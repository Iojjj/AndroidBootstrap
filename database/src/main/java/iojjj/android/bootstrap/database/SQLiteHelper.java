package iojjj.android.bootstrap.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import iojjj.android.bootstrap.database.interfaces.IEntity;

/**
 * Created by Alexander Vlasov on 07.10.2015.
 */
class SQLiteHelper extends SQLiteOpenHelper {

    private final IModelListener modelListener;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context.getApplicationContext(), name, factory, version);
        this.modelListener = null;
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, @NonNull final IModelListener modelListener) {
        super(context.getApplicationContext(), name, factory, version);
        this.modelListener = modelListener;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // TODO: 03.11.2015 add an option to enable WAL. Add methods to begin transaction in nonExclusive mode if WAL is enabled
        db.enableWriteAheadLogging();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "onCreate: " + getDatabaseName());
        if (modelListener != null) {
            for (IEntity entity : modelListener.getEntities()) {

            }
            for (TypeConverter converter : modelListener.getTypeConverters()) {

            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "onUpgrade: " + getDatabaseName());
    }

    public interface IModelListener {
        List<IEntity> getEntities();
        List<TypeConverter> getTypeConverters();
    }
}
