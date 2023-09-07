package com.demoproject.net;

public interface HttpResponseConverter<Type> {

    /**
     * This method can be used to convert the HTTP response
     * to any desired object.
     * @param response The HTTP response to convert.
     * @return
     * @throws Exception
     */
    Type convert(HttpResponse response) throws Exception;
}
