package com.cnode.wephone.cnode.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cnode.wephone.cnode.App;

import java.util.Date;

/**
 * Created by ASUS on 2016/3/23.
 * 简单的数据库辅助类
 * 存储用户信息及各栏目首页数据
 */
public class DBHelper {
    private final SQLiteDatabase db;

    public static DBHelper newInstance(){
        return new DBHelper();
    }

    private DBHelper(){
        db = new SQLiteHelper().getWritableDatabase();
    }

    /**
     * 获取数据
     * @param tab 标签
     * @return 更新时间、内容
     */
    public String[] get(String tab) {
        Cursor c= db.rawQuery("select update_time, content from data_cache where tab=?", new String[]{tab});//意思应该是根据tab取出相应的数据库行
        if (c.moveToFirst()){
            try {
                String update_time = String.valueOf(c.getInt(c.getColumnIndex("update_time")));
                String content = c.getString(c.getColumnIndex("content"));
                c.close();
                return new String[]{update_time, content};
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 存入数据
     * @param tab 标签
     * @param content 数据
     */
    public void save(String tab, String content){//保存content---请求
        ContentValues values=new ContentValues();
        values.put("update_time", new Date().getTime()/1000);
        values.put("content", content);
        if (get(tab) != null){
            db.update("data_cache", values, "tab=?", new String[]{tab});//database更新 意思应该是根据tab把数据存进去并替换之前的
        } else {
            values.put("tab", tab);
            db.insert("data_cache", null, values);//之前没数据的话 不用更新 直接存进去
        }
    }
//数据库里的content是个json数据
    class SQLiteHelper extends SQLiteOpenHelper {
        //数据库名称
        private final static String DB_NAME = "cnode.db";
        //数据库版本
        private final static int DB_Version = 1;
        //SQL语句，创建表 创建Datacache表 设置四个参数 id--int tab--非空字符 时间--int content
        private final static String CREATE_CACHE_TABLE = "create table data_cache(" +
                "_id integer primary key autoincrement," +
                "tab char(10) not null," +
                "update_time integer," +
                "content text" +
                ")";

        public SQLiteHelper() {
            super(App.getContext(), DB_NAME, null, DB_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CACHE_TABLE);
        }//创建数据库时把这个database文件插入

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            LogUtil.d(null, "数据库升级啦：" + oldVersion + "--->" + newVersion);
        }
    }
}
