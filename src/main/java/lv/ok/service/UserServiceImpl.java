package lv.ok.service;

import lv.ok.utils.Constants;
import lv.ok.utils.HashPassword;
import lv.ok.utils.JwtGenerator;
import lv.ok.models.User;
import lv.ok.repository.UserRepository;
import lv.ok.resources.responses.LoginResponse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
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
            userRepository.insertUser(user);

            try {
                String host = "smtp.gmail.com";
                String user2 = "mark.gusman11@gmail.com";
                String from = "mark.gusman11@gmail.com";
                String to = user.getUsername();

//                String user2 = "xakim@inbox.lv";
//                String from = "xakim@inbox.lv";
//                String host = "mail.inbox.lv";


                String database = "planitnow";

                Properties properties = System.getProperties();
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.required", "true");

                java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

                Session mailSession = Session.getDefaultInstance(properties, null);
                mailSession.setDebug(false);
                Message msg = new MimeMessage(mailSession);
                msg.setFrom(new InternetAddress(from));
                InternetAddress[] address = {new InternetAddress(to)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject("welcome to PlanIt");
                msg.setSentDate(new Date());
                msg.setText("This is email text");
                Transport transport = mailSession.getTransport("smtp");
                transport.connect(host, user2, database);
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();
                System.out.println("email sent successfully");
            }
            catch (MessagingException mex) {
                mex.printStackTrace();
            }
            
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
