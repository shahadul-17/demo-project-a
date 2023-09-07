package com.demoproject.net;

import com.demoproject.utils.JsonSerializer;

/**
 * @implNote Comprehensive features like headers, cookies etc. are not
 * implemented to keep this class simple.
 */
public class HttpResponse {

    private int statusCode;
    private String message;
    private String content;

    public HttpResponse() { }

    public HttpResponse(int statusCode, String message, String content) {
        setStatusCode(statusCode);
        setMessage(message);
        setContent(content);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
