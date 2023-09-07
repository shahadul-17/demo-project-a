package com.demoproject.net;

public interface HttpClient {
    HttpResponse get(String requestUrl) throws Exception;
    <Type> Type get(String requestUrl, HttpResponseConverter<Type> converter) throws Exception;
    boolean isInsecure();
    int getMaximumRetryCount();
    int getRetryIntervalInMillis();
}
