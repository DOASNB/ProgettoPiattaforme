package com.progettopiattaforme.services;


import com.progettopiattaforme.entites.Product;
import com.progettopiattaforme.entites.User;

import com.progettopiattaforme.repositories.UserRepository;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.progettopiattaforme.security.exceptions.MailUserAlreadyExistsException;

import java.util.List;
import java.util.Set;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(User user) throws MailUserAlreadyExistsException {
        if ( userRepository.existsByEmail(user.getEmail()) ) {
            throw new MailUserAlreadyExistsException();
        }
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public User getByEmail(String email) throws UserNotFoundException {

        if(!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException();
        }

        return userRepository.findByEmail(email).get(0);
    }


    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
