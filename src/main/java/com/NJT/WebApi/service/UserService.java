package com.NJT.WebApi.service;

import com.NJT.WebApi.model.auth.LoginResponse;
import com.NJT.WebApi.model.auth.RegistrationBody;
import com.NJT.WebApi.model.exception.EmailFailureException;
import com.NJT.WebApi.model.exception.RegistrationException;
import com.NJT.WebApi.model.exception.LoginException;
import com.NJT.WebApi.model.VerificationToken;
import com.NJT.WebApi.model.auth.ChangePasswordBody;
import com.NJT.WebApi.model.auth.LoginBody;
import com.NJT.WebApi.model.user.Student;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.model.user.ZaposleniUNastavi;
import com.NJT.WebApi.model.user.ZaposleniVanNastave;
import com.NJT.WebApi.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {

    UserRepository userRepository;
    StudentRepository studentRepository;
    ZaposleniVanNastaveRepository zaposleniVanNastaveRepository;
    ZaposleniUNastaviRepository zaposleniUNastaviRepository;
    EncryptionService encryptionService;
    JWTService jwtService;
    EmailService emailService;
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserService(
            StudentRepository studentRepository,
            ZaposleniVanNastaveRepository zaposleniVanNastaveRepository,
            ZaposleniUNastaviRepository zaposleniUNastaviRepository,
            UserRepository userRepository,
            EncryptionService encryptionService,
            JWTService jwtService,
            EmailService emailService,
            VerificationTokenRepository verificationTokenRepository) {
        this.studentRepository = studentRepository;
        this.zaposleniVanNastaveRepository = zaposleniVanNastaveRepository;
        this.zaposleniUNastaviRepository = zaposleniUNastaviRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void registerUser(RegistrationBody registrationBody) throws RegistrationException, EmailFailureException {

        Optional<User> opUser = userRepository.findByEmail(registrationBody.getEmail());
        User user;
        if (!opUser.isPresent()) {
            throw new RegistrationException("Email doesn't exist in the system. Please contact administrator.");
        } else {
            user = opUser.get();
        }

        if (user.getUsername() != null) {
            throw new RegistrationException("User already registered. Please login.");
        }
        if (userRepository.findByUsername(registrationBody.getUsername()).isPresent()) {
            throw new RegistrationException("Username already exists in the system. Change username.");
        }

        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        user.setUsername(registrationBody.getUsername());
        VerificationToken verificationToken = createVerificationToken(user);

        upisiUBazu(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public VerificationToken createVerificationToken(User user) throws EmailFailureException {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationToken(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        emailService.sendVerificationEmail(verificationToken);
        return verificationToken;
    }

    private void upisiUBazu(User user) throws RegistrationException {
        if (user instanceof ZaposleniVanNastave) {
            zaposleniVanNastaveRepository.save((ZaposleniVanNastave) user);
        } else if (user instanceof ZaposleniUNastavi) {
            zaposleniUNastaviRepository.save((ZaposleniUNastavi) user);
        } else if (user instanceof Student) {
            studentRepository.save((Student) user);
        } else {
            throw new RegistrationException("Invalid user type");
        }
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isPresent()) {
            VerificationToken verificationToken1 = verificationToken.get();
            User user = verificationToken1.getUser();
            if (!user.getPotvrdjenMail()) {
                user.setPotvrdjenMail(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public LoginResponse loginUser(LoginBody loginBody) throws LoginException, EmailFailureException {

        Optional<User> opUser = userRepository.findByUsername(loginBody.getUsername());
        LoginResponse loginResponse;
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.getPotvrdjenMail()) {
                    if (user.getOdobren()) {
                        //return jwtService.createToken(user); //stari kod, ne radimo ovako vise
                        loginResponse = new LoginResponse();
                        loginResponse.setUser(user);
                        loginResponse.setJwt(jwtService.createToken(user));
                        return loginResponse;
                    } else {
                        throw new LoginException("Profil jos uvek nije odobren!");
                    }
                } else {
                    throw new LoginException("Nije verifikovana email adresa!");
                }
            }
        }
        return null;
    }

    public List<User> getAllByOdobren(String odobren) {
        boolean odobrenBool = Boolean.parseBoolean(odobren);

        return (List<User>) userRepository.findAllByOdobren(odobrenBool);
    }

    public boolean acceptUserRegistration(User user) {
        Optional<User> userOpt = userRepository.findById(user.getId());

        if (userOpt.isPresent()) {
            User userDb = userOpt.get();
            userDb.setOdobren(true);
            userDb.setRole(user.getRole());

            userRepository.save(userDb);

            try {
                emailService.posaljiMailZaRezervaciju("Prihvacen zahtev za registraciju",
                        "\n\n"
                        + "Vas zahtev za registraciju je prihvacen! "
                        + "\n\n-----------------------------------------------------------\n\n "
                        + "Srdacno,\n "
                        + "NjtApp2024", user.getEmail());
            } catch (EmailFailureException ex) {
                Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            return true;
        }

        return false;
    }

    public boolean changePassword(ChangePasswordBody changePassword) {
        Optional<User> userOpt = userRepository.findById(changePassword.getId());

        if (userOpt.isPresent()) {
            User userDb = userOpt.get();

            if (encryptionService.checkPassword(changePassword.getCurrentPassword(), userDb.getPassword())) {
                userDb.setPassword(encryptionService.encryptPassword(changePassword.getNewPassword()));
            }

            userRepository.save(userDb);

            return true;
        }

        return false;
    }

    public boolean denyUserRegistration(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User userDb = userOpt.get();
            userDb.setRole("/");
            
            userRepository.save(userDb);

            return true;
        }

        return false;
    }
}
