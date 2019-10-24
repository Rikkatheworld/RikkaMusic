package com.rikkathewrold.rikkamusic.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rikkathewrold.rikkamusic.App;
import com.rikkathewrold.rikkamusic.R;
import com.rikkathewrold.rikkamusic.base.BaseRikkaMusicDialog;

public class RikkaMusicDialog extends BaseRikkaMusicDialog {
    private static final String TAG = "RikkaMusicDialog";

    private Context mContext;
    private TextView tvMsg;
    private TextView tvConfirm, tvCancel;
    private DialogInterface.OnClickListener cancelListener, confirmListener;

    private RikkaMusicDialog(@NonNull Context context) {
        this(context, R.style.RikkaBaseDialog);
    }

    private RikkaMusicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_rikka_commom_dialog, null);
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        initView(view);
        initListener();
        setContentView(view);
    }


    private void initView(RelativeLayout view) {
        tvMsg = view.findViewById(R.id.tv_message);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel = view.findViewById(R.id.tv_cancel);
    }

    private void setMsg(String msg) {
        tvMsg.setText(msg);
        tvMsg.setVisibility(View.VISIBLE);
    }

    private void setCancelListener(OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
        tvCancel.setVisibility(View.VISIBLE);
    }

    private void setConfirmListener(OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
        tvConfirm.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        tvCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
            }
        });

        tvConfirm.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onClick(RikkaMusicDialog.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
    }

    public static class Builder {
        RikkaMusicDialog dialog;
        private TextView tvConfirm;
        private TextView tvCancel;

        public Builder(Context context) {
            dialog = new RikkaMusicDialog(context);
            tvCancel = dialog.tvCancel;
            tvConfirm = dialog.tvConfirm;
        }

        public Builder setMsg(int resId) {
            String res = App.getContext().getString(resId);
            return setMsg(res);
        }

        Builder setMsg(String msg) {
            dialog.setMsg(msg);
            return this;
        }

        public Builder setPositiveText(int resId) {
            String res = App.getContext().getString(resId);
            return setPositiveText(res);
        }

        Builder setPositiveText(String text) {
            tvConfirm.setText(text);
            return this;
        }

        public Builder setNegativeText(int resId) {
            String res = App.getContext().getString(resId);
            return setNegativeText(res);
        }

        Builder setNegativeText(String text) {
            tvCancel.setText(text);
            return this;
        }

        public Builder setNegativeClickListener(DialogInterface.OnClickListener listener) {
            dialog.setCancelListener(listener);
            return this;
        }

        public Builder setPositiveClickListener(DialogInterface.OnClickListener listener) {
            dialog.setConfirmListener(listener);
            return this;
        }

        public Builder setPositiveTextColor(int color) {
            tvConfirm.setTextColor(color);
            return this;
        }

        public Builder setNegativeClickListener(int color) {
            tvCancel.setTextColor(color);
            return this;
        }

        public RikkaMusicDialog create() {
            return dialog;
        }
    }
}
