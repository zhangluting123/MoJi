package cn.edu.hebtu.software.test.UploadAndDownload;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
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
 * @ProjectName MoJi
 * @PackageName：com.example.moji
 * @Description：
 * @time 2019/12/4 14:21
 */
public class UploadFileTask extends AsyncTask<String, Void, String> {
    private Context context;
    private Note note;

    public UploadFileTask(Context context, Note note) {
        this.context = context;
        this.note = note;
    }

    /**
     * 访问服务器，上传note信息，接收响应并返回
     */
    @Override
    protected String doInBackground(String... strings) {
        if(DetermineConnServer.isConnByHttp(context)){
            OkHttpClient client = new OkHttpClient();
            MediaType type = MediaType.parse("image/jpeg");
            List<String> imgList = note.getImgList();
            RequestBody[] body = new RequestBody[imgList.size()];
            //传参
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.ALTERNATIVE);
            builder
                    .addFormDataPart("userId", note.getUserId())
                    .addFormDataPart("title",note.getTitle())
                    .addFormDataPart("content",note.getContent())
                    .addFormDataPart("latitude",note.getLatitude() + "")
                    .addFormDataPart("longitude",note.getLongitude() + "")
                    .addFormDataPart("self",note.getSelf() + "")
                    .addFormDataPart("location",note.getLocation());
            //传图片
            for(int i = 0; i < imgList.size(); i++){
                File file = new File(imgList.get(i));
                body[i] = RequestBody.create(file, type);
                builder.addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name='file';filename=" + file.getName()),
                        body[i]
                );
            }
            RequestBody requestBody = builder.build();

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
            } finally {
                response.close();
            }
            return null;
        }else{
            return "ERROR";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if("ERROR".equals(s)){
            Toast.makeText(context, "未连接到服务器", Toast.LENGTH_SHORT).show();
        }else if("1".equals(s)){
            Toast.makeText(context,"发布成功",Toast.LENGTH_SHORT).show();
            Intent jumpToBMap = new Intent(context, MainActivity.class);
            context.startActivity(jumpToBMap);

        }else{
            Toast.makeText(context,"发布失败",Toast.LENGTH_SHORT).show();
        }
    }
}
