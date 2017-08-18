package com.apply.update.request;

import com.apply.update.model.UpdateRelease;

/**
 * Created by Administrator on 2017/8/16.
 */

public interface RequestData {

    //数据返回成功
    void responseSuccess(UpdateRelease updateRelease);
}
