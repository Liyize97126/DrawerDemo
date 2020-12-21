package com.yz.drawerdemo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yz.drawerlibrary.DrawerView;
import com.yz.drawerlibrary.DrawerViewContainer;
import com.yz.drawerlibrary.ViewState;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button mBtn;
    @BindView(R.id.dv)
    DrawerView mDv;
    @BindView(R.id.rv)
    RecyclerView mRv;
    private MyAdapter mAdapter;
    private int number = 0;
    @BindView(R.id.drw)
    DrawerViewContainer mDrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDv.getState() == ViewState.CLOSE) {
                    number++;
                    mAdapter.clear();
                    if(number%2 == 1){
                        mAdapter.setDataList(Arrays.asList(getResources().getStringArray(R.array.list_data)));
                    } else {
                        mAdapter.setDataList(Arrays.asList(getResources().getStringArray(R.array.list_data2)));
                    }
                    //切换成悬浮状态（另：也可以设置成全屏(ViewState.FULL)状态）
                    mDv.changeState(ViewState.HOVER);
                } else {
                    //切换成关闭状态
                    mDv.changeState(ViewState.CLOSE);
                }
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDv.changeState(ViewState.CLOSE);
            }
        });

        mRv.setAdapter(mAdapter = new MyAdapter(this, this));
        mRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDv.getState() != ViewState.CLOSE) {
            mDv.changeState(ViewState.CLOSE);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
