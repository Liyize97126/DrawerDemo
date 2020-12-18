package com.yz.drawerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.yz.drawerlibrary.DrawerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDv.getState() == ViewState.CLOSE) {
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

        mAdapter.setDataList(Arrays.asList(getResources().getStringArray(R.array.list_data)));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && mDv.getState() != ViewState.CLOSE){
            mDv.changeState(ViewState.CLOSE);
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
