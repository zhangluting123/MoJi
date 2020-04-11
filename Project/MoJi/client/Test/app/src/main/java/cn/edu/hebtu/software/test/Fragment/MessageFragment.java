package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName:    MoJi
 * @Description:    私信列表页
 * @Author:         王佳成
 * @CreateDate:     2020/4/11 11:27
 * @Version:        1.0
 */
public class MessageFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_layout, container, false);


        return view;
    }
}
