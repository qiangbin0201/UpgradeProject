package com.apply.update.request;

import android.util.Log;


import com.apply.update.model.UpdateRelease;
import com.apply.update.presenter.UpgradeManager;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/14.
 */

public class UpgradeRequest {

    private static UpgradeRequest mUpgradeRequest;
    private UpdateRelease updateRelease;

    private RequestData mRequestData;

    public static UpgradeRequest getInstance() {
        if (mUpgradeRequest == null) {
            mUpgradeRequest = new UpgradeRequest();
        }
        return mUpgradeRequest;
    }

    public void setRequestData(RequestData requestData){
        mRequestData = requestData;
    }



    private OkHttpClient client = new OkHttpClient();

    private Gson gson = new Gson();

    public UpdateRelease post(final String url, int code, String project, String package_name, String business) throws IOException {
        client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(20000, TimeUnit.MILLISECONDS);
        RequestBody body = new FormEncodingBuilder()
                .addEncoded("versionCode", String.valueOf(code))
                .addEncoded("project", project)
                .addEncoded("pkg", package_name)
                .addEncoded("business", business)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("onFailure", request.urlString());

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    updateRelease = gson.fromJson(response.body().charStream(), UpdateRelease.class);
                    setRequestData(UpgradeManager.getInstance());

                    mRequestData.responseSuccess(updateRelease);

                }

            }
        });
        return updateRelease;
    }
}
