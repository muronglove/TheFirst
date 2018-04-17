
package com.example.administrator.thefirst;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSONObject;
import com.baidu.translate.demo.TransApi;
import com.example.administrator.thefirst.helper.MyDatabaseHelper;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DateTimePicker;
import java.io.File;
import java.io.IOException;

import static android.provider.UserDictionary.Words.APP_ID;
import static android.widget.Toast.makeText;


public class NewCamera extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO= 0;
    public static final int  REQUEST_SELECT_IMAGE_IN_ALBUM= 1;
    private Uri mUriPhotoTaken;
    private File mFilePhotoTaken;
    private ImageView imageView ;
    private Bitmap mBitmap;
    private Uri mImageUri;
    private View inflate;
    private EditText mEditText;

    private VisionServiceClient client;
    private static final String APP_ID = "20180106000112508";
    private static final String SECURITY_KEY = "dkGciCsc_Xo8kXjUDpdz";

    private Dialog dialog;
    private MyDatabaseHelper dbHelper;
    private MyDateTimePicker picker2;

    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camera);
        //创建数据库
        dbHelper = new MyDatabaseHelper(this,"Storage.db",null,1);
        dbHelper.getReadableDatabase();

        if (client==null){
            client = new VisionServiceRestClient("204abc13061449da9613f09c37750006","https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
        }
        mEditText = (EditText)findViewById(R.id.camera_edittext);

        Toolbar cameraToolbar = (Toolbar) findViewById(R.id.camera_toolbar);
        setSupportActionBar(cameraToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


            //扫码部分
            RelativeLayout btnScan = (RelativeLayout) findViewById(R.id.camera_scan);
            btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new IntentIntegrator(NewCamera.this)
                            .setCaptureActivity(ScanActivity.class)
                            .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                            .setPrompt("请对准二维码")// 设置提示语
                            .setCameraId(0)// 选择摄像头,可使用前置或者后置
                            .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                            .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                            .initiateScan();// 初始化扫码
                }
            });


            Time t = new Time();
            t.setToNow();   //取得系统时间
            int year = t.year;
            int month = t.month + 1;
            int date = t.monthDay;
            int hour = t.hour;
            int minute = t.minute;
            TextView btn_date = (TextView) findViewById(R.id.text_date);
            btn_date.setText(year + "-" + month + "-" + date + " "
                    + hour + ":" + minute);
            final MyDateTimePicker picker1 = new MyDateTimePicker(this,DateTimePicker.HOUR_24) {
                @Override
                public void doSomething() {
                    this.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                        @Override
                        public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                            TextView btn_date = (TextView) findViewById(R.id.text_date);
                            btn_date.setText(year + "-" + month + "-" + day + " "
                                    + hour + ":" + minute);

                        }
                    });
                }
            };
            new DateTimePickerHelper(picker1).helper();
            picker2 = new MyDateTimePicker(this,DateTimePicker.HOUR_24) {
                @Override
                public void doSomething() {
                    this.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                        @Override
                        public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                            try {

                                java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date d1 = df.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
                                Date d2 = new Date(System.currentTimeMillis());
                                long diff = d1.getTime() - d2.getTime();
                                Intent intent = new Intent(NewCamera.this, AlarmReceiver.class);
                                intent.setAction("VIDEO_TIMER");
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(NewCamera.this, 0, intent, 0);
                                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + diff, pendingIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            };
            new DateTimePickerHelper(picker2).helper();

            RelativeLayout btn_datereminder = (RelativeLayout) findViewById(R.id.camera_date);
            btn_datereminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    picker1.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("ImageUri", mUriPhotoTaken);
    }
    // Recover the saved state when the activity is recreated.
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mUriPhotoTaken = savedInstanceState.getParcelable("ImageUri");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.camera_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            //取消存储
            case  R.id.camera_cancel_action:
                finish();
                return true;
                //存储到数据库
            case R.id.camera_save_action:
                ContentValues values = new ContentValues();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                values.put("color", ((EditText) findViewById(R.id.camera_edittext)).getText().toString());
                values.put("caption", ((EditText) findViewById(R.id.camera_edittext)).getText().toString());
                values.put("tag", ((TextView) findViewById(R.id.camera_label)).getText().toString());
                values.put("cabinet", ((TextView) findViewById(R.id.camera_select_number)).getText().toString());
                values.put("image", output.toByteArray());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.insert("storage", null, values);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        //扫码部分
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                new GetBookTask().execute(result.getContents());
            }
        } else{
            try{switch(requestCode){
                case REQUEST_TAKE_PHOTO:
                    //if(resultCode == RESULT_OK){
                    Intent intent = new Intent();
                    intent.setData(Uri.fromFile(mFilePhotoTaken));
                    mImageUri = intent.getData();
                    mBitmap =ImagineHelper.loadSizeLimitedBitmapFromUri(
                            mImageUri, getContentResolver());
                    if (mBitmap != null) {
                        imageView = findViewById(R.id.camera_photograph);
                        imageView.setImageBitmap(mBitmap);
                        doDescribe();
                    }
                    //finish();
                    //}
                    break;
                case REQUEST_SELECT_IMAGE_IN_ALBUM:{
                    //if(REQUEST_SELECT_IMAGE_IN_ALBUM ==RESULT_OK ){
                    try{
                        Uri selectedImage = data.getData();
                        String []filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                        cursor.moveToFirst();
                        int columnIndex  =cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);
                        mBitmap = BitmapFactory.decodeFile(path);
                        if (mBitmap != null) {
                            Toast.makeText(this,"Create succeeded",Toast.LENGTH_SHORT).show();
                            imageView = findViewById(R.id.camera_photograph);
                            imageView.setImageBitmap(mBitmap);
                            doDescribe();
                        }
                        else {
                            Toast.makeText(this,"Create failed",Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //}
                break;
                default:
                    break;
            }}catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public void takePhoto(View view){
        dialog.dismiss();
        try{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try{
                    mFilePhotoTaken = File.createTempFile(
                            "IMG_",  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                    if(mFilePhotoTaken != null){
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mUriPhotoTaken = FileProvider.getUriForFile(this,"com.example.administrator.thefirst.fileprovider",mFilePhotoTaken);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    }
                }catch (IOException e){
                    //setInfo(e.getMessage());
                }
            }
        }catch(Exception d){
            d.printStackTrace();
        }

    }
//    public void setRequstSelectImagrInAlbum(View view){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("mFilePhotoTaken/*");
//        if(intent.resolveActivity(getPackageManager())!= null){
//            startActivityForResult(intent,REQUEST_SELECT_IMAGE_IN_ALBUM);
//        }
//    }



    class GetBookTask extends AsyncTask<String,Integer,BookInfo>{

        @Override
        protected BookInfo doInBackground(String... strings) {
            DouBanBookInfoXmlParser parser=new DouBanBookInfoXmlParser();
            BookInfo info=null;
            try{
                info=parser.fetchBookInfoByXML(strings[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            return info;
        }

//        @Override
//        protected void onPostExecute(BookInfo bookInfo) {
//            super.onPostExecute(bookInfo);
//            TextView textView=findViewById(R.id.txt_scan_result);
//            textView.setText(bookInfo.getAuthor()+"  "+bookInfo.getPrice());
//        }
    }

    public void show(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.camera_out, null);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        Button cancel = inflate.findViewById(R.id.cancel_photo);
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          dialog.dismiss();
                                      }
                                  }
        );
        Button selectPhoto_Button = inflate.findViewById(R.id.choosePhoto);
        selectPhoto_Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_SELECT_IMAGE_IN_ALBUM );
            }
        });
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
          lp.y = 20;//设置Dialog距离底部的距离
