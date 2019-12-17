package cn.edu.hebtu.software.test.Quotation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Fragment.QuotationFragment;
import cn.edu.hebtu.software.test.R;

public class BottomDialogFragment extends DialogFragment {

    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;

    public Dialog dialog;
    public ImageView imageView;
    private DialogInterface.OnDismissListener mOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_card, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView textView = view.findViewById(R.id.content);
        imageView = view.findViewById(R.id.add);
        // 手势监听
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 按下
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
                        break;
                    // 移动
                    case MotionEvent.ACTION_MOVE:
                        mCurrentPosX = event.getX();
                        mCurrentPosY = event.getY();
                        if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 10)
                            Log.e("", "向右");
                        else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 10)
                            Log.e("", "向左");
                        else if (mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 20) {
                            Log.e("", "向下");
                            dismiss();
                        }
                        else if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 20) {
                            Log.e("", "向上");
                            StaticX staticX = new StaticX();
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                            QuotationFragment mainActivity = new QuotationFragment();
                            Log.e("staticX.getAddIndex()",staticX.getAddIndex()+"");

                            java.util.Random random=new java.util.Random();// 定义随机类
                            int result=random.nextInt(10);
                            if(result==0){
                                result = 1;
                            }
                            mainActivity.getmFragmentCardAdapter().addCardFragment(new CardFragment(staticX.getAddIndex(),"images/timg"+result+".jpg"));
                            staticX.listAdd(new DataBean(staticX.getAddIndex(),"title","content","images/timg"+result+".jpg"));
                            mainActivity.getAdapter().fresh();
                            mainActivity.getmViewPager().setCurrentItem(staticX.getList().size()-1);

                            new Link(1,"images/timg"+result+".jpg");
                            dismiss();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //做一些弹框的初始化，以及创建一个弹框
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnClickListener != null) {
            mOnClickListener.onDismiss(dialog);
        }
        if(mOnClickListener == null){
            StaticX staticX = new StaticX();
            staticX.setTagup(1);
        }
    }

    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setWindowAnimations(R.style.FragmentDialogAnimation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setCancelable(true);
        }
    }
}
