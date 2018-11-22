package lv.ok.service;

import lv.ok.HashPassword;
import lv.ok.models.User;
import lv.ok.repository.UserRepository;
import lv.ok.resources.responses.LoginResponse;

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
    public LoginResponse signIn(User user) {

        //LoginResponse response = new LoginResponse();
        boolean isUsernameCorrect = userRepository.checkIfUsernameExists(user.getUsername());

        if (isUsernameCorrect == false){
            return new LoginResponse(false, "Your details are incorrect");
        }

        String hash = userRepository.getPasswordHash(user.getUsername());
        boolean isPasswordCorrect = false;

        try {
             isPasswordCorrect = HashPassword.validatePassword(user.getPassword(), hash);
        }
        catch (NoSuchAlgorithmException ne) {}
        catch (InvalidKeySpecException ie) {}

        if (isPasswordCorrect == false){
            return new LoginResponse(false, "Your details are incorrect");
        }

        return new LoginResponse(true,
                "Welcome, " + user.getUsername() + "!");
    }

}
