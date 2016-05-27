package com.example.zcy.joker.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhangcaoyang on 16/5/25.
 */
public class LoadMoreRecycleView extends RecyclerView {

    private LoadMore loadMore;
    private ShowOrHideTurnToUpViewListener turnToUpListener;

    public LoadMoreRecycleView(Context context) {
        super(context);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        super.setOnScrollListener(listener);

    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        int totalItemCount = getLayoutManager().getItemCount();
        if (totalItemCount - lastVisibleItem <= 4 && dy > 0) {
            if (loadMore != null) {
                loadMore.doLoadMore();
            }
            if (totalItemCount > 20 && turnToUpListener != null) {
                turnToUpListener.show();
            } else if (turnToUpListener != null) {
                turnToUpListener.hide();
            }
        }
    }

    public interface LoadMore {
        void doLoadMore();
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public interface ShowOrHideTurnToUpViewListener {
        void hide();

        void show();
    }

    public void setTurntoUpViewListener(ShowOrHideTurnToUpViewListener listener) {
        this.turnToUpListener = listener;
    }

}
