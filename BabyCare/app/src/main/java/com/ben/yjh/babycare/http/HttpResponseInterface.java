package com.ben.yjh.babycare.http;

public interface HttpResponseInterface<Object> {
    void onStart();

    void onSuccess(Object classOfT);

    void onFailure(HttpResult result);

    void onHttpError(String error);
}
