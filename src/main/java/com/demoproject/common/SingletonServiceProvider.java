package com.demoproject.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class SingletonServiceProvider implements ServiceProvider {

    private final Logger logger = LogManager.getLogger(SingletonServiceProvider.class);
    private final Map<String, Object> instanceMap = new HashMap<>();

    private static final ServiceProvider SERVICE_PROVIDER = new SingletonServiceProvider();

    private SingletonServiceProvider() { }

    @Override
    public <Type> Type get(Class<Type> serviceClass) {
        if (serviceClass == null) { return null; }

        String typeName = serviceClass.getTypeName();
        Object instanceAsObject = instanceMap.get(typeName);

        // if an instanceAsObject is already available, we'll return that...
        if (instanceAsObject != null && serviceClass.isAssignableFrom(instanceAsObject.getClass())) {
            logger.log(Level.DEBUG, "Reusing existing service instance for type '" + typeName + "'.");

            return (Type) instanceAsObject;
        }

        logger.log(Level.DEBUG, "Instantiating service of type '" + typeName + "'.");

        try {
            // for the sake of simplicity, we'll only focus on no-argument constructor...
            Constructor<Type> constructor = serviceClass.getDeclaredConstructor();
            constructor.setAccessible(true);        // constructor might be private...
            Type instance = constructor.newInstance();

            instanceMap.put(typeName, instance);

            logger.log(Level.DEBUG, "Service instantiation succeeded for type '" + typeName + "'.");

            return instance;
        } catch (Exception exception) {
            logger.log(Level.ERROR, "An exception occurred while instantiating service of type '" + typeName + "'.", exception);

            return null;
        }
    }

    public static ServiceProvider getInstance() {
        return SERVICE_PROVIDER;
    }
}
