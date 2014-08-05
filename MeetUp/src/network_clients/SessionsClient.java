package network_clients;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.meetup.NetworkRequestUtil;
import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;

public class SessionsClient implements NetworkRequestListener {

	private static SessionsClient instance;
	private final String path = "sessions";
	private SessionsClientListener mListener;

	public static SessionsClient getInstance() {
		if (instance == null) {
			instance = new SessionsClient();
		}
		return instance;
	}

	public interface SessionsClientListener {
		public void requestSucceededWithResponse(LoginResponse response);

		public void requestFailedWithError();
	}

	public void makeLoginCall(SessionsClientListener listener, JSONObject body) {
		mListener = listener;
		NetworkRequestUtil.makePostRequest(path, this, body);
	}

	@Override
	public void requestSucceededWithJSON(JSONObject object) {

		final LoginResponse response = new LoginResponse();

		try {
			boolean success = object.getBoolean("success");
			response.setSuccess(success);

			if (success) {
				response.setInfo(object.getString("info"));

				JSONObject data = object.getJSONObject("data");
				response.setAuth_token(data.getString("auth_token"));
			} else {
				response.setError(object.getString("error"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			response.setSuccess(false);
		}

		if (mListener != null) {
			mListener.requestSucceededWithResponse(response);
		}
	}

	@Override
	public void requestFailedWithJSON(JSONObject object) {

		final LoginResponse response = new LoginResponse();
		response.setSuccess(false);

		try {
			response.setError(object.getString("error"));
		} catch (JSONException e) {
			e.printStackTrace();
			response.setSuccess(false);
		}

		if (mListener != null) {
			mListener.requestSucceededWithResponse(response);
		}
	}

	@Override
	public void requestFailed(Exception e) {
		if (mListener != null) {
			mListener.requestFailedWithError();
		}
	}

	public static class LoginResponse {

		private boolean success;
		private String info;
		private String auth_token;
		private String error;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public String getAuth_token() {
			return auth_token;
		}

		public void setAuth_token(String auth_token) {
			this.auth_token = auth_token;
		}
	}
}
