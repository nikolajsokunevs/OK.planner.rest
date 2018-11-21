package lv.ok.service;

import lv.ok.models.User;

public interface IUserService {

    void deleteUser(String id);
    String signUpUser(User user);

}
