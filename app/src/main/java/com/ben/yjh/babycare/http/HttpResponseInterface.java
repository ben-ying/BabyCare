package com.ben.yjh.babycare.http;

import com.ben.yjh.babycare.model.HttpBaseResult;

public interface HttpResponseInterface<Object> {
    void onStart();

    void onSuccess(Object classOfT);

    void onFailure(HttpBaseResult result);

    void onHttpError(String error);
}
