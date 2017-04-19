package com.centling.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.centling.R;

import java.util.Locale;

public class ProcessDialog
        extends AlertDialog.Builder {
    private String _title = "请稍候";
    private AlertDialog alertDialog;
    private Context context;

    public ProcessDialog(Context context) {
        super(context, R.style.ProcessDialog);
        this.context = context;
    }

    /**
     * 取消dialog
     */
    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            try {
                alertDialog.dismiss();
            }catch (Exception ignored){

            }
        }
    }

    /**
     * 展示dialog
     * @param cancel cancel
     */
    public void showDialog(boolean cancel) {
        alertDialog = super.show();
        View view = View.inflate(context,R.layout.layout_process_dialog, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_process_title);
        Window window = alertDialog.getWindow();
        if(window != null){
            window.setContentView(view);
        }
        alertDialog.setCancelable(cancel);
        tvTitle.setText(String.format(Locale.CHINA, "%s...", _title));
    }

    public ProcessDialog setTitle(String text) {
        _title = text;
        return this;
    }
}
