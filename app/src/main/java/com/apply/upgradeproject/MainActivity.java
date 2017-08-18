package com.apply.upgradeproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.apply.update.presenter.DefaultUpgradePresenter;
import com.apply.update.presenter.UpgradeManager;

/**
 * 遗留问题：当更新包的大小大于20M时，通知栏进度条数字显示有问题
 */
public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "http://upgrade.adgomob.com/UpgradeCenter/UpgradeThreeAction.do";

    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 第一个false表示是否忽略提示信息，如：已是最新版本无需更新
                 * 第二个true表示是否表示请求过程的过渡动画
                 * 第三个false表示是否强制更新
                 *
                 */

                DefaultUpgradePresenter presenter = new DefaultUpgradePresenter(mContext, false, true, false);
                /**
                 * REQUEST_URL表示请求地址
                 * 1021表示版本号
                 * guanggao_170728表示UID项目号
                 * com.bbnews.car表示包名
                 * bbnews表示业务线
                 * 1表示热更新版本号
                 */
                UpgradeManager.getInstance().setUpgradePresenter(presenter)
                        .checkUpgradeNew(REQUEST_URL, 1021, "guanggao_170728", "com.bbnews.car", "bbnews", 1);
            }
        });
    }
}
