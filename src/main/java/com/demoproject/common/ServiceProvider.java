package com.demoproject.common;

public interface ServiceProvider {

    /**
     * Retrieves the instance of the service class.
     * @param serviceClass Service class of which the instance shall be retrieved.
     * @return Returns the instance of the provided service class.
     * @param <Type> Type of the service class.
     */
    <Type> Type get(Class<Type> serviceClass);
}
