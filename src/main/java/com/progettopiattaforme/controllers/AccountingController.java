package com.progettopiattaforme.controllers;


import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.support.exceptions.MailUserAlreadyExistsException;
import com.progettopiattaforme.services.AccountingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.progettopiattaforme.support.ResponseMessage;

import java.util.List;


@RestController
@RequestMapping("/users")
public class AccountingController {
    @Autowired
    private AccountingService accountingService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid User user) {
        try {
            User added = accountingService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<User> getAll() {
        return accountingService.getAllUsers();
    }


}
