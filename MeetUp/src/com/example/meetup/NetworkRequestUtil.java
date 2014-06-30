package com.example.meetup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkRequestUtil {
	private static String baseUrl = "http://meet-up-server.herokuapp.com/";

	private static class RequestTask extends AsyncTask<String, String, String> {
		NetworkRequestListener listener;
		JSONObject body;

		public RequestTask(NetworkRequestListener listener, JSONObject body) {
			this.listener = listener;
			this.body = body;
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				HttpPost post = new HttpPost(uri[0]);
				post.setHeader("Accept", "application/json");
				post.setHeader("Content-type", "application/json");
				StringEntity entity = new StringEntity(this.body.toString());
				entity.setContentType("application/json");
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));

				post.setEntity(entity);
				response = httpclient.execute(post);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					try {
						convertToJson(responseString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
		}
	}

	public static interface NetworkRequestListener {

		public abstract void requestSucceededWithJSON();

		public abstract void requestFailed();
	}

	public static void makePostRequest(String path,
			NetworkRequestListener listener, JSONObject body) {
		RequestTask task = new RequestTask(listener, body);
		task.execute(baseUrl + path);
	}

	public static void convertToJson(String responseString)
			throws JSONException {
		JSONObject jObject = new JSONObject(responseString);
		Log.v("json", jObject.toString());
	}
}
