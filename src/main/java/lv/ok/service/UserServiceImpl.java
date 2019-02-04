package lv.ok.service;

import lv.ok.HashPassword;
import lv.ok.JwtGenerator;
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
            user.setDateCreated();
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

        boolean isUsernameCorrect = userRepository.checkIfUsernameExists(user.getUsername());

        if (isUsernameCorrect == false){
            return new LoginResponse(false, "Your details are incorrect");
        }

        boolean isPasswordCorrect = checkIfPasswordIsValid(user);

        if (isPasswordCorrect == false){
            return new LoginResponse(false, "Your details are incorrect");
        }

        LoginResponse response = generateLoginResponse(user);
        return response;
    }

    public boolean checkIfPasswordIsValid(User user){
        String hash = userRepository.getPasswordHash(user.getUsername());
        boolean isPasswordCorrect = false;

        try {
            isPasswordCorrect = HashPassword.validatePassword(user.getPassword(), hash);
            return isPasswordCorrect;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException exc) {
            System.out.println("either password hashing algorithm or key is invalid");
            return isPasswordCorrect;
        }
    }

    public LoginResponse generateLoginResponse(User user) {
        LoginResponse response = new LoginResponse(true,
                "Welcome, " + user.getUsername() + "!");
        String token = new JwtGenerator().generateToken(user);
        response.setToken(token);
        return response;
    }

}
