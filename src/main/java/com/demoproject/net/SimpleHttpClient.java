package com.demoproject.net;

import com.demoproject.common.PropertiesProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Properties;

public class SimpleHttpClient implements HttpClient {

    private final boolean isInsecure;
    private final int connectTimeoutInMillis;
    private final int readTimeoutInMillis;
    private final int maximumRetryCount;
    private final int retryIntervalInMillis;
    private final Logger logger = LogManager.getLogger(SimpleHttpClient.class);
    private static HostnameVerifier insecureHostnameVerifier;
    private static SSLSocketFactory insecureSslSocketFactory;
    private static HttpClient httpClient;

    private SimpleHttpClient(boolean isInsecure,
                             int connectTimeoutInMillis,
                             int readTimeoutInMillis,
                             int maximumRetryCount,
                             int retryIntervalInMillis) {
        this.isInsecure = isInsecure;
        this.connectTimeoutInMillis = connectTimeoutInMillis;
        this.readTimeoutInMillis = readTimeoutInMillis;
        this.maximumRetryCount = maximumRetryCount;
        this.retryIntervalInMillis = retryIntervalInMillis;
    }

    private HttpResponse sendGetRequest(String requestUrl, int retryCount) throws Exception {
        InputStream inputStream = null;

        try {
            HttpURLConnection connection = createHttpUrlConnection(requestUrl, isInsecure);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(connectTimeoutInMillis);
            connection.setReadTimeout(readTimeoutInMillis);

            int statusCode = connection.getResponseCode();

            inputStream = statusCode < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuilder contentBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line);
            }

            String content = contentBuilder.toString();

            return new HttpResponse(statusCode, connection.getResponseMessage(), content);
        } catch (Exception exception) {
            logger.log(Level.WARN, "An exception occurred while sending GET request to url, '" + requestUrl + "'.", exception);

            // if all the retry attempts failed, we shall throw exception...
            if (retryCount == 0) { throw exception; }

            if (retryIntervalInMillis < 1) {
                logger.log(Level.INFO, "Attempting retry.");
            } else {
                logger.log(Level.INFO, "Attempting retry in " + (retryIntervalInMillis / 1000) + " seconds.");

                // we shall wait for a while before attempting retry...
                // NOTE: WE MAY NOT ALWAYS NEED ADDITIONAL WAIT BEFORE RETRY
                // e.g. Internal socket connection of HttpURLConnection waits for a while
                // before throwing java.net.SocketTimeoutException...
                Thread.sleep(retryIntervalInMillis);
            }

            return sendGetRequest(requestUrl, retryCount - 1);
        } finally {
            // we shall not close the HttpURLConnection because it will close
            // the underlying socket and thus, the socket will not be reused...
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception exception) {
                    logger.log(Level.WARN, "An exception occurred while closing the underlying input stream of the HTTP client.", exception);
                }
            }
        }
    }

    /**
     * Sends a get request to the specified URL.
     * @implNote Performs retry according to the provided configuration.
     * Comprehensive features like headers, cookies etc. are not
     * handled to keep this method simple.
     * @param requestUrl URL string to send request.
     * @return HTTP response.
     * @throws Exception
     */
    public HttpResponse get(String requestUrl) throws Exception {
        return sendGetRequest(requestUrl, maximumRetryCount);
    }

    public <Type> Type get(String requestUrl, HttpResponseConverter<Type> converter) throws Exception {
        HttpResponse response = sendGetRequest(requestUrl, maximumRetryCount);
        Type convertedResponse = converter.convert(response);

        return convertedResponse;
    }

    public boolean isInsecure() {
        return isInsecure;
    }

    public int getMaximumRetryCount() {
        return maximumRetryCount;
    }

    public int getRetryIntervalInMillis() {
        return retryIntervalInMillis;
    }

    private static HttpURLConnection createHttpUrlConnection(String requestUrl, boolean isInsecure) throws Exception {
        URL url = new URL(requestUrl);
        URLConnection urlConnection = url.openConnection();

        if (isInsecure && urlConnection instanceof HttpsURLConnection) {
            HttpsURLConnection connection = (HttpsURLConnection) urlConnection;
            connection.setHostnameVerifier(insecureHostnameVerifier);
            connection.setSSLSocketFactory(insecureSslSocketFactory);
        }

        return (HttpURLConnection) urlConnection;
    }

    private static SSLSocketFactory createInsecureSocketFactory() throws Exception {
        TrustManager[] trustManagers = new TrustManager[] {
                new InsecureTrustManager(),
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());

        return sslContext.getSocketFactory();
    }

    public static HttpClient createHttpClient() throws Exception {
        if (httpClient != null) { return httpClient; }

        Properties properties = PropertiesProvider.getProperties();
        boolean isInsecure = "true".equals(properties.getProperty("HTTP_CLIENT_DISABLE_SECURITY"));
        int connectTimeoutInMillis = Integer.parseInt(properties.getProperty("HTTP_CLIENT_CONNECT_TIMEOUT_IN_MILLIS"));
        int readTimeoutInMillis = Integer.parseInt(properties.getProperty("HTTP_CLIENT_READ_TIMEOUT_IN_MILLIS"));
        int maximumRetryCount = Integer.parseInt(properties.getProperty("HTTP_CLIENT_MAXIMUM_RETRY_COUNT"));
        int retryIntervalInMillis = Integer.parseInt(properties.getProperty("HTTP_CLIENT_RETRY_INTERVAL_IN_MILLIS"));

        if (isInsecure) {
            insecureHostnameVerifier = new InsecureHostnameVerifier();
            insecureSslSocketFactory = createInsecureSocketFactory();
        }

        HttpClient httpClient = new SimpleHttpClient(
                isInsecure, connectTimeoutInMillis, readTimeoutInMillis,
                maximumRetryCount, retryIntervalInMillis);
        SimpleHttpClient.httpClient = httpClient;

        return httpClient;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }
}
