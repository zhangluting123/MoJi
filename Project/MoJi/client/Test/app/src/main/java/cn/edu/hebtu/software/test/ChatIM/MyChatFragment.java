package cn.edu.hebtu.software.test.ChatIM;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import java.util.List;

import androidx.annotation.Nullable;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.SQLiteUtil;

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    private SQLiteUtil sqLiteUtil;
    private MyApplication data;
    private String toChatUserName;

    public MyChatFragment(String toChatUserName) {
        this.toChatUserName = toChatUserName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (MyApplication)getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        super.setUpView();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        //设置消息属性
        message.setAttribute(EaseConstant.EXTRA_USER_ID,data.getUser().getUserId());
        message.setAttribute(EaseConstant.EXTRA_USER_NAME, data.getUser().getUserName());
        message.setAttribute(EaseConstant.EXTRA_USER_AVATAR,data.getUser().getUserHeadImg());

    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

    //接收消息
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息
        for(int i = 0;i < messages.size();i++){
            User user = new User();
            String id = messages.get(i).getStringAttribute(EaseConstant.EXTRA_USER_ID,EaseConstant.NO_VALUE);
            String name = messages.get(i).getStringAttribute(EaseConstant.EXTRA_USER_NAME,EaseConstant.NO_VALUE);
            String avatar = messages.get(i).getStringAttribute(EaseConstant.EXTRA_USER_AVATAR,EaseConstant.NO_VALUE);
            user.setUserId(id);
            user.setUserName(name);
            user.setUserHeadImg(avatar);
            if(id.equals(EaseConstant.NO_VALUE)){
                //扩展消息没有数据
            }else{
                //扩展消息有数据，ID作为唯一主键，进行数据库存储，有的话更新，没有的话添加
                if(sqLiteUtil.queryExistUser(id)){
                    sqLiteUtil.update(user);
                }else{
                    sqLiteUtil.insert(user);
                }

            }
        }
        super.onMessageReceived(messages);
    }
}
