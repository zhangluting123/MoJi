package cn.edu.hebtu.software.test.UploadAndDownload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.LeftNavigation.MineActivity;
import cn.edu.hebtu.software.test.LeftNavigation.UserInfoActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.tools.utils.DeviceHelper.getApplication;

public class UploadUserMsg extends AsyncTask<String, Void, String> {
    private Context context;
    private User user;
    private boolean isChangeAvatar;

    public UploadUserMsg(Context context, User user, boolean isChangeAvatar) {
        this.context = context;
        this.user = user;
        this.isChangeAvatar = isChangeAvatar;
    }

    /**
     * 访问服务器，上传User信息，接收响应并返回
     */
    @Override
    protected String doInBackground(String... strings) {
        //判断是否连接到服务器
        boolean b = DetermineConnServer.isConnByHttp(context);
        if(b){
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody;
            //更改头像
            if(isChangeAvatar){
                File file = new File(user.getUserHeadImg());
                MediaType type = MediaType.parse("image/jpeg");
                RequestBody headBody = RequestBody.create(file, type);
                //传参
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.ALTERNATIVE)
                        .addFormDataPart("userId", user.getUserId())
                        .addFormDataPart("userName", user.getUserName())
                        .addFormDataPart("occupation", user.getOccupation())
                        .addFormDataPart("signature", user.getSignature())
                        .addFormDataPart("sex", user.getSex())
                        .addFormDataPart("file", file.getName(), headBody)
                        .build();
            }else {//不更改头像
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.ALTERNATIVE)
                        .addFormDataPart("userId", user.getUserId())
                        .addFormDataPart("userName", user.getUserName())
                        .addFormDataPart("occupation", user.getOccupation())
                        .addFormDataPart("signature", user.getSignature())
                        .addFormDataPart("sex", user.getSex())
                        .build();
            }

            Request request=new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                response.close();
            }
            return null;
        }else{
            return "ERRORNET";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if("OK".equals(s)){
            //更新全局变量
            final MyApplication data = (MyApplication) getApplication();
            String n = new File(user.getUserHeadImg()).getName();
            user.setUserHeadImg("avatar/" + n);
            data.getUser().setUserHeadImg(user.getUserHeadImg());
            data.getUser().setUserName(user.getUserName());
            data.getUser().setSignature(user.getSignature());
            data.getUser().setPhone(user.getPhone());
            data.getUser().setSex(user.getSex());
            data.getUser().setPassword(user.getPassword());
            data.getUser().setOccupation(user.getOccupation());
            data.getUser().setUserId(user.getUserId());

            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
            Activity activity = UserInfoActivity.getUserInfoActivity();
            Intent request =activity.getIntent();
            int tabId = request.getIntExtra("tab", 0);
            Intent response = new Intent(activity.getApplicationContext(), MineActivity.class);
            response.putExtra("tab",tabId);
            activity.startActivity(response);
            activity.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
            activity.finish();
        }else if("error".equals(s)){
            Toast.makeText(context, "更新失败！", Toast.LENGTH_SHORT).show();
        }else if("ERRORNET".equals(s)){
            Toast.makeText(context, "未连接到服务器", Toast.LENGTH_SHORT).show();
        }
    }
}
