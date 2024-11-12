package com.progettopiattaforme.controllers;



import com.progettopiattaforme.entites.Order;
import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.services.PurchasingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.progettopiattaforme.security.ResponseMessage;
import com.progettopiattaforme.security.exceptions.DateWrongRangeException;
import com.progettopiattaforme.security.exceptions.QuantityProductUnavailableException;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/orders")

public class PurchasingController {
    @Autowired
    private PurchasingService purchasingService;

    @PreAuthorize("hasRole('utente')")
    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity create(@RequestBody @Valid Order order) {
        try {
            return new ResponseEntity<>(purchasingService.addPurchase(order), HttpStatus.OK);
        } catch (QuantityProductUnavailableException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity unavailable!", e);
        }
    }

    @PreAuthorize("hasRole('utente')")
    @GetMapping("/{user}")
    public List<Order> getOrders(@RequestBody @Valid @PathVariable("user") User user) {
        try {
            return purchasingService.getOrdersByUser(user);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }/*
    @PreAuthorize("hasRole('utente')")
    @GetMapping("/{user}/{startDate}/{endDate}")
    public ResponseEntity getPurchasesInPeriod(@Valid @PathVariable("user") User user, @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date start, @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date end) {
        try {
            List<Order> result = purchasingService.getOrdersByUserInPeriod(user, start, end);
            if (result.isEmpty()) {
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", e);
        } catch (DateWrongRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be previous end date", e);
        }
    }*/


}
