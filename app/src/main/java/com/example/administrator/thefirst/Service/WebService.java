package com.example.administrator.thefirst.Service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.administrator.thefirst.helper.ImgIOJsonOutputUtils;
import com.example.administrator.thefirst.helper.MyDatabaseHelper;
import com.example.administrator.thefirst.helper.Shared;
import com.google.gson.JsonObject;

import org.apache.http.client.fluent.Content;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class WebService extends Service implements Runnable {
    private MyDatabaseHelper dbHelper;

    private static final String HOST = "192.168.119.179";
    private static final int PORT = 9999;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";

    public static  String username;
    public static String password;
    public static  String imeinumber;
    private SharedPreferences sp;



    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new WebBinder();
    }

    public class WebBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public WebService getService(){
            return WebService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sp = this.getSharedPreferences("service", Context.MODE_MULTI_PROCESS);
        username = sp.getString("username","");
        password = sp.getString("password","");
        imeinumber = sp.getString("imeinumber","");
        //创建数据库
        dbHelper = MyDatabaseHelper.getInstance(this);
        dbHelper.getReadableDatabase();
        new Thread(this).start();
    }

    public void register(String username, String password,String imeinumber){
        try {

            JSONObject object = new JSONObject();
            object.put("cmd", "register");
            object.put("username", username);
            object.put("password", password);
            object.put("imei", imeinumber);
            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println(object.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void deleteImei(){
//        try{if(flag){
//            JSONObject obj = new JSONObject();
//            obj.put("cmd","delete");
//            out.println(obj);
//            this.flag = false;
//        }}catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public void login(String username,String password,String imeinumber){
        try {

            JSONObject object = new JSONObject();
            object.put("cmd", "login");
            object.put("username", username);
            object.put("password",password);
            object.put("imei", imeinumber);

            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println(object.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sync(Context context){
        if (socket.isConnected()) {
            if (!socket.isOutputShutdown()) {
                try {
//                    if(!ifExit1(username,password)){
//
//                    }

//                    sp = this.getSharedPreferences("service", Context.MODE_MULTI_PROCESS);
//                    username = sp.getString("username","");
//                    password = sp.getString("password","");
                    JSONObject obj = new JSONObject();

                    obj.put("cmd", "syncfromserver");
                    obj.put("username",username);
                    obj.put("password",password);
                    obj.put("imeinumber",imeinumber);
                    if(!ifExit1(username,password)){
                        obj.put("flag",true);
                    }else{
                        obj.put("flag",false);
                    }
                    //obj.put("flag", Shared.flag);


//                    String str = ImgIOJsonOutputUtils.encodeImage("/storage/emulated/0/DCIM/Camera/IMG_20180309_091600.jpg");
////                    obj.put("image",str);
                    out.println(obj.toString());
                    //Shared.flag = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                socket = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
            } catch (Exception ex) {
                ex.printStackTrace();
                Intent intent = new Intent("sendContent");
                intent.putExtra("content", "与服务器连接异常，请检查您的网络");
                sendBroadcast(intent);
                //ShowDialog("登陆异常:" + ex.getMessage());

            }
            try {
                while (true) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {

                            if ((content = in.readLine()) != null) {
                                Log.i("TAG", "++ " + content);
                                JSONObject obj = new JSONObject(content);
                                if (obj.getString("cmd").equals("syncfromserver")) {
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    //values.put("uuid",obj.getString("uuid"));
                                    String uuid = obj.getString("uuid");
                                    //values.put("details", obj.getString("details"));
                                    putDetailsToContentValues(obj, values);
                                    int a = obj.getInt("a");
                                    if (a == 1) {
                                        values.put("a", 8);
                                        if (ifExit(uuid)) {
                                            db.update("storage", values, "uuid = ? and username = ? and password = ?", new String[]{uuid, username, password});
                                        }
                                    } else if (a == 2) {
                                        values.put("a", 9);
                                        values.put("uuid", uuid);
                                        db.insert("storage", null, values);
                                    } else if (a == 3) {
                                        values.put("a", 9);
                                        db.update("storage", values, "uuid = ? and username = ? and password = ?", new String[]{uuid, username, password});
                                    }

                                } else if (obj.getString("cmd").equals("syncfromclient")) {

                                } else if (obj.getString("cmd").equals("syncfromserverover")) {
                                    Log.i("TAG","1");
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    Log.i("TAG","2");
                                    Cursor cursor = db.rawQuery(
                                            "select * from   storage  where   a < 8 and username = '" + username + "' and password = '" + password + "'", null);
                                    Log.i("TAG","3");
                                    boolean flag = true;
                                    while (cursor.moveToNext()) {
                                        String uuid = cursor.getString(cursor.getColumnIndex("uuid"));

                                        int a = cursor.getInt(cursor.getColumnIndex("a"));
                                        JSONObject tempobj = new JSONObject();
                                        tempobj.put("cmd", "syncfromclient");
                                        tempobj.put("uuid", uuid);
                                        putDetailsToJSONObject(tempobj, cursor);
                                        //tempobj.put("details",details);
                                        tempobj.put("a", a);
                                        out.println(tempobj.toString());
                                        flag = false;
                                    }
                                    if(flag){
                                        JSONObject tempobj = new JSONObject();
                                        tempobj.put("cmd", "syncfromclient");
                                        out.println(tempobj.toString());
                                    }
                                    ContentValues values = new ContentValues();
                                    values.put("a", 8);
                                    db.update("storage", values, "a = 1 and username = '" + username + "' and password = '" + password + "' ", null);
                                    values.put("a", 9);
                                    db.update("storage", values, "a < 8 and username = '" + username + "' and password = '" + password + "'", null);

                                } else {
                                    content = obj.getString("cmd");
                                    Intent intent = new Intent("sendContent");
                                    intent.putExtra("content", content);
                                    sendBroadcast(intent);
                                    //mHandler.sendMessage(mHandler.obtainMessage());
                                }
                            } else {

                            }
                        }
                    }

                }//end of while
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized boolean ifExit(String str) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "select * from   storage  where   uuid = ? ",
                new String[] { str });
        while (cursor.moveToNext()) {
            db.close();

            return true;
        }
        db.close();
        return false;

    }

    private synchronized boolean ifExit1(String username,String password) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "select * from   storage  where   username = ? and password = ?",
                new String[] { username,password });
        while (cursor.moveToNext()) {
            db.close();

            return true;
        }
        db.close();
        return false;

    }

    private void putDetailsToJSONObject(JSONObject obj, Cursor cursor){
        try{
        String caption = cursor.getString(cursor.getColumnIndex("caption"));
        String label = cursor.getString(cursor.getColumnIndex("label"));
        int number = cursor.getInt(cursor.getColumnIndex("number"));
        String color = cursor.getString(cursor.getColumnIndex("color"));
        String position = cursor.getString(cursor.getColumnIndex("position"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        byte[] imageQuery = cursor.getBlob(cursor.getColumnIndex("image"));
        String image = ImgIOJsonOutputUtils.encode(imageQuery);
        obj.put("caption",caption);
        obj.put("label",label);
        obj.put("number",number);
        obj.put("color",color);
        obj.put("position",position);
        obj.put("date",date);
        obj.put("image",image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void putDetailsToContentValues(JSONObject obj,ContentValues values){
        try{
            String caption = obj.getString("caption");
            String label = obj.getString("label");
            int number = obj.getInt("number");
            String color = obj.getString("color");
            String position = obj.getString("position");
            String date = obj.getString("date");
            String image = obj.getString("image");
            byte[] imageQuery = ImgIOJsonOutputUtils.decode(image);
            values.put("username",username);
            values.put("password",password);
            values.put("caption",caption);
            values.put("label",label);
            values.put("number",number);
            values.put("color",color);
            values.put("position",position);
            values.put("date",date);
            values.put("image",imageQuery);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUsernameAndPassword(String username,String password,String imeinumber){
        SharedPreferences sp = getSharedPreferences("service", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        this.username = username;
        this.password = password;
        this.imeinumber = imeinumber;
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("imeinumber",imeinumber);
        editor.commit();
    }




}
