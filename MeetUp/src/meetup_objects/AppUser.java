package meetup_objects;

public class AppUser {
    private String mEmail;
    private String mAuth_token;
    private String mName;

    public AppUser(String email, String authToken, String name) {
        mEmail = email;
        mAuth_token = authToken;
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAuth_token() {
        return mAuth_token;
    }

    public void setAuth_token(String auth_token) {
        this.mAuth_token = auth_token;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
