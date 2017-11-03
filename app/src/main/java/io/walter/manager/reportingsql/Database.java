package io.walter.manager.reportingsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import io.walter.manager.utils.CalendarUtils;

/**
 * Created by walter on 11/2/17.
 */

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, "reports_db.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE purchaseSummary (\n" +
                "  id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  month TEXT NOT NULL,\n" +
                "  code INT NOT NULL,\n" +
                "  totalSales REAL NOT NULL,\n" +
                "  numberSales INTEGER NOT NULL,\n" +
                "  UNIQUE (code) ON CONFLICT REPLACE\n" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS purchaseSummary";
        db.execSQL(sql);
        String query="CREATE TABLE IF NOT EXISTS purchaseSummary (\n" +
                "  id  INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  month TEXT NOT NULL,\n" +
                "  code INT NOT NULL,\n" +
                "  totalSales REAL NOT NULL,\n" +
                "  numberSales INTEGER NOT NULL,\n" +
                "  UNIQUE (code) ON CONFLICT REPLACE\n" +
                ")";
        db.execSQL(query);
    }

    public  void saveData(String month,int code,double totalSales,int numberSales){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String month_words= CalendarUtils.ConvertToMonthInWords(month);
        values.put("month", month_words);
        values.put("code", code);
        values.put("totalSales",totalSales);
        values.put("numberSales", numberSales);
        database.insert("purchaseSummary", null, values);
        Log.d("DATABASE_INSERT", "saveData: "+code);
        database.close();
    }

    public ArrayList<MonthlySale> getData(){
        ArrayList<MonthlySale> data=new ArrayList<>();
        String selectQuery = "SELECT  month, SUM(totalSales), SUM(numberSales) from purchaseSummary GROUP BY month";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MonthlySale sale=new MonthlySale(cursor.getDouble(1), cursor.getInt(2), cursor.getString(0));
                data.add(sale);
                Log.d("DATABASE_INSERT", "saveData: "+cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return  data;
    }


}
