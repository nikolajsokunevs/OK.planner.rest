package lv.ok.service;

import lv.ok.HashPassword;
import lv.ok.models.User;
import lv.ok.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserServiceImpl implements IUserService {

    private UserRepository userRepository = UserRepository.getInstance();


    @Override
    public String signUpUser(User user) {
        boolean usernameAlreadyExists = userRepository.checkIfUsernameExists(user.getUsername());
        if (usernameAlreadyExists) {
            return "Username " + user.getUsername() + " is taken by another user. Please choose another username.";
        }
        else {
            userRepository.insertUser(user);
            return "Username " + user.getUsername() + " has been added successfully.";
        }
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    @Override
    public String signIn(User user) {

        boolean isUsernameCorrect = userRepository.checkIfUsernameExists(user.getUsername());
        if (isUsernameCorrect == false){
            return "Your details are incorrect";
        }
        String hash = userRepository.getPasswordHash(user.getUsername());
        boolean isPasswordCorrect = false;
        try {
             isPasswordCorrect = HashPassword.validatePassword(user.getPassword(), hash);
        }
        catch (NoSuchAlgorithmException ne) {}
        catch (InvalidKeySpecException ie) {}

        if (isPasswordCorrect == false){
            return "Your details are incorrect";
        }

        return "Welcome " + user.getUsername() + "! You are logged in now";
    }

}
