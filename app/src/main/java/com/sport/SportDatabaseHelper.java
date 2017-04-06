package com.sport;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import com.sport.entity.Location;
import com.sport.entity.Sport;

/**
 * Created by earthgee on 2016/4/16.
 */
public class SportDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="sport.sqlite";
    private static final int VERSON=1;

    private static final String TABLE_SPORT="sport";
    private static final String START_DATE="start_date";
    private static final String END_DATE="stop_date";
    private static final String STEP="step";

    private static final String TABLE_LOCATION="location";
    private static final String TIMESTAMP="timestamp";
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";
    private static final String SPORT_ID="sport_id";

    public SportDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sport ("+"_id integer primary key autoincrement,start_date integer,stop_date integer,step integer,img_position varchar(100))");
        db.execSQL("create table location(" + "timestamp integer,latitude real,longitude real,sport_id integer references sport(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //新增一条sport数据,插入id,开始时间
    public long insertSport(Sport sport){
        ContentValues cv=new ContentValues();
        cv.put(START_DATE,sport.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_SPORT,null,cv);
    }

    //新增一条location数据,插入某一时刻的位置信息
    public long insertLocation(long sportId,Location location){
        ContentValues cv=new ContentValues();
        cv.put(TIMESTAMP,location.getTime().getTime());
        cv.put(LATITUDE,location.getLatitude());
        cv.put(LONGITUDE,location.getLongitude());
        cv.put(SPORT_ID,sportId);
        return getWritableDatabase().insert(TABLE_LOCATION,null,cv);
    }

    //向sport中插入一条记录轨迹的结束时间,截图，步数待实现
    public void updateSport(Sport sport){
        ContentValues cv=new ContentValues();
        cv.put(STEP,sport.getStep());
        cv.put(END_DATE,sport.getEndDate().getTime());
        getWritableDatabase().update(TABLE_SPORT,cv,"_id=?",new String[]{String.valueOf(sport.getId())});
    }

}
