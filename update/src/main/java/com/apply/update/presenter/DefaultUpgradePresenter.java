package com.apply.update.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.apply.update.BaseInfo;
import com.apply.update.R;
import com.apply.update.model.UpdateRelease;
import com.apply.update.service.TinkerUpdateService;
import com.apply.update.service.UpdateService;
import com.apply.update.utils.CustomDialogFactory;
import com.apply.update.utils.FileUtils;
import com.apply.update.utils.OnDialogClickListener;
import com.apply.update.view.UpgradeView;

import java.io.File;

/**
 * Created by Administrator on 2017/8/14.
 */

public class DefaultUpgradePresenter implements UpgradePresenter {

    public static Context mContext;
    private boolean mIgnoreMessage;     //对于在更新过程中出现的消息，是否忽略
    private boolean mShowProgress;      //是否显示检查进度条
    private boolean mForceUpdate;

    public DefaultUpgradePresenter(Context context, boolean ignoreMessage, boolean showProgress, boolean forceUpdate){
        mContext = context;
        mIgnoreMessage = ignoreMessage;
        mShowProgress = showProgress;
        mForceUpdate = forceUpdate;
    }

    private void showMessage(String message){
        if(!mIgnoreMessage){
            UpgradeView.getInstance().showToast(mContext, message);
        }
    }

    @Override
    public void showProgressOnChecking() {
        if(mShowProgress){
            UpgradeView.getInstance().showLoadingDialog(mContext, "", true);
        }

    }

    @Override
    public void hideProgressOnChecking() {
        if(mShowProgress){
            UpgradeView.getInstance().dismissLoadingDialog();
        }

    }

    @Override
    public void onCheckUpgraded(final UpdateRelease updateResult, int tinkerCode) {
        if (updateResult != null && !(updateResult.getCode() > BaseInfo.versionCode)) {
            showMessage("已是最新版本无需更新");
            //热更新
            if(updateResult.getHotfix() != null){
                if((updateResult.getHotfix().getCode() > tinkerCode)){
                    Intent updateIntent = new Intent(mContext, TinkerUpdateService.class);
                    updateIntent.putExtra("tinker_url", updateResult.getHotfix().getDown_url());
                    mContext.startService(updateIntent);
                }
            }
            return;
        }

        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_upgrade, null);
        TextView titleView = (TextView) contentView.findViewById(R.id.title);
        TextView messageView = (TextView) contentView.findViewById(R.id.message);

        titleView.setText(String.format("检查到新版本%s", updateResult.getVersion()));
        messageView.setText(updateResult.getContent().replace("||", "\n"));
        CustomDialogFactory.showConfirmDialog(
                mContext,
                false,
                contentView,
                "立即更新",
                "稍后提醒",
                new OnDialogClickListener() {
                    @Override
                    public void onClick(AlertDialog b) {
//                                UpgradeManager.getInstance().upgrade();
                        checkAndCreateDirectory("/update");
                        Intent updateIntent = new Intent(mContext, UpdateService.class);
                        updateIntent.putExtra("url", updateResult.getDown_url());
                        mContext.startService(updateIntent);
                        showMessage("开始下载中...");
                    }
                },
                new OnDialogClickListener() {
                    @Override
                    public void onClick(AlertDialog dialog) {
                        if(mForceUpdate) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }else {
                            dialog.dismiss();
                        }

                    }
                },
                null, true, true
        );

    }

    @Override
    public void onUpgradeFailed(Throwable e) {
        showMessage("已是最新版本无需更新");
    }

    public void checkAndCreateDirectory(String dirName) {
        File new_dir = new File(FileUtils.getAppDir(mContext) + dirName);
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
    }
}
