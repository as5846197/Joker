package com.example.zcy.joker.jokers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.zcy.joker.base.BaseActivity;
import com.example.zcy.joker.jokers.data.JokerModel;
import com.example.zcy.joker.R;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zcy on 2016/5/3.
 */
public class JokerActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        JokerFragment jokerFragment = (JokerFragment) getSupportFragmentManager().findFragmentById(R.id.frame_content);
        if (jokerFragment == null) {
            jokerFragment = JokerFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_content, jokerFragment);
            transaction.commit();
        }
        new JokerPresenter(new JokerModel(this),jokerFragment,this);
    }
}
