package com.apply.update.presenter;

import com.apply.update.model.UpdateRelease;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface UpgradePresenter {

    void showProgressOnChecking();

    void hideProgressOnChecking();

    /**
     * 检查更新成功后触发
     * @param updateResult 检查更新结果
     * @param tinkerCode 热更新版本号
     *
     */
    void onCheckUpgraded(UpdateRelease updateResult, int tinkerCode);

    /**
     * 更新失败后触发
     * @param e
     */
    void onUpgradeFailed(Throwable e);


}
