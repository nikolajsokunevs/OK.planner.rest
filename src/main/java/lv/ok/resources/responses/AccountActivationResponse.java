package lv.ok.resources.responses;

public class AccountActivationResponse {
    private int verificationAttempts;
    private String responseMessage;

    public AccountActivationResponse(String responseMessage, int verificationAttempts) {
        this.responseMessage = responseMessage;
        this.verificationAttempts = verificationAttempts;
    }

}
