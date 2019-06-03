package lv.ok.service;

import lv.ok.resources.responses.AccountActivationResponse;
import lv.ok.utils.Constants;
import lv.ok.utils.HashPassword;
import lv.ok.utils.JwtGenerator;
import lv.ok.models.User;
import lv.ok.repository.UserRepository;
import lv.ok.resources.responses.LoginResponse;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Properties;


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
            user.setEmailVerificationHash();
            user.setVerificationAttempts(0);
            user.setStatus("pending");

            userRepository.insertUser(user);

            //TODO send account activation link and hash code in email
            sendAuthenticationEmail(user.getUsername(), user.getEmailVerificationHash());
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
            return new LoginResponse(false, Constants.INVALID_LOGIN_DETAILS);
        }

        boolean isPasswordCorrect = checkIfPasswordIsValid(user);

        if (isPasswordCorrect == false){
            return new LoginResponse(false, Constants.INVALID_LOGIN_DETAILS);
        }

        LoginResponse response = generateLoginResponse(user);
        return response;
    }

    @Override
    public AccountActivationResponse activateUser(User user, String hash) {
        userRepository.activateUser(user, hash);
        return LoginResponse(username + " account has been activated", )
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

    public void sendAuthenticationEmail(String usernameValue, String hashValue) {
        try {
            String host = "smtp.gmail.com";
            String from = "mark.gusman11@gmail.com";
            String database = "planitnow";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.starttls.enable", Constants.TRUE);
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", Constants.TRUE);
            properties.put("mail.smtp.starttls.required", Constants.TRUE);

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            //  Session mailSession = Session.getDefaultInstance(properties, null);
            Session mailSession = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(from, database);
                }
            });
            mailSession.setDebug(false);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(usernameValue)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Verify your Planit email");
            msg.setSentDate(new Date());
            msg.setText("Please navigate to <href>" + Constants.DOMAIN +"/user/verify/username=" + usernameValue + "&&hash=" + hashValue + "</href> to verify your email address");
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, from, database);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
