package fu.alfie.com.contentproviderexdemoa;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "fu.alfie.com.contentproviderexdemoa.MyContentProvider";
    private static final String DB_FILE = "user.db";
    private static final String DB_TABLE = "user";
    private static final int URI_ROOT = 0;
    private static final int TABLE_USER = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DB_TABLE);
    private static final UriMatcher uriMatcher = new UriMatcher(URI_ROOT);
    static {
        uriMatcher.addURI(AUTHORITY, DB_TABLE, TABLE_USER);
    }
    private SQLiteDatabase sqLiteDatabase;

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(),DB_FILE,null,1);
        sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + DB_TABLE + "'", null);
        if(cursor != null) { // 沒有資料表，建立一個資料表
            if(cursor.getCount() == 0){
                sqLiteDatabase.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY," +
                        "account_id TEXT NOT NULL," +
                        "mobile TEXT," +
                        "mail TEXT);");
            }
            cursor.close();
        }
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != TABLE_USER) throw new UnsupportedOperationException("Not yet implemented");

        long rowId = sqLiteDatabase.insert(DB_TABLE, null, values);
        Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(insertedRowUri, null);
        return insertedRowUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uriMatcher.match(uri) != TABLE_USER) throw new UnsupportedOperationException("Not yet implemented");

        int rowId = sqLiteDatabase.update(DB_TABLE, values, selection, selectionArgs);
        Uri updatedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(updatedRowUri, null);
        return rowId;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != TABLE_USER) throw new UnsupportedOperationException("Not yet implemented");

        int rowId = sqLiteDatabase.delete(DB_TABLE, selection, selectionArgs);
        Uri deletedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(deletedRowUri, null);
        return rowId;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) != TABLE_USER) throw new UnsupportedOperationException("Not yet implemented");

        Cursor cursor = sqLiteDatabase.query(true, DB_TABLE, projection, selection, null, null, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
