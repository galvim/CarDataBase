package com.example.lukaszjarka.cardatabase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.lukaszjarka.cardatabase.add.AddNewCarActivity;

public class BetterMotoContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.lukaszjarka.cardatabase";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private MotoDatabaseOpenHelper openHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int CARS_MULTIPLE_ITEM = 1;

    private static final int CARS_SINGLE_ITEM = 2;

    static {
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME, CARS_MULTIPLE_ITEM);
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME + "/#", CARS_SINGLE_ITEM);
    }

    private Cursor cursor;
    private long id;
    private String type;

    public BetterMotoContentProvider(AddNewCarActivity addNewCarActivity) {

    }


    @Override
    public boolean onCreate() {

        openHelper = new MotoDatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readebleDatabase = openHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case CARS_SINGLE_ITEM: {
                cursor = readebleDatabase.query(CarsTableContract.TABLE_NAME, projection, CarsTableContract._ID + " = ?",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            }
            case CARS_MULTIPLE_ITEM: {
                cursor = readebleDatabase.query(CarsTableContract.TABLE_NAME, projection , selection, selectionArgs, null, null, sortOrder);

            }
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            //  Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)){
            case CARS_SINGLE_ITEM: {
                type = "vnd.android.cursor.item/vnd.com.example.lukaszjarka.cardatabase.cars";
                break;
            }
            case CARS_MULTIPLE_ITEM: {
                type = "vnd.android.cursor.dir/vnd.com.example.lukaszjarka.cardatabase.cars";

            }
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (uriMatcher.match(uri)){
            case CARS_MULTIPLE_ITEM: {
               id =  openHelper.getWritableDatabase()
                        .insert(CarsTableContract.TABLE_NAME, null, values);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase writableDataBase = openHelper.getWritableDatabase();
        int deletedItems = 0;
        if (uriMatcher.match(uri) == CARS_SINGLE_ITEM) {
           deletedItems =  writableDataBase.delete(CarsTableContract.TABLE_NAME,CarsTableContract._ID + "= ?", new String[]{uri.getLastPathSegment()});

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedItems;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
