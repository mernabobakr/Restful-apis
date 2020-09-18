package com.kidzona.smsauth.services;

import com.kidzona.smsauth.exceptions.InvalidTokenException;
import com.kidzona.smsauth.helpers.SmsSender;
import com.kidzona.smsauth.helpers.TokenUtil;
import com.kidzona.smsauth.models.Token;
import com.kidzona.smsauth.models.User;
import com.kidzona.smsauth.repos.TokenRepo;
import com.kidzona.smsauth.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenUtil tokenUtil;

    public void createTokenForPhone(String phoneNumber) {
        int token = generateRandomToken();
        String tokenString = String.valueOf(token);
        while (!tokenRepo.findByToken(tokenString).isEmpty()) {
            token = generateRandomToken();
            tokenString = String.valueOf(token);
        }
        Token tokenObject = new Token();
        tokenObject.setNumber(phoneNumber);
        tokenObject.setToken(tokenString);
        this.tokenRepo.save(tokenObject);

        String smsText = "Your verification code is " + tokenString;
        SmsSender.sendSMS(phoneNumber, smsText);
    }

    private int generateRandomToken() {
        int upper = 9999;
        int lower = 1001;
        return (int) (Math.random() * (upper - lower)) + lower;
    }

    public String acknowledge(Token token) throws InvalidTokenException {
        List<Token> tokens = this.tokenRepo.findByToken(token.getToken());
        if (tokens == null || tokens.isEmpty()) throw new InvalidTokenException("Invalid Token");
        System.out.println(token.getNumber());
        System.out.println(tokens.get(0).getNumber());
        if(!tokens.get(0).getNumber().equals(token.getNumber())) throw new InvalidTokenException("Invalid Token");
        return this.createJWT(tokens.get(0).getNumber());
    }

    public String createJWT(String phoneNumber){
        List<User> users = this.userRepo.findByPhone(phoneNumber);
        if(users == null || users.isEmpty()){
            User user = new User();
            user.setPhone(phoneNumber);
            this.userRepo.save(user);
            return this.createJWT(phoneNumber);
        }
        return this.tokenUtil.generateJWT(users.get(0));
    }
}
