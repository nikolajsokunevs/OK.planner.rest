package lv.ok.service;

import lv.ok.models.User;

public interface IUserService {

    User addUser(User user);
    void deleteUser(String id);

}
