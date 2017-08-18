package com.apply.update.view;

import android.content.Context;
import android.content.DialogInterface;

import com.apply.update.utils.ProgressDialogUtil;
import com.apply.update.utils.ToastUtil;


/**
 * Created by Administrator on 2017/8/14.
 */

public class UpgradeView {

    private static UpgradeView mUpgradeView;

    public static UpgradeView getInstance(){
        if(mUpgradeView == null){
            mUpgradeView = new UpgradeView();
        }
        return mUpgradeView;
    }

    private ProgressDialogUtil mLoadingDialog;
    /**
     * 以toast形式显示消息
     *
     * @param message
     */
    public void showToast(Context context, String message) {
        ToastUtil.show(context, message);
    }

    public void showLoadingDialog(Context context, String message, boolean cancelable) {
        ToastUtil.cancelToast();
        showLoadingDialog(context, message, cancelable, false);
    }

    /**
     * 获取 点击焦点外可消失的等待框
     */
    public void showLoadingDialog(Context context, String message, boolean cancelable, boolean cancelableOutside) {
        dismissLoadingDialog();
//        if (context.isFinishing()) return;

        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialogUtil(context);
        }

        mLoadingDialog.show(message, cancelable, cancelableOutside, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onLoadingDialogCanceled();
            }
        });
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.hide();
            mLoadingDialog = null;
        }
    }

    protected void onLoadingDialogCanceled() {
        mLoadingDialog = null;
    }



}
