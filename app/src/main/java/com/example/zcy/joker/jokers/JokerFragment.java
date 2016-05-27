package com.example.zcy.joker.jokers;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zcy.joker.view.LoadMoreRecycleView;
import com.example.zcy.joker.base.BaseActivity;
import com.example.zcy.joker.base.BaseFragment;
import com.example.zcy.joker.jokers.data.JokerEntity;
import com.example.zcy.joker.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zcy on 2016/5/3.
 */
public class JokerFragment extends BaseFragment implements JokerContract.View, SwipeRefreshLayout.OnRefreshListener {

    private JokerContract.Presenter mPresenter;
    private BaseActivity mActivity;
    private LinearLayoutManager linearLayoutManager;
    private List<JokerEntity> jokersList;
    public JokerAdapter mAdapter;

    @BindView(R.id.rv_joker_list)
    LoadMoreRecycleView list;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.loading_view)
    View loadingView;

    @BindView(R.id.error_view)
    View errorView;

    @BindView(R.id.content_view)
    CoordinatorLayout contentView;

    @BindView(R.id.fab_turn_to_up)
    FloatingActionButton turnToUpView;


    public static JokerFragment newInstance() {
        JokerFragment fragment = new JokerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_joker, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(mActivity);
        list.setLayoutManager(linearLayoutManager);
        list.setLoadMore(()->mPresenter.getMoreJokersData());
        list.setTurntoUpViewListener(new LoadMoreRecycleView.ShowOrHideTurnToUpViewListener() {
            @Override
            public void hide() {
                turnToUpView.setVisibility(View.GONE);
            }

            @Override
            public void show() {
                turnToUpView.setVisibility(View.VISIBLE);
            }
        });
        mAdapter = new JokerAdapter();
        refreshLayout.setOnRefreshListener(this);

        list.setAdapter(mAdapter);
        RxView.clicks(turnToUpView).subscribe(this::onUpViewClicked);
    }

    @Override
    public void setPresenter(@Nonnull JokerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showJokers(List<JokerEntity> jokers) {
        setData(jokers);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }



    @Override
    public void showLoadMoreFailed() {
        showSnackMsg(contentView,"加载失败");
    }

    @Override
    public void showLoadMoreSuccess(@Nonnull final List<JokerEntity> jokers) {
        setData(jokers);
    }

    @Override
    public void hideRefreshLayout() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        showSnackMsg(contentView,"刷新成功");
    }

    @Override
    public void showRefreshFailed() {
        showSnackMsg(contentView,"刷新失败");
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshJokersData();
    }


    public class JokerAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_joker, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.contentView.setText(jokersList.get(position).content);
            holder.timeView.setText(jokersList.get(position).updatetime);
        }

        @Override
        public int getItemCount() {
            return jokersList == null ? 0 : jokersList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeView;
        TextView contentView;

        protected ViewHolder(View view) {
            super(view);
            timeView = (TextView) view.findViewById(R.id.tv_time);
            contentView = (TextView) view.findViewById(R.id.tv_content);
        }
    }

    private void setData(List<JokerEntity> jokers) {
        this.jokersList = jokers;
        mAdapter.notifyDataSetChanged();
    }

    private void onUpViewClicked(Void v){
        linearLayoutManager.scrollToPosition(0);
    }


}
