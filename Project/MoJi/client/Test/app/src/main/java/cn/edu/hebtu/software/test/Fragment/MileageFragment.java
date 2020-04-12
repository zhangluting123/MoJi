package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName:    MoJi
 * @Description:    远行界面
 * @Author:         邸祯策
 * @CreateDate:     2020/4/10 15:39
 * @Version:        1.0
 */
public class MileageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mileage_layout,container,false);
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("远行");

        return  view;
    }



}