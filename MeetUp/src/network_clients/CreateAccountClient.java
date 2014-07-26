package network_clients;

import network_clients.CreateAccountClient.CreateAccountResponse.CreateAccountErrorCodes;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.meetup.NetworkRequestUtil;
import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;

public class CreateAccountClient implements NetworkRequestListener {
	private CreateAccountClientListener mListener;
	private static CreateAccountClient instance;
	private final String path = "registrations";

	public static CreateAccountClient getInstance() {
		if (instance == null) {
			instance = new CreateAccountClient();
		}
		return instance;
	}

	public interface CreateAccountClientListener {
		public void requestSucceededWithResponse(CreateAccountResponse response);

		public void requestFailedWithError();
	}

	public void makeCreateAccountCall(CreateAccountClientListener listener,
			JSONObject body) {
		mListener = listener;
		NetworkRequestUtil.makePostRequest(path, this, body);
	}

	@Override
	public void requestFailedWithJSON(JSONObject object) {
		final CreateAccountResponse response = new CreateAccountResponse();
		response.setSuccess(false);
		try {
			JSONObject info = object.getJSONObject("info");
			if (info.get("email") != null) {
				response.setErrorCode(CreateAccountErrorCodes.EMAIL_TAKEN);
			} else {
				response.setErrorCode(CreateAccountErrorCodes.DEFAULT);
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
	public void requestSucceededWithJSON(JSONObject object) {

		final CreateAccountResponse response = new CreateAccountResponse();

		try {
			boolean success = object.getBoolean("success");
			response.setSuccess(success);
			response.setInfo(object.getString("info"));

			JSONObject data = object.getJSONObject("data");
			response.setAuth_token(data.getString("auth_token"));

			JSONObject user = data.getJSONObject("user");
			response.setEmail(user.getString("email"));
			response.setName(user.getString("name"));
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
		mListener.requestFailedWithError();
	}

	// TODO: Look for framework that can put json into the model for you, like
	// Mantle for iOS
	public static class CreateAccountResponse {

		private boolean success;
		private String info;
		private String email;
		private String name;
		private String auth_token;
		private CreateAccountErrorCodes errorCode;

		public void setErrorCode(CreateAccountErrorCodes errorCode) {
			this.errorCode = errorCode;
		}

		public CreateAccountErrorCodes getErrorCode() {
			return this.errorCode;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAuth_token() {
			return auth_token;
		}

		public void setAuth_token(String auth_token) {
			this.auth_token = auth_token;
		}

		public enum CreateAccountErrorCodes {
			EMAIL_TAKEN, DEFAULT;
		}
	}
}
