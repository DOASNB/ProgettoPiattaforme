package com.progettopiattaforme.services;



import com.progettopiattaforme.ProgettoPiattaformeApplication;
import com.progettopiattaforme.entites.Product;

import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.entites.UserFavorite;
import com.progettopiattaforme.entites.UserFavoriteId;
import com.progettopiattaforme.exceptions.ProductNotFoundException;
import com.progettopiattaforme.repositories.ProductRepository;
import com.progettopiattaforme.repositories.UserFavoriteRepository;

import com.progettopiattaforme.repositories.UserRepository;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.progettopiattaforme.security.exceptions.BarCodeAlreadyExistException;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;
    @Autowired
    private ProgettoPiattaformeApplication progettoPiattaformeApplication;


    @Transactional(readOnly = false)
    public void addProduct(Product product) throws BarCodeAlreadyExistException {
        if ( product.getBarCode() != null && productRepository.existsByBarCode(product.getBarCode()) ) {
            throw new BarCodeAlreadyExistException();
        }
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Product>  showFavoredProducts(String email) {

        User user = userRepository.findByEmail(email).get(0);



        return productRepository.findFavorites(user.getId());
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByBarCode(String barCode) {
        return productRepository.findByBarCode(barCode);
    }


    @Transactional(readOnly = false)
    public void favoriteProduct(String username, String barCode) throws UserNotFoundException, ProductNotFoundException {

        if ( productRepository.existsByBarCode(barCode) && userRepository.existsByEmail(username)) {

            User user = userRepository.findByEmail(username).get(0);
            Product product = productRepository.findByBarCode(barCode).get(0);




            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(user);
            userFavorite.setFavorites(product);
            UserFavoriteId userFavoriteId = new UserFavoriteId();
            userFavoriteId.setFavoritesId(product.getId());
            userFavoriteId.setUserId(user.getId());
            userFavorite.setId(userFavoriteId);
            userFavoriteRepository.save(userFavorite);

        }else {
            if(!userRepository.existsByEmail(username)){throw new UserNotFoundException();}
            if(!productRepository.existsByBarCode(barCode)){throw new ProductNotFoundException();}
            throw new RuntimeException();

        }


    }

    @Transactional(readOnly = false)
    public void unFavoriteProduct(String username, String barCode) throws UserNotFoundException, ProductNotFoundException {




        if ( !productRepository.existsByBarCode(barCode)) {throw new ProductNotFoundException();}
        if(!userRepository.existsByEmail(username)){throw new UserNotFoundException();}

        User user = userRepository.findByEmail(username).get(0);
        Product product = productRepository.findByBarCode(barCode).get(0);

        UserFavoriteId userFavoriteId = new UserFavoriteId();
        userFavoriteId.setUserId(user.getId());
        userFavoriteId.setFavoritesId(product.getId());

        userFavoriteRepository.deleteById(userFavoriteId);



    }


}