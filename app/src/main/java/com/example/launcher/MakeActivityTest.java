package com.example.launcher;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MakeActivityTest extends AppCompatActivity {
    CheckBox ck_walk, ck_run;
    private String dateTimeString;
    private TextView tv_time, tv_time_show;
    EditText et_line_size;
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);
         et_line_size = findViewById(R.id.et_line_size);
        findViewById(R.id.add_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainCameraPermission();
            }
        });
        findViewById(R.id.make_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.et_gongli);
                String gongli = et.getText().toString();
                try {
                    if(TextUtils.isEmpty(gongli)){
                        gongli = "7.42";
                    }
                    Float.parseFloat(gongli);
                    if (!gongli.contains(".")) {
                        showToast("请输入有效的公里数，必须带小数点后两位");
                        return;
                    } else {
                        String end = gongli.substring(gongli.indexOf("."));
                        if (end == null || end.length() != 3) {
                            showToast("请输入有效的公里数，必须带小数点后两位");
                            return;
                        }
                    }
                } catch (NumberFormatException e) {
                    showToast("请输入有效的公里数，必须带小数点后两位");
                    return;
                }
                if (sportBitmap == null) {
                    showToast("先选择运动图片");
                    return;
                }
                if (dateTimeString == null) {
                    dateTimeString = "2019年4月3日 23:58";
//                    showToast("先选择运动时间");
//                    return;
                }
                String lineWidth = et_line_size.getText().toString().trim();
                int width = 16;
                try{
                    width = Integer.parseInt(lineWidth);
                }catch (Exception e){

                }
                Intent intent = new Intent(MakeActivityTest.this, IsportActivity.class);
                intent.putExtra("gongli", gongli);
                intent.putExtra("time", dateTimeString);
                intent.putExtra("isWalk", ck_walk.isChecked());
                intent.putExtra("lineWidth", width);
                startActivity(intent);
            }
        });
        findViewById(R.id.msg_rl).setVisibility(View.INVISIBLE);
        ck_run = findViewById(R.id.ck_run);
        ck_walk = findViewById(R.id.ck_walk);
        ck_run.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ck_walk.setChecked(!isChecked);
            }
        });
        ck_walk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ck_run.setChecked(!isChecked);
            }
        });
        tv_time_show = findViewById(R.id.tv_time_show);
        tv_time = findViewById(R.id.tv_time);
        tv_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_time.getPaint().setAntiAlias(true);//抗锯齿
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        initImageView();
        Uri uri = Uri.parse("content://media/external/images/media/841833");
        handleImageResult(uri);
    }

    private void initImageView() {
        ImageView iv = findViewById(R.id.img);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.i("lc_miao", "initImageView widthPixels:" + dm.widthPixels);
        Log.i("lc_miao", "initImageView heightPixels:" + dm.heightPixels);
        iv.getLayoutParams().width = dm.widthPixels;
        iv.getLayoutParams().height = (int) (dm.widthPixels * 0.5625f);
        iv.requestLayout();
    }

    /**
     * 打开系统相册
     */
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 160);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            handleImgeOnKitKat(data);
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 122);
        } else {//有权限直接调用系统相机拍照
            openSysAlbum();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 122://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        Uri uri = data.getData();
       handleImageResult(uri);
    }
    private void handleImageResult(Uri uri){
        String imagePath = null;
        Log.d("uri=intent.getData :", "" + uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);        //数据表里指定的行
                Log.d("getDocumentId(uri) :", "" + docId);
                Log.d("uri.getAuthority() :", "" + uri.getAuthority());
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        }
        displayImage(imagePath);
    }
    /**
     * 通过uri和selection来获取真实的图片路径,从相册获取图片时要用
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 拍完照和从相册获取玩图片都要执行的方法(根据图片路径显示图片)
     */
    public static Bitmap sportBitmap;

    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            if (sportBitmap != null) {
                sportBitmap.recycle();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//获取图片
            sportBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            ImageView iv = findViewById(R.id.img);
            iv.setImageBitmap(rotateBitmap(bitmap,-90));
            if (findViewById(R.id.msg_rl).getVisibility() != View.VISIBLE) {
                findViewById(R.id.msg_rl).setVisibility(View.VISIBLE);
            }
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }

        return bitmap;

    }


    /**
     * 展示日期选择对话框
     */
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(MakeActivityTest.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTimeString = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                Log.i("2222222", "showDatePickerDialog dateTimeString:" + dateTimeString);
                showTimePickerDialog();

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    /**
     * 展示时间选择对话框
     */
    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(MakeActivityTest.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateTimeString += " " + hourOfDay + ":" + minute;
                Log.i("2222222", "showTimePickerDialog dateTimeString:" + dateTimeString);
                tv_time_show.setText(dateTimeString);
                tv_time.setText("重新选择");
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();

    }
}
