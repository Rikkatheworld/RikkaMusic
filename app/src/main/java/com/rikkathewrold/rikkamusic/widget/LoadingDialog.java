package com.rikkathewrold.rikkamusic.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;


public class LoadingDialog extends Dialog {
    private static final String TAG = "LoadingDialog";

    private Animation animation = AnimationUtils.loadAnimation(App.getContext(), R.anim.dialog_load_anim);
    private String msg;

    public LoadingDialog(@NonNull Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        new Handler().postDelayed(() -> setDialogCancelable(true), 3000);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setDialogCancelable(false);
    }

    private void setDialogCancelable(boolean cancelable) {
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
