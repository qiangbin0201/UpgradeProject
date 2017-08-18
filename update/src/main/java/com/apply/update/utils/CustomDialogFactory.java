package com.apply.update.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.apply.update.R;


/**
 * Created by Administrator on 2017/8/14.
 */

public class CustomDialogFactory {

    /**
     * 弹出确认框
     */
    public static AlertDialog showConfirmDialog(Context mContext, boolean cancelable, View contentView,
                                                CharSequence positiveButtonText, CharSequence negativeButtonText,
                                                final OnDialogClickListener positiveClickListener,
                                                final OnDialogClickListener negativeClickListener,
                                                final OnDialogDismissListener dismissListener,
                                                final boolean positiveAutoDismiss,
                                                final boolean negativeAutoDismiss) {

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(cancelable);
        alertDialog.setCancelable(cancelable);
        alertDialog.getWindow().setContentView(R.layout.dialog_style_confirm_view);
        //以下两句是为了解决，弹出框需要输入时，能够弹出输入法
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ViewGroup containerView = (ViewGroup) alertDialog.findViewById(R.id.fl_container);
        TextView mTextViewNegative = (TextView) alertDialog.findViewById(R.id.btn_confirm_left);
        TextView mTextViewPositive = (TextView) alertDialog.findViewById(R.id.btn_confirm_right);

        containerView.addView(contentView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        mTextViewPositive.setText(positiveButtonText);
        mTextViewNegative.setText(negativeButtonText);

        mTextViewPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveAutoDismiss)
                    alertDialog.dismiss();
                if (positiveClickListener != null) {
                    positiveClickListener.onClick(alertDialog);
                }
            }
        });

//        mTextViewPositive.setOnClickListener(l -> {
//            if (positiveAutoDismiss)
//                alertDialog.dismiss();
//            if (positiveClickListener != null) {
//                positiveClickListener.onClick(alertDialog);
//            }
//        });

        mTextViewNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeAutoDismiss)
                    alertDialog.dismiss();
                if (negativeClickListener != null) {
                    negativeClickListener.onClick(alertDialog);
                }
            }
        });

//        mTextViewNegative.setOnClickListener(l -> {
//            if (negativeAutoDismiss)
//                alertDialog.dismiss();
//            if (negativeClickListener != null) {
//                negativeClickListener.onClick(alertDialog);
//            }
//        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dismissListener != null) {
                    dismissListener.onDismiss(alertDialog);
                }
            }
        });

//        alertDialog.setOnDismissListener(l -> {
//            if (dismissListener != null) {
//                dismissListener.onDismiss(alertDialog);
//            }
//        });

        return alertDialog;
    }
}
