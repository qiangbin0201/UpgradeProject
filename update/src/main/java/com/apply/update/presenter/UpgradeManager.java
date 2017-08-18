package com.apply.update.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import com.apply.update.model.UpdateRelease;
import com.apply.update.request.RequestData;
import com.apply.update.request.UpgradeRequest;

import java.io.IOException;


/**
 * <p>系统更新、升级管理器</p>
 */
public class UpgradeManager implements RequestData {

    private static UpgradeManager instance = null;
    //交互处理类
    private UpgradePresenter mUpgradePresenter = null;
    //检查更新结果
    private UpdateRelease mUpgradeRelease = null;

    private static final int RESPONSE_SUCCESS = 1;

    private int mTinkerCode;


    private UpgradeManager() {
    }

    public static synchronized UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    /**
     * 设置升级过程中的交互处理类
     *
     * @param upgradePresenter
     */
    public UpgradeManager setUpgradePresenter(UpgradePresenter upgradePresenter) {
        mUpgradePresenter = upgradePresenter;
        return this;
    }


    /**
     * 展示菊花
     */
    private void showProgress() {
        if (mUpgradePresenter != null) {
            mUpgradePresenter.showProgressOnChecking();
        }
    }

    /**
     * 隐藏菊花
     */
    private void hidenProgress() {
        if (mUpgradePresenter != null) {
            mUpgradePresenter.hideProgressOnChecking();
        }
    }

    /**
     * 检查是否需要更新
     */
    public void checkUpgradeNew(String requestUrl, int versionCode, String project, String pkg, String business, int tinkerCode) {
        showProgress();
        try {
            UpgradeRequest.getInstance().post(requestUrl, versionCode, project, pkg, business);
            mTinkerCode = tinkerCode;
        } catch (IOException e) {
            e.printStackTrace();
            hidenProgress();
            mUpgradePresenter.onUpgradeFailed(e);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RESPONSE_SUCCESS:
                    hidenProgress();
                    mUpgradePresenter.onCheckUpgraded(mUpgradeRelease, mTinkerCode);
            }

        }
    };

    @Override
    public void responseSuccess(UpdateRelease updateRelease) {
        if(updateRelease != null){
            mUpgradeRelease = updateRelease;
            handler.sendEmptyMessage(RESPONSE_SUCCESS);
        }
    }
}