////       将属性设置给窗体
          dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();//显示对话框
    }
    public void locationDialog(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.location_dialog, null);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        TextView cancel = inflate.findViewById(R.id.location_dialog_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          dialog.dismiss();
                                      }
                                  }
        );
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 20;//设置Dialog距离底部的距离
////       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();//显示对话框
    }
    public void numDialog(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.num_dialog, null);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        TextView cancel = inflate.findViewById(R.id.num_dialog_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          dialog.dismiss();
                                      }
                                  }
        );
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 20;//设置Dialog距离底部的距离
////       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();//显示对话框
    }
    public void labelDialog(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.label_dialog, null);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        TextView cancel = inflate.findViewById(R.id.label_dialog_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          dialog.dismiss();
                                      }
                                  }
        );
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 20;//设置Dialog距离底部的距离
////       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();//显示对话框
    }
    public void labelDaialogOnclick(View view){
        TextView  textView= (TextView)view;
        TextView button =  findViewById(R.id.camera_label);
        button.setText(textView.getText().toString());
        dialog.dismiss();
    }
    public void numDaialogOnclick(View view){
        TextView  textView= (TextView)view;
        TextView button =  findViewById(R.id.camera_select_number);
        button.setText(textView.getText().toString());
        dialog.dismiss();
    }
    public void locationDaialogOnclick(View view){
        TextView  textView= (TextView)view;
        TextView button =  findViewById(R.id.camera_location);
        button.setText(textView.getText().toString());
        dialog.dismiss();
    }







    public void doDescribe() {
        //mButtonSelectImage.setEnabled(false);
        mEditText.setText("描述中……");

        try {
            new doRequest().execute();
        } catch (Exception e)
        {
            mEditText.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();
        //String[] features = {"ImageType", "Color", "Faces", "Adult", "Categories"};
        String[] features = {"Color","Description"};
        String[] details = {};

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        AnalysisResult v = this.client.analyzeImage(inputStream, features, details);
        //AnalysisResult h = this.client.describe(inputStream, 1);

//        String result = gson.toJson(v);
//        Log.d("result", result);
//        Gson gson = new Gson();
        AnalysisResult result = gson.fromJson(gson.toJson(v), AnalysisResult.class);


        try{
            TransApi api = new TransApi(APP_ID, SECURITY_KEY);
            String caption = result.description.captions.get(0).text;
            String jsonString = api.getTransResult(caption, "auto", "zh");
            JSONObject obj = JSONObject.parseObject(jsonString);
            String Caption = obj.getJSONArray("trans_result").getJSONObject(0).getString("dst");

            String tag = result.description.tags.get(0);
            String jsonString1 = api.getTransResult(tag, "auto", "zh");
            JSONObject obj1 = JSONObject.parseObject(jsonString1);
            String Tag = obj1.getJSONArray("trans_result").getJSONObject(0).getString("dst");

            String color = result.color.dominantColorForeground;
            String jsonString2 = api.getTransResult(color, "auto", "zh");
            JSONObject obj2 = JSONObject.parseObject(jsonString2);
            String Color = obj2.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            JSONObject object = new JSONObject();
            object.put("caption",Caption);
            object.put("tag",Tag);
            object.put("color",Color);
            return object.toString();


        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }



    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            mEditText.setText("");
            if (e != null) {
                mEditText.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                JSONObject obj = JSONObject.parseObject(data);
                mEditText.append("标题:"+obj.getString("caption"));
                mEditText.append("\n");
                mEditText.append("标签:"+obj.getString("tag")+"\n");
                mEditText.append("\n");
                mEditText.append("颜色："+obj.getString("color"));
//                ContentValues values = new ContentValues();
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//                values.put("color",obj.getString("color"));
//                values.put("caption",obj.getString("caption"));
//                values.put("tag",obj.getString("tag"));
//                values.put("cabinet",1);
//                values.put("image",output.toByteArray());
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                db.insert("storage",null,values);

                //mEditText.append("Image format: " + result.metadata.format + "\n");
                //mEditText.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                //mEditText.append("\n");

//                for (Caption caption: result.description.captions) {
//                    mEditText.append("Caption: " + caption.text + ", confidence: " + caption.confidence + "\n");
//                }



//                for (String tag: result.description.tags) {
//                    mEditText.append("Tag: " + tag + "\n");
//                }


//                mEditText.append("\n--- Raw Data ---\n\n");
                //mEditText.append(data);
//                mEditText.setSelection(0);
            }
            //mButtonSelectImage.setEnabled(true);
        }
    }


    public void swtich1Click(View view){
        Switch sw = (Switch)view;
        boolean isChecked = sw.isChecked();
        if(isChecked){
            //Toast.makeText(NewCamera.this, "开启", Toast.LENGTH_SHORT).show();
            picker2.show();
        }else {
            //Toast.makeText(NewCamera.this, "关闭", Toast.LENGTH_SHORT).show();
        }
    }
}
