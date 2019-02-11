package com.example.abc.socketapp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class MyLayout extends LinearLayout {

    private ViewTarget<MyLayout, Drawable> viewTarget;

    public MyLayout(Context context) {
        this(context, null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewTarget=new ViewTarget<MyLayout, Drawable>(this) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                MyLayout myLayout=getView();
                myLayout.setBackgroundDrawable(resource);
            }
        };
    }
    public ViewTarget<MyLayout,Drawable>getTarget(){
        return viewTarget;
    }
}
