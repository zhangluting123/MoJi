package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.UploadAndDownload.UploadUserMsg;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.FileUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ProjectName:    MoJi
 * @Description:    个人资料界面
 * @Author:         张璐婷
 * @CreateDate:     2019/11/27 14:46
 * @Version:        1.0
 */
public class UserInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout changeHeader;
    private ImageView userHead;
    private EditText userName;
    private RadioGroup radioGroup;
    private RadioButton girl;
    private RadioButton boy;
    private EditText userJob;
    private EditText userSign;
    private Button btnSubmit;
    private String sex;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;
    private PopupWindow popupWindow;
    private View v;

    private String ip;

    //判断是否更换图片
    private String isfilePathChange;

    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;

    private String mTempPhotoPath;
    private Uri imageUri;
    private User user;
    private User currentUser;

    private static volatile Activity userInfoActivity;

    public static Activity getUserInfoActivity() {
        return userInfoActivity;
    }

    public static void setCurrentActivity(Activity userInfoActivity) {
        UserInfoActivity.userInfoActivity =  userInfoActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActivityManager.getInstance().addActivity(this);

        final MyApplication data = (MyApplication) getApplication();
        currentUser = data.getUser();

        user = new User();
        user.setUserId(currentUser.getUserId());
        user.setUserHeadImg(currentUser.getUserHeadImg());
        user.setOccupation(currentUser.getOccupation());
        user.setPassword(currentUser.getPassword());
        user.setSex(currentUser.getSex());
        user.setPhone(currentUser.getPhone());
        user.setSignature(currentUser.getSignature());
        user.setUserName(currentUser.getUserName());

        isfilePathChange = currentUser.getUserHeadImg();

        ip = data.getIp();

        mTempPhotoPath = user.getUserHeadImg();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));
        }

        getViews();
        registerListener();

        //点击返回按钮返回MineActivity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(UserInfoActivity.this,MineActivity.class);
                startActivity(response);
                finish();
            }
        });

        userName.setText(user.getUserName());
        userSign.setText(user.getSignature());
        userJob.setText(user.getOccupation());
        if("girl".equals(user.getSex())){
            girl.setSelected(true);
            girl.setChecked(true);
        }else if("boy".equals(user.getSex())){
            boy.setSelected(true);
            boy.setChecked(true);
        }
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + ip + ":8080/MoJi/"+user.getUserHeadImg()).apply(options).into(userHead);
    }


    private void getViews(){
        changeHeader = findViewById(R.id.ll_changeHeader);
        userHead = findViewById(R.id.iv_userHead);
        userName = findViewById(R.id.et_userName);
        radioGroup = findViewById(R.id.radioGroup);
        girl = findViewById(R.id.rb_girl);
        boy = findViewById(R.id.rb_boy);
        userJob = findViewById(R.id.et_userJob);
        userSign = findViewById(R.id.et_userSign);
        btnSubmit = findViewById(R.id.btnSubmit);
        toolbar = findViewById(R.id.info_toolbar);
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.popup_layout, null);
        btnTakePhoto = v.findViewById(R.id.take_photo);
        btnChooseFromAlbum = v.findViewById(R.id.choose_from_album);
    }

    private void registerListener() {
        CustomOnClickListener listener = new CustomOnClickListener();
        changeHeader.setOnClickListener(listener);
        btnTakePhoto.setOnClickListener(listener);
        btnChooseFromAlbum.setOnClickListener(listener);
        btnSubmit.setOnClickListener(listener);
    }

    public void showPopupWindow(){
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        Button button = v.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//关闭弹出窗口
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(v);
        LinearLayout parent = findViewById(R.id.ll_parent);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }

    /**
     * 判断单选按钮选择情况
     */
    private String judgeRadioButton(){
        if(girl.isChecked()){
            sex = "girl";
        }else{
            sex = "boy";
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                sex = radioButton.getText().toString();
            }
        });
        return sex;
    }

    class CustomOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_changeHeader:
                    showPopupWindow();
                    break;
                case R.id.take_photo:
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(UserInfoActivity.this, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.CAMERA}, RC_TAKE_PHOTO);
                    } else {
                        takePhoto();
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                        ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
                    } else {
                        //已授权，获取照片
                        choosePhoto();
                    }
                    popupWindow.dismiss();
                    break;
                case R.id.btnSubmit:
                    user.setUserName(userName.getText().toString());
                    user.setSignature(userSign.getText().toString());
                    user.setOccupation(userJob.getText().toString());
                    user.setSex(judgeRadioButton());
                    user.setUserHeadImg(mTempPhotoPath);
                    boolean is = true;
                    if(mTempPhotoPath.equals(isfilePathChange)){
                        is = false;
                    }

                    UploadUserMsg task = new UploadUserMsg(getApplicationContext(), user, is);
                    task.execute("http://"+ ip +":8080/MoJi/user/changeMsg");

                    break;
            }

        }
    }
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }


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
     * 权限申请结果回调
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
                    mTempPhotoPath = filePath;

                    if (!TextUtils.isEmpty(filePath)) {
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop();
                        //将相册图片显示在 userHead上
                        Glide.with(this).load(filePath).apply(requestOptions1).into(userHead);
                        user.setUserHeadImg(filePath);
                    }
                    break;
                case RC_TAKE_PHOTO:
                    RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop()
                            ;
                    //将照片显示在userHead上
                    Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(userHead);
                    break;
            }
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
    }

}
