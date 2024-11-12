package com.progettopiattaforme.controllers;



import com.progettopiattaforme.entites.Product;
import com.progettopiattaforme.security.authentication.exceptions.ProductNotFoundException;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;
import jakarta.validation.Valid;

import com.progettopiattaforme.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.progettopiattaforme.security.ResponseMessage;
import com.progettopiattaforme.security.exceptions.BarCodeAlreadyExistException;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductService productService;



    @PostMapping("/create")
    public ResponseEntity create(@RequestBody @Valid Product product) {
        try {
            productService.addProduct(product);
        } catch (BarCodeAlreadyExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Barcode already exist!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Added successful!"+ product), HttpStatus.OK);
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.showAllProducts();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Product> result = productService.showAllProducts(pageNumber, pageSize, sortBy);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/by_name")
    public ResponseEntity getByName(@RequestParam(required = false) String name) {
        List<Product> result = productService.showProductsByName(name);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('utente')")
    @GetMapping("/prova")
    public ResponseEntity prova() {

        String result = "yesss";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/favoritesList")
    public ResponseEntity favorites(@RequestParam(required = true) int userId) {
        List<Product> result = productService.showFavoredProducts(userId);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/favorite")
    public ResponseEntity favorite(@RequestBody Map<String,String> request){
        int userId = Integer.parseInt(request.get("userId"));
        int productId = Integer.parseInt(request.get("productId"));

        try {

            productService.favoriteProduct(userId,productId);

        }catch (ProductNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("Product not found!"), HttpStatus.NOT_FOUND);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("User not found!"), HttpStatus.NOT_FOUND);
        }catch (RuntimeException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseMessage("Added successful!"), HttpStatus.OK);


    }

    @PreAuthorize("hasRole('utente')")
    @PostMapping("/unFavorite")
    public ResponseEntity unFavorite(@RequestBody Map<String,String> request){
        int userId = Integer.parseInt(request.get("userId"));
        int productId = Integer.parseInt(request.get("productId"));


        try {
            productService.unFavoriteProduct(userId,productId);

        }catch (ProductNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("Product not found! with code: "), HttpStatus.NOT_FOUND);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("User not found! with email: "), HttpStatus.NOT_FOUND);
        }catch (RuntimeException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseMessage("remove successful!"), HttpStatus.OK);

    }



}
