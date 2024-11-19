package com.NJT.WebApi.api;

import com.NJT.WebApi.model.auth.RegistrationBody;
import com.NJT.WebApi.model.exception.EmailFailureException;
import com.NJT.WebApi.model.exception.RegistrationException;
import com.NJT.WebApi.model.exception.LoginException;
import com.NJT.WebApi.model.auth.LoginBody;
import com.NJT.WebApi.model.auth.LoginResponse;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final UserController user;
    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService, UserController user) {
        this.userService = userService;
        this.user = user;
    }

    /*
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {

        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with email sending... ");
        }
    }*/

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegistrationBody registrationBody) {

        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with email sending... ");
        }
    }



    @GetMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if(userService.verifyUser(token)){
            return ResponseEntity.status(HttpStatus.OK).body("Uspesno potvrdjena mail adresa.");
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginBody loginBody){
        LoginResponse response = new LoginResponse();
        try {
            response = userService.loginUser(loginBody);
        } catch (LoginException e) {
            response.setSuccess(false);
            response.setFailureReason(e.getMsg());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if(response==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }



}
