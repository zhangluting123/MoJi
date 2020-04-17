package cn.edu.hebtu.software.test.DetailActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.UploadAndDownload.UploadFileTask;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DensityUtil;
import cn.edu.hebtu.software.test.Util.FileUtil;

public class AddNoteActivity extends AppCompatActivity {
    //纬度
    private double latitude;
    //经度
    private double longitude;
    //位置
    private String location;
    //ip
    private String ip;
    //用户名
    private String userId;
    //图片
    private List<String> imgList = new ArrayList<>();
    //自定义监听器
    private MyCustomListener customListener;
    private customTextChangedListener customTextChangedListener;
    //控件
    private Toolbar addNoteToolbar;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button btnIssue;
    private TextView tvCanWrite;
    private CheckBox checkboxSelf;
    private ImageButton uploadImage;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;

    private PopupWindow popupWindow;
    private View v;
    //参数
    private int self;
    private int i;
    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    private String mTempPhotoPath;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));
        }

        i = 0;

        final MyApplication data = (MyApplication) getApplication();
        userId = data.getUser().getUserId();
        ip = data.getIp();

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0.00);
        longitude = intent.getDoubleExtra("longitude",0.00);
        location = intent.getStringExtra("location");

        getViews();
        //替换默认的ActionBar
        setSupportActionBar(addNoteToolbar);
        registListener();
    }

    /**
     *  @author 春波
     *  @time 2019/11/25  16:50
     *  @Description：获取控件对象
     */
    private void getViews() {
        addNoteToolbar = findViewById(R.id.addNoteToolbar);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        btnIssue = findViewById(R.id.btnIssue);
        tvCanWrite = findViewById(R.id.tvCanWrite);
        checkboxSelf = findViewById(R.id.checkboxSelf);
        uploadImage = findViewById(R.id.uploadImage);
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.popup_layout, null);
        btnTakePhoto = v.findViewById(R.id.take_photo);
        btnChooseFromAlbum = v.findViewById(R.id.choose_from_album);
    }

    /**
     *  @author 春波
     *  @time 2019/11/25  16:50
     *  @Description：注册监听器
     */
    private void registListener() {
        customListener = new MyCustomListener();
        customTextChangedListener = new customTextChangedListener();
        btnIssue.setOnClickListener(customListener);
        uploadImage.setOnClickListener(customListener);
        btnTakePhoto.setOnClickListener(customListener);
        btnChooseFromAlbum.setOnClickListener(customListener);
        //监听已经输入的字符个数
        editTextContent.addTextChangedListener(customTextChangedListener);

        addNoteToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    /**
     *  @author 春波
     *  @time 2019/12/4  14:49
     *  @Description：弹出选择窗口
     */
    public void showPopupWindow(){
        popupWindow = new PopupWindow(this);
        //设置popupWindow显示的宽度（填充整个屏幕）
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        Button button = v.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//关闭弹出窗口
                popupWindow.dismiss();
            }
        });
        //设置弹出窗口显示的内容
        popupWindow.setContentView(v);
        //获取弹出窗口父视图对象
        HorizontalScrollView parent = findViewById(R.id.parent);
        //showAsDropDown 自动显示在button下方，showAtLocation 显示位置
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }

    /**
     *  @author 春波
     *  @time 2019/12/4  14:36
     *  @Description：监听已经输入的字符个数
     */
    class customTextChangedListener implements TextWatcher{
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            tvCanWrite.setText("还可以输入" + (300 - temp.length()) + "个字符");
        }
    }

    /**
     *  @author 春波
     *  @time 2019/11/25  17:03
     *  @Description：自定义事件监听器
     */
    class MyCustomListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.uploadImage:
                    showPopupWindow();
                    break;
                case R.id.take_photo:
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(AddNoteActivity.this, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{Manifest.permission.CAMERA}, RC_TAKE_PHOTO);
                    }else{
                        takePhoto();
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                        ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
                    } else {
                        //已授权，获取照片
                        choosePhoto();
                    }
                    popupWindow.dismiss();
                    break;
                //发布便签
                case R.id.btnIssue:
                    if(editTextTitle.length() == 0) {
                        Toast toast = Toast.makeText(AddNoteActivity.this, "标题不能为空", Toast.LENGTH_SHORT);
                        toast.show();
                    }else if(editTextContent.length() == 0){
                        Toast toast = Toast.makeText(AddNoteActivity.this,"内容不能为空",Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        self = 0;
                        String title = editTextTitle.getText().toString().trim();
                        String content = editTextContent.getText().toString().trim();
                        if(checkboxSelf.isChecked()){
                            self = 1;
                        }
                        //上传图片
                        Note note = new Note();
                        note.setImgList(imgList);
                        note.setUserId(userId);
                        note.setTitle(title);
                        note.setContent(content);
                        note.setLatitude(latitude);
                        note.setLongitude(longitude);
                        note.setSelf(self);
                        note.setLocation(location);

//                        Intent intent = getIntent();
//                        int tabId = intent.getIntExtra("tab", 0);
                        UploadFileTask task = new UploadFileTask(getApplicationContext(), note);
                        task.execute("http://" + ip + ":8080/MoJi/note/add");
                    }
                    break;
            }
        }
    }

    /**
     *  @author 春波
     *  @time 2019/12/4  14:57
     *  @Description：从相册选择照片
     */
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    /**
     *  @author 春波
     *  @time 2019/12/4  14:58
     *  @Description：拍摄照片
     */
    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";

        File photoFile = new File(fileDir, fileName);
        mTempPhotoPath = photoFile.getAbsolutePath();//获取照片路径
        imageUri = FileProvider7.getUriForFile(this, photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    /**
     *  @author 春波
     *  @time 2019/12/4  14:58
     *  @Description：权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    takePhoto();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_CHOOSE_PHOTO:
                    Uri uri = data.getData();
                    String filePath = FileUtil.getFilePathByUri(this, uri);

                    if (!TextUtils.isEmpty(filePath)) {
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                        //将照片显示在 ivImage上
                        insertPhoto(requestOptions1,filePath);
                    }
                    break;
                case RC_TAKE_PHOTO:
                    RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将图片显示在ivImage上
                    insertPhoto(requestOptions,mTempPhotoPath);
                    break;
            }
        }
    }

    private void insertPhoto(RequestOptions requestOptions, final String filePath){
        final LinearLayout layout = findViewById(R.id.layout_img);
        final RelativeLayout relativeLayout = new RelativeLayout(this);

        ImageView img = new ImageView(this);
        img.setId(i);
        Glide.with(this).load(filePath).apply(requestOptions).into(img);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(DensityUtil.dip2px(this,100),DensityUtil.dip2px(this,100));
        param1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(img,param1);

        ImageButton deleteImg = new ImageButton(this);
        deleteImg.setId(i);
        deleteImg.setBackgroundResource(R.mipmap.deleteimg);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayout.addView(deleteImg,param2);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(DensityUtil.dip2px(this,110),DensityUtil.dip2px(this,110));
        layout.addView(relativeLayout,param);
        imgList.add(filePath);
        //        Log.e("filepath=", filePath);

        //设置删除按钮的监听器
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();
                layout.removeView(relativeLayout);
                imgList.remove(filePath);
            }
        });
        i++;
    }

}
