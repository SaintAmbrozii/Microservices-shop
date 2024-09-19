package com.example.userservice.security.oauht2;




import com.example.userservice.domain.AuthProvider;
import com.example.userservice.exception.OAuth2AuthentificationProcessingException;

import java.util.Map;

public class Oauth2UserInfoFactory {

    public static Oauth2userInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOauth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthentificationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
