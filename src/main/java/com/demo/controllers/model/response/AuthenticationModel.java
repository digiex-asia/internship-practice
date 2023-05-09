package com.demo.controllers.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationModel {

    private String facebookId;
    private String googleId;
    private String firstName;
    private String lastName;
    private String email;



    public AuthenticationModel(GoogleIdToken.Payload payload) {
        this.facebookId = null;
        this.googleId = payload.getSubject();
        this.firstName = (String) payload.get("family_name");
        this.lastName = (String) payload.get("given_name");
        this.email = payload.getEmail();
    }

}
