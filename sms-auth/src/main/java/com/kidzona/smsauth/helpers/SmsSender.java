package com.kidzona.smsauth.helpers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    private static final String ACCOUNT_SID =
            "AC349bf745ee31c025a6a113e6fc7ab8fd";
    private static final String AUTH_TOKEN =
            "f6efd6877f64def1716594371cc7f650";
    private static final String FROM_NUMBER =
            "+12057367703";

    public static void sendSMS(String toNumber, String textMessage) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        toNumber = "+2" + toNumber;
        Message message = Message
                .creator(new PhoneNumber(toNumber), // to
                        new PhoneNumber(FROM_NUMBER), // from
                        textMessage)
                .create();

    }
}

