package cn.edu.hebtu.software.test.UploadAndDownload;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.FileRequestBody;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author 春波
 * @ProjectName sendVideo
 * @PackageName：com.example.sendvideo
 * @Description：
 * @time 2020/4/17 15:00
 */
public class UploadVideoTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private Video video;
    private PopupWindow popupWindow;
    private View view;
    private TextView tvUploadProgress;
    private ProgressBar progressBar;

    public UploadVideoTask(Context context, Video video) {
        this.context = context;
        this.video = video;
    }

    /**
     *  该方法会在后台任务开始执行前调用，并在主线程执行
     *  用于进行一些界面上的初始化操作，比如显示一个进度条对话框等
     */
    @Override
    protected void onPreExecute() {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = 0.5f;
        ((Activity)context).getWindow().setAttributes(lp);
        //设置视图对象
        view = LayoutInflater.from(context).inflate(R.layout.popup_upload_progress, null);
        //设置父视图
        View rootView = LayoutInflater.from(context).inflate(R.layout.activity_upload_video, null);
        //新建一个popupWindow
        popupWindow = new PopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
        //获取popupWindow的控件并赋值
        tvUploadProgress = view.findViewById(R.id.tv_upload_progress);
        progressBar = view.findViewById(R.id.video_upload_progressBar);
        tvUploadProgress.setText("0%");
        progressBar.setProgress(0);

    }

    /**
     * 访问服务器，上传video信息，接收响应并返回
     * 这个方法在子线程中运行，应该在这里处理所有的耗时任务
     * 任务执行结束，可以通过 return语句来返回任务执行的结果
     * 这个方法不能执行UI操作，如果需要进行UI更新操作，如更新任务进度，可以调用 publishProgress(Progress…)来完成
     */
    @Override
    protected String doInBackground(String... strings) {
        progressBar.setProgress(0);
        if(DetermineConnServer.isConnByHttp(context)){
            OkHttpClient client = new OkHttpClient();
            //以二进制流方式
            MediaType type = MediaType.parse("application/octet-stream");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            //传参
            builder.addFormDataPart("videoId", video.getVideoId())
                    .addFormDataPart("videoTitle",video.getTitle())
                    .addFormDataPart("videoContent",video.getContent())
                    .addFormDataPart("videoPath",video.getPath())
                    .addFormDataPart("videoDuration",video.getDuration())
                    .addFormDataPart("videoSize",video.getSize())
                    .addFormDataPart("userId",video.getUser().getUserId())
                    .addFormDataPart("videoTag",video.getTag());
            //传视频
            File file = new File(video.getPath());
            builder.addPart(Headers.of("Content-Disposition","form-data; name='file';filename=" + file.getName()),
                    RequestBody.create(file, type)
            );
            //自定义的类继承RequestBody
            FileRequestBody fileRequestBody = new FileRequestBody(builder.build(), new FileRequestBody.LoadingListener() {
                @Override
                public void onProgress(long currentLength, long contentLength) {
                    //获取上传的比例,实现监听
                    double i = (currentLength * 100.0) / contentLength;
                    publishProgress((int)i);//调用此方法会调用onProgressUpdate方法
                }
            });
            Request request=new Request.Builder()
                    .url(strings[0])
                    .post(fileRequestBody)
                    .build();

            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
            return null;
        }else{
            return "ERROR";
        }

    }

    /**
     *  当在doInBackground中调用publishProgress(Progress…)后，这个方法就会马上被调用
     *  方法中携带的参数是后台任务传过来的，该方法在主线程运行，所以可以进行UI更新
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        tvUploadProgress.setText(values[0] + "%");
        progressBar.setProgress(values[0]);
    }

    /**
     *  当 doInBackground(Params...)执行完毕，并通过 return进行返回时，这个方法就会马上被调用
     *  返回的数据会被作为该方法的参数传递过来
     *  该方法是在主线程中运行
     *  可以利用返回的数据进行UI更新操作，如提醒任务执行的结果或关闭掉进度条对话框等
     */
    @Override
    protected void onPostExecute(String s) {
        if("ERROR".equals(s)){
            Toast.makeText(context, "未连接到服务器", Toast.LENGTH_SHORT).show();
        }else if("SUCCESS".equals(s)){
            Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
//            Intent jumpToBMap = new Intent(context, UploadVideoActivity.class);
//            context.startActivity(jumpToBMap);
            ((Activity)context).finish();

        }else{
            Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();
        }
        //关闭popupWindow
        popupWindow.dismiss();
        //恢复窗口颜色
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = 1f;
        ((Activity)context).getWindow().setAttributes(lp);
    }
}
