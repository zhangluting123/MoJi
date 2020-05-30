package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.ChatIM.ChatActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.SQLiteUtil;


/**
 * @ProjectName:    MoJi
 * @Description:    私信列表页
 * @Author:         王佳成
 * @CreateDate:     2020/4/11 11:27
 * @Version:        1.0
 */
public class MyMailMessageFragment extends EaseConversationListFragment {
    private SQLiteUtil sqLiteUtil;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         //        View view = inflater.inflate(R.layout.fragment_mymail_message_layout, container, false);
        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });

         sqLiteUtil = new SQLiteUtil(getActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //监听，有新消息，刷新UI
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //收到消息
            for(int i = 0;i < list.size();i++){
                User user = new User();
                String id = list.get(i).getStringAttribute(EaseConstant.EXTRA_USER_ID,EaseConstant.NO_VALUE);
                String name = list.get(i).getStringAttribute(EaseConstant.EXTRA_USER_NAME,EaseConstant.NO_VALUE);
                String avatar = list.get(i).getStringAttribute(EaseConstant.EXTRA_USER_AVATAR,EaseConstant.NO_VALUE);
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
                refresh();
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }

    };
}
