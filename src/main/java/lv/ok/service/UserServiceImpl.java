package lv.ok.service;

import lv.ok.models.User;
import lv.ok.repository.UserRepository;

public class UserServiceImpl implements IUserService {

    private UserRepository userRepository = UserRepository.getInstance();

    @Override
    public User addUser(User user) {
        userRepository.insertUser(user);
        return null;
    }
}
