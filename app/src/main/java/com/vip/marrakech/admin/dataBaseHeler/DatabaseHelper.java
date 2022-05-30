package com.vip.marrakech.admin.dataBaseHeler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.vip.marrakech.admin.models.MasterBottelModel;
import com.vip.marrakech.admin.models.MasterVenueModel;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by IGTS04 on 04-Jan-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MasterDB";
    private static final String TABLE_VENUE = "VenueTable";
    private static final String TABLE_BOTTLE = "BottleTable";

    private static final String KEY_ID = "key_id";

    private static final String KEY_VENDOR_ID = "vendor_id";
    private static final String KEY_TITLE= "title";
    private static final String KEY_VENUE_TYPE= "venue_type";
    private static final String KEY_STATUS= "status";

    private static final String KEY_VENUE_ID = "venue_id";
    private static final String KEY_BOTTLE_NAME= "bottle_name";
    private static final String KEY_BOTTLE_TYPE= "bottle_type";
    private static final String KEY_BOTTLE_PRICE= "bottle_price";


    private static final String CREATE_TABLE_VENUE = "CREATE TABLE "
            + TABLE_VENUE + "(" + KEY_ID + " TEXT,"
            + KEY_VENDOR_ID + " TEXT,"
            + KEY_TITLE + " TEXT,"
            + KEY_VENUE_TYPE + " TEXT,"
            + KEY_STATUS + " TEXT" + ")";

    private static final String CREATE_TABLE_BOTTLE = "CREATE TABLE "
            + TABLE_BOTTLE + "(" + KEY_ID + " TEXT,"
            + KEY_VENUE_ID + " TEXT,"
            + KEY_BOTTLE_NAME + " TEXT,"
            + KEY_BOTTLE_TYPE + " TEXT,"
            + KEY_BOTTLE_PRICE + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VENUE);
        db.execSQL(CREATE_TABLE_BOTTLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_VENUE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BOTTLE);
        onCreate(db);
    }

    public boolean insertVenue(MasterVenueModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertData = new ContentValues();
        insertData.put(KEY_ID, model.getId());
        insertData.put(KEY_VENDOR_ID, model.getVendor_id());
        insertData.put(KEY_TITLE, model.getTitle());
        insertData.put(KEY_VENUE_TYPE, model.getVenue_type());
        insertData.put(KEY_STATUS,model.getStatus());
        long is=db.insert(TABLE_VENUE, null, insertData);
        return is!=-1;
    }

    public boolean insertBottle(MasterBottelModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertData = new ContentValues();
        Gson gson = new Gson();
        insertData.put(KEY_ID, model.getId());
        insertData.put(KEY_VENUE_ID, model.getVenue_id());
        insertData.put(KEY_BOTTLE_NAME, model.getBottle_name());
        insertData.put(KEY_BOTTLE_TYPE, model.getBottle_type());
        insertData.put(KEY_BOTTLE_PRICE,model.getBottle_price());
        long is=db.insert(TABLE_BOTTLE, null, insertData);
        return is!=-1;
    }

    public List<MasterVenueModel> getAllVenue(){
        List<MasterVenueModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_VENUE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                MasterVenueModel model = new MasterVenueModel();

                model.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                model.setVendor_id(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ID)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                model.setVenue_type(cursor.getString(cursor.getColumnIndex(KEY_VENUE_TYPE)));
                model.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                data.add(model);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }


    public List<MasterVenueModel> getAllVenue(String venueType){
        List<MasterVenueModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_VENUE+" WHERE "+KEY_VENUE_TYPE+" = '"+venueType+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                MasterVenueModel model = new MasterVenueModel();

                model.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                model.setVendor_id(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ID)));
                model.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                model.setVenue_type(cursor.getString(cursor.getColumnIndex(KEY_VENUE_TYPE)));
                model.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                data.add(model);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
     /*   MasterVenueModel model = new MasterVenueModel();
        model.setTitle("Select Venue");
        model.setId("0001");
        data.add(0,model);*/
        return data;
    }


    public List<MasterBottelModel> getAllBottle(){
        List<MasterBottelModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_VENUE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                MasterBottelModel model = new MasterBottelModel();

                model.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                model.setVenue_id(cursor.getString(cursor.getColumnIndex(KEY_VENUE_ID)));
                model.setBottle_name(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_NAME)));
                model.setBottle_type(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_TYPE)));
                model.setBottle_price(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_PRICE)));
                data.add(model);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public List<MasterBottelModel> getAllBottle(String bottleType){


        List<MasterBottelModel> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_BOTTLE+" WHERE "+KEY_BOTTLE_TYPE+" = '"+bottleType+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                MasterBottelModel model = new MasterBottelModel();

                model.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                model.setVenue_id(cursor.getString(cursor.getColumnIndex(KEY_VENUE_ID)));
                model.setBottle_name(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_NAME)));
                model.setBottle_type(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_TYPE)));
                model.setBottle_price(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_PRICE)));
                data.add(model);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
     /*   MasterBottelModel model = new MasterBottelModel();
        model.setBottle_type(bottleType);
        model.setBottle_name("Select Bottle Name");
        model.setBottle_price("0");
        model.setVenue_id("");
        data.add(0,model);*/
        return data;
    }


    public List<String> getBottleType(String venue_id){

        List<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+KEY_BOTTLE_TYPE+" FROM "+TABLE_BOTTLE+" WHERE "+KEY_VENUE_ID+" = "+venue_id+" GROUP BY "+KEY_BOTTLE_TYPE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                data.add(cursor.getString(cursor.getColumnIndex(KEY_BOTTLE_TYPE)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
       /* data.add(0,"Select Bottle Type");*/
        return data;
    }


    public List<String> getVenueTypeType(){
        List<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+KEY_VENUE_TYPE+" FROM "+TABLE_VENUE+" GROUP BY "+KEY_VENUE_TYPE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                data.add(cursor.getString(cursor.getColumnIndex(KEY_VENUE_TYPE)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        data.add(0,"Venue Type");
        return data;
    }

  /*  public boolean checkExist(String quote_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE+" where "+KEY_QUOTE_ID+"='"+quote_id+"'";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return  count != 0;
    }
    public boolean deleteQuote(String quote_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, KEY_QUOTE_ID + "=" + quote_id, null) > 0;
    }*/

  public void deleteDB(){
      SQLiteDatabase db = this.getWritableDatabase();
          String clearVenue = "DELETE FROM "+TABLE_VENUE;
          String clearBottle = "DELETE FROM "+TABLE_BOTTLE;
          db.execSQL(clearVenue);
          db.execSQL(clearBottle);
  }

}
