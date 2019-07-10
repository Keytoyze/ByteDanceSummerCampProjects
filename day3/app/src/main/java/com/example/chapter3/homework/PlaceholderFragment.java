package com.example.chapter3.homework;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceholderFragment extends Fragment {

    @BindView(R.id.loading_view)
    LottieAnimationView loadingView;
    @BindView(R.id.friends_list)
    ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // DONE ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        return inflater.inflate(R.layout.fragment_placeholder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        loadingView.playAnimation();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 这里会在 5s 后执行
                    // generate friends.
                    Random random = new Random();
                    List<String> friends = new ArrayList<>(20);
                    for (int i = 0; i < 20; i++) {
                        friends.add(String.valueOf(random.nextInt()));
                    }
                    listView.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                            android.R.layout.simple_list_item_1, friends));
                    listView.setVisibility(View.VISIBLE);
                    // DONE ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1)
                            .setDuration(1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            loadingView.setAlpha(1 - animation.getAnimatedFraction());
                            listView.setAlpha(animation.getAnimatedFraction());
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadingView.setVisibility(View.GONE);
                        }
                    });
                    animator.start();
                }
            }, 5000);
        }
    }
}
