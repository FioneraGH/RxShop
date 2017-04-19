package com.centling.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centling.R;

/**
 * ShowDialog
 * Created by fionera on 15-11-12.
 */

public class ShowDialog {

    public static void showConfirmDialog(Context context, String title, String message,
                                         View.OnClickListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.ProcessDialog)
                .setCancelable(false).create();
        if (((Activity) context).isFinishing()) {
            return;
        }
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_confirm);
        TextView tvTitle = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_confirm_dialog_title);
        TextView tvMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_confirm_dialog_msg);
        tvTitle.setText(title);
        tvMsg.setText(message);
        alertDialog.getWindow().findViewById(R.id.bt_confirm_dialog_ok)
                .setOnClickListener(view -> {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                    alertDialog.dismiss();
                });
    }

    public static void showSelectDialog(Context context, String title, String message,
                                        String msgSub, View.OnClickListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.ProcessDialog)
                .setCancelable(false).create();
        if (((Activity) context).isFinishing()) {
            return;
        }
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_alert);
        TextView tvTitle = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_edit_dialog_title);
        TextView tvMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_select_dialog_msg);
        TextView tvSubMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_select_dialog_submsg);
        tvTitle.setText(title);
        tvMsg.setText(message);
        tvSubMsg.setText(msgSub);
        if (TextUtils.isEmpty(message)) {
            tvMsg.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(msgSub)) {
            tvSubMsg.setVisibility(View.GONE);
        }
        alertDialog.getWindow().findViewById(R.id.tv_select_dialog_cancel)
                .setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.getWindow().findViewById(R.id.tv_select_dialog_ok).setOnClickListener(v -> {
            listener.onClick(v);
            alertDialog.dismiss();
        });
    }

    //强制性Dialog
    public static void showForceSelectDialog(Context context, String title, String message,
                                             String msgSub, View.OnClickListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.ProcessDialog)
                .setCancelable(false).create();
        if (((Activity) context).isFinishing()) {
            return;
        }
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_alert);
        TextView tvTitle = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_edit_dialog_title);
        TextView tvMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_select_dialog_msg);
        TextView tvSubMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_select_dialog_submsg);
        TextView cancle = (TextView) alertDialog.findViewById(R.id.tv_select_dialog_cancel);
        TextView updateTv = (TextView) alertDialog.findViewById(R.id.tv_select_dialog_ok);
        updateTv.setText("升级");
        cancle.setVisibility(View.GONE);
        tvTitle.setText(title);
        tvMsg.setText(message);
        tvSubMsg.setText(msgSub);
        if (TextUtils.isEmpty(message)) {
            tvMsg.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(msgSub)) {
            tvSubMsg.setVisibility(View.GONE);
        }
        alertDialog.getWindow().findViewById(R.id.tv_select_dialog_cancel)
                .setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.getWindow().findViewById(R.id.tv_select_dialog_ok).setOnClickListener(v -> {
            listener.onClick(v);
            alertDialog.dismiss();
        });
    }

    public static void showEditDialog(Context context, String title, String message,
                                      View.OnClickListener listener) {
        showEditDialog(context, title, message, listener, new String[]{"请输入原因"});
    }

    public static void showEditDialog(Context context, String title, String message,
                                      View.OnClickListener listener, String[] hint) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.ProcessDialog)
                .setCancelable(false).create();
        if (((Activity) context).isFinishing()) {
            return;
        }
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.setContentView(R.layout.dialog_edit);
        TextView tvTitle = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_edit_dialog_title);
        TextView tvMsg = (TextView) alertDialog.getWindow()
                .findViewById(R.id.tv_edit_dialog_msg);
        LinearLayout llContainer = (LinearLayout) alertDialog.getWindow()
                .findViewById(R.id.ll_edit_dialog_container);

        for (int i = 0; i < hint.length; i++) {
            llContainer.getChildAt(i).setVisibility(View.VISIBLE);
            ((EditText) llContainer.getChildAt(i)).setHint(hint[i]);
            //输入数字
            if (hint[i].contains(".")) {
                ((EditText) llContainer.getChildAt(i)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            if (hint[i].contains("数量")) {
                ((EditText) llContainer.getChildAt(i)).setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
        tvTitle.setText(title);
        tvMsg.setText(message);
        if (TextUtils.isEmpty(message)) {
            tvMsg.setVisibility(View.GONE);
        }
        alertDialog.getWindow().findViewById(R.id.tv_edit_dialog_cancel)
                .setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.getWindow().findViewById(R.id.tv_edit_dialog_ok).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hint.length; i++) {
                sb.append(((EditText) llContainer.getChildAt(i)).getText().toString()).append("!@#");
            }
            v.setTag(sb.toString());
            listener.onClick(v);
            alertDialog.dismiss();
        });
    }
}
