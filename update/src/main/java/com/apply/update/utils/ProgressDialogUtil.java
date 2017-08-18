package com.apply.update.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.apply.update.R;
import com.apply.update.view.ProgressDialogView;


/**
 * Created by Administrator on 2017/8/14.
 */

public class ProgressDialogUtil {

    private Context context;
    private Dialog dialog;
    private ProgressDialogView dialogView;

    public ProgressDialogUtil(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(String text, boolean cancelable, boolean cancelableOutside, final DialogInterface.OnCancelListener cancelListener) {
        final String message = (text == null) ? context.getResources().getString(R.string.loading) : text;

        if (dialogView != null) {
            dialogView.stopAnim();
            dialogView = null;
        }

        dialogView = new ProgressDialogView(context);

        dialogView.setTextVisible(!TextUtils.isEmpty(message));
        dialogView.setText(message);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelableOutside);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (cancelListener != null) {
                    cancelListener.onCancel(dialogInterface);
                }
                if (dialogView != null) {
                    dialogView.stopAnim();
                    dialogView = null;
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (dialogView != null) {
                    dialogView.stopAnim();
                    dialogView = null;
                }
            }
        });
        dialog.setContentView(dialogView);
        if (dialog.isShowing()) {
            return;
        }
        dialog.show();
        dialogView.startAnim();
    }

    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.setCancelable(true);
        }
    }

}
