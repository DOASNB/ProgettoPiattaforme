package com.progettopiattaforme.controllers;


import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.security.exceptions.MailUserAlreadyExistsException;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;
import com.progettopiattaforme.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.progettopiattaforme.security.ResponseMessage;

import java.util.List;


@RestController
@RequestMapping("/users")
public class AccountingController {
    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid User user) {
        try {
            User added = userService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('utente')")
    @GetMapping("/byEmail")
    public ResponseEntity getByEmail(@RequestParam(value = "email") String email){

        try {

            return new ResponseEntity<>(userService.getByEmail(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasRole('utente')")
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }


}
