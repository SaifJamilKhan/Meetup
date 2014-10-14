package com.example.meetup;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NetworkRequestUtil {
    // private static String baseUrl = "http://meet-up-server.herokuapp.com/";

    private static String baseUrl = "http://meetuprails.herokuapp.com/";

    public static interface NetworkRequestListener {

        public abstract void requestSucceededWithJSON(JSONObject object);

        public abstract void requestFailedWithJSON(JSONObject object);

        public abstract void requestFailed(Exception e);
    }

    public static void makePostRequest(String path,
                                       NetworkRequestListener listener, JSONObject body) {
        makePostRequest(path, listener, body, null);
    }

    public static void makePostRequest(String path,
                                      NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        String url = baseUrl + path;

        if(parameters != null) {
            String params = URLEncodedUtils.format(parameters, "UTF-8");
            url = url.concat("?" + params);
        }
        HttpPost post = new HttpPost(url);
        setJSONHeaders(post);
        try {
            StringEntity entity = new StringEntity(body.toString());
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));

            post.setEntity(entity);

            RequestTask task = new RequestTask(listener, post);
            task.execute();
        } catch (IOException e) {
            Log.v("", "exception: " + e);
            listener.requestFailed(null);
        }
    }

    public static void makePutRequest(String path,
                                       NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {

        String paramString = URLEncodedUtils.format(parameters, "UTF-8");
        HttpPut put = new HttpPut(baseUrl + path + '?' + paramString);
        setJSONHeaders(put);
        try {
            StringEntity entity = new StringEntity(body.toString());
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));

            put.setEntity(entity);

            RequestTask task = new RequestTask(listener, put);
            task.execute();
        } catch (IOException e) {
            Log.v("", "exception: " + e);
            listener.requestFailed(null);
        }
    }

    public static void makeGetRequest(String path,
                                      NetworkRequestListener listener, ArrayList<NameValuePair> parameters) {

        String paramString = URLEncodedUtils.format(parameters, "UTF-8");
        HttpGet get = new HttpGet(baseUrl + path + '?' + paramString);
        setJSONHeaders(get);
        RequestTask task = new RequestTask(listener, get);
        task.execute();
    }

    private static void setJSONHeaders(HttpRequestBase request) {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
    }

    private static class RequestTask extends AsyncTask<String, String, String> {
        NetworkRequestListener listener;
//        JSONObject body;
        HttpRequestBase requestBase;

        public RequestTask(NetworkRequestListener listener, HttpRequestBase requestBase) {
            this.listener = listener;
            this.requestBase = requestBase;
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(requestBase);
                responseString = handleResponse(response, listener);
            } catch (ClientProtocolException e) {
                listener.requestFailed(null);
            } catch (IOException e) {
                Log.v("meetup", "exception: " + e);
                listener.requestFailed(null);
            }
            return responseString;
        }
    }

    private static String handleResponse(HttpResponse response, NetworkRequestListener listener) throws IOException {
        String responseString;
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            responseString = getResponseString(response);
            try {
                JSONObject responseJSON = new JSONObject(responseString);
                listener.requestSucceededWithJSON(responseJSON);
            } catch (JSONException e) {
                listener.requestFailed(e);
                e.printStackTrace();
            }
        } else if (statusLine.getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY
                || statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            responseString = getResponseString(response);
            try {
                JSONObject responseJSON = new JSONObject(responseString);
                listener.requestFailedWithJSON(responseJSON);
            } catch (JSONException e) {
                listener.requestFailed(e);
                e.printStackTrace();
            }
        } else {
            listener.requestFailed(null);
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
        return responseString;
    }

    private static String getResponseString(HttpResponse response)
            throws IOException {
        String responseString;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        out.close();
        responseString = out.toString();
        return responseString;
    }
}
