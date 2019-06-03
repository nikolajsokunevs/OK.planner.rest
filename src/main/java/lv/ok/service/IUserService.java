package lv.ok.service;

import lv.ok.models.User;
import lv.ok.resources.responses.AccountActivationResponse;
import lv.ok.resources.responses.LoginResponse;

public interface IUserService {

    void deleteUser(String id);
    String signUpUser(User user);
    LoginResponse signIn(User user);
    AccountActivationResponse activateUser(String username, String hash);

}
