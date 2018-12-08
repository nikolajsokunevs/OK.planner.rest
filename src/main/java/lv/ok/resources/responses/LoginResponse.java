package lv.ok.resources.responses;

public class LoginResponse {
    private boolean isAuthenticationSuccessful;
    private String token;
    private String message;

    public LoginResponse(boolean isAuthenticationSuccessful, String message) {
        this.isAuthenticationSuccessful = isAuthenticationSuccessful;
        this.message = message;
    }


    public boolean getIsAuthenticationSuccessful() {
        return isAuthenticationSuccessful;
    }

    public void setIsAuthenticationSuccessful(boolean isAuthenticationSuccessful) {
        this.isAuthenticationSuccessful = isAuthenticationSuccessful;
    }


    public String getToken() {return token;}

    public void setToken(String token){this.token = token;}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
