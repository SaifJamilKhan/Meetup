package com.example.meetup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkRequestUtil {
    // private static String baseUrl = "http://meet-up-server.herokuapp.com/";

    private static String baseUrl = "http://192.168.0.15:3000/";

    public static interface NetworkRequestListener {

        public abstract void requestSucceededWithJSON(JSONObject object);

        public abstract void requestFailedWithJSON(JSONObject object);

        public abstract void requestFailed(Exception e);
    }

    public static void makePostRequest(String path,
                                       NetworkRequestListener listener, JSONObject body) {
        HttpPost post = new HttpPost(baseUrl + path);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        try {
            StringEntity entity = new StringEntity(body.toString());
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));

            post.setEntity(entity);

            RequestTask task = new RequestTask(listener, body, post);
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
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        try {
            StringEntity entity = new StringEntity(body.toString());
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));

            put.setEntity(entity);

            RequestTask task = new RequestTask(listener, body, put);
            task.execute();
        } catch (IOException e) {
            Log.v("", "exception: " + e);
            listener.requestFailed(null);
        }
    }

    //    NetworkRequestUtil.makePutRequest(mPath, this, body, parameters);
    private static class RequestTask extends AsyncTask<String, String, String> {
        NetworkRequestListener listener;
        JSONObject body;
        org.apache.http.client.methods.HttpEntityEnclosingRequestBase requestBase;

        public RequestTask(NetworkRequestListener listener, JSONObject body, org.apache.http.client.methods.HttpEntityEnclosingRequestBase requestBase) {
            this.listener = listener;
            this.body = body;
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
        String responseString = null;
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
