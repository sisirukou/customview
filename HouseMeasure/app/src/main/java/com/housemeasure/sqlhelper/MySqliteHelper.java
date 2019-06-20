package com.housemeasure.sqlhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.housemeasure.constant.Constant;

/**
 * des:创建数据库
 * author: Yang Weisi
 * date 2019/1/14 21:59
 */
public class MySqliteHelper extends SQLiteOpenHelper {
    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySqliteHelper(Context context) {
        super(context, Constant.DATABASE_NAME,null,Constant.DATABASE_VERSION);
    }

    /**
     * 创建数据库
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table"+Constant.TABLE_NAME+"("
                +Constant.ID+"Integer primary key,"
                +Constant.ROOM_Length+"Double"
                +Constant.ROOM_WIDTH+"Double"
                +Constant.BRICK_SUM+"Double"
                +Constant.BRICK_INTEGER+"Double"
                +Constant.BRICK_HALF+"Double"
                +Constant.BRICK_INT_LENGTH+"Double"
                +Constant.BRICK_INT_WIDTH+"Double"
                +Constant.BRICK_HALF_LENGTH+"Double"
                +Constant.BRICK_HALF_WIDTH+"Double";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 更新数据库
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
