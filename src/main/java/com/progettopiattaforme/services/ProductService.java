package com.progettopiattaforme.services;



import com.progettopiattaforme.ProgettoPiattaformeApplication;
import com.progettopiattaforme.entites.Product;

import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.entites.UserFavorite;
import com.progettopiattaforme.entites.UserFavoriteId;
import com.progettopiattaforme.security.authentication.exceptions.ProductNotFoundException;
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
    public List<Product> showAllProductsByNamePaged(int pageNumber, int pageSize, String sortBy,String name) {
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
    public List<Product>  showFavoredProducts(int userid) {

        return productRepository.findFavorites(userid);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        String formattedName = "%"+name+"%";
        return productRepository.advancedSearch(formattedName,formattedName);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByBarCode(String barCode) {
        return productRepository.findByBarCode(barCode);
    }


    @Transactional(readOnly = false)
    public void favoriteProduct(int userId, int productId) throws UserNotFoundException, ProductNotFoundException {

            User user = userRepository.findById(userId).get();
            Product product = productRepository.findById(productId).get();

            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUser(user);
            userFavorite.setFavorites(product);
            UserFavoriteId userFavoriteId = new UserFavoriteId();
            userFavoriteId.setFavoritesId(product.getId());
            userFavoriteId.setUserId(user.getId());
            userFavorite.setId(userFavoriteId);
            userFavoriteRepository.save(userFavorite);

    }

    @Transactional(readOnly = false)
    public void unFavoriteProduct(int userId   , int productId) throws UserNotFoundException, ProductNotFoundException {

        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        UserFavoriteId userFavoriteId = new UserFavoriteId();
        userFavoriteId.setUserId(user.getId());
        userFavoriteId.setFavoritesId(product.getId());

        userFavoriteRepository.deleteById(userFavoriteId);



    }


}