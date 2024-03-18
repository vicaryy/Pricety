package com.vicary.zalandoscraper.service.repository_services;

import com.vicary.zalandoscraper.entity.WebUserEntity;
import com.vicary.zalandoscraper.exception.IllegalInputException;
import com.vicary.zalandoscraper.model.LogInModel;
import com.vicary.zalandoscraper.model.RegisterModel;
import com.vicary.zalandoscraper.pattern.Pattern;
import com.vicary.zalandoscraper.repository.WebUserRepository;
import com.vicary.zalandoscraper.service.map.WebUserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebUserService {

    private final static Logger logger = LoggerFactory.getLogger(WebUserService.class);

    private final WebUserRepository repository;

    private final WebUserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    public Optional<WebUserEntity> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void registerUser(WebUserEntity entity) {
        repository.save(entity);
        logger.info("New user added to repository: " + entity.getEmail());
    }

    public void registerUser(RegisterModel model) {
        registerUser(mapper.map(model));
    }

    public void checkRegisterModelValidation(RegisterModel model) throws IllegalArgumentException {
        if (!isEmailValid(model.getEmail()) || !isPasswordValid(model.getPassword()))
            throw new IllegalArgumentException("Invalid data.");

        if (isEmailTaken(model.getEmail()))
            throw new IllegalArgumentException("Email is already taken.");
    }

    public void checkLogInModelValidation(LogInModel model) throws IllegalArgumentException {
        WebUserEntity user = getByEmail(model.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid data."));
        if (!passwordEncoder.matches(model.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid data.");
    }

    private boolean isEmailValid(String email) {
        return Pattern.isEmail(email);
    }

    private boolean isEmailTaken(String email) {
        return getByEmail(email).isPresent();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7 && isPasswordContainsSpecialChar(password);
    }

    private boolean isPasswordContainsSpecialChar(String password) {
        for (char c : password.toCharArray())
            if (!Character.isLetterOrDigit(c))
                return true;
        return false;
    }
}
