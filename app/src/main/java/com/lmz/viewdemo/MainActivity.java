package com.lmz.viewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lmz.viewdemo.Utils.Contants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.btnScaleSearchBar)
    Button btnScaleSearchBar;
    @BindView(R.id.btnNestedScrolling)
    Button btnNestedScrolling;
    @BindView(R.id.btnSearchNestedViewPager)
    Button btnSearchNestedViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {

    }

    private void initListener() {

    }

    @OnClick({R.id.btnScaleSearchBar,R.id.btnNestedScrolling,R.id.btnSearchNestedViewPager})
    public void onClickView(View v) {
        switch (v.getId()){

            case R.id.btnScaleSearchBar:
                Intent intent = new Intent(MainActivity.this,ScaleSearchBar2Activity.class);
                startActivity(intent);
                break;
            case R.id.btnNestedScrolling:
                intent = new Intent(MainActivity.this,NestedViewPagerDemoActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSearchNestedViewPager:
                intent = new Intent(MainActivity.this,ScaleSearchNestedViewPagerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
