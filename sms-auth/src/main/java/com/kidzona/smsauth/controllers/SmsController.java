package com.kidzona.smsauth.controllers;

import com.kidzona.smsauth.exceptions.InvalidTokenException;
import com.kidzona.smsauth.models.Token;
import com.kidzona.smsauth.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
public class SmsController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/sms/verify/{phone}")
    public void verifyPhoneNumber(@PathVariable("phone") String phoneNumber){
        this.tokenService.createTokenForPhone(phoneNumber);
    }

    @PostMapping("/sms/ack")
    public HashMap<String, String> acknowledgeSmsReceived(@RequestBody Token token) throws InvalidTokenException {
        String jwtToken = this.tokenService.acknowledge(token);
        HashMap<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return response;
    }
}
