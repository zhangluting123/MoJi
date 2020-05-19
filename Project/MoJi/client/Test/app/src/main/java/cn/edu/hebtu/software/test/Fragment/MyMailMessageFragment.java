package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.ChatIM.ChatActivity;
import cn.edu.hebtu.software.test.R;


/**
 * @ProjectName:    MoJi
 * @Description:    私信列表页
 * @Author:         王佳成
 * @CreateDate:     2020/4/11 11:27
 * @Version:        1.0
 */
public class MyMailMessageFragment extends EaseConversationListFragment {

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

        return view;
    }
}
