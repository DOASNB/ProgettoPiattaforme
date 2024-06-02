package com.progettopiattaforme.services;


import com.progettopiattaforme.entites.Product;
import com.progettopiattaforme.entites.ProductInPurchase;
import com.progettopiattaforme.entites.Order;
import com.progettopiattaforme.entites.User;
import com.progettopiattaforme.repositories.ProductRepository;
import com.progettopiattaforme.security.exceptions.DateWrongRangeException;
import com.progettopiattaforme.security.exceptions.QuantityProductUnavailableException;
import com.progettopiattaforme.security.exceptions.UserNotFoundException;

import com.progettopiattaforme.repositories.ProductInPurchaseRepository;
import com.progettopiattaforme.repositories.OrderRepository;
import com.progettopiattaforme.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;


@Service
public class PurchasingService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;


    @Transactional
    public Order addPurchase(Order order) throws QuantityProductUnavailableException {
        int maxRetries = 3;
        int attempt = 0;
        while (attempt < maxRetries){
        try {
            Order result = orderRepository.save(order);
            for (ProductInPurchase pip : result.getProductsInPurchase()) {
                pip.setOrder(result);
                ProductInPurchase justAdded = productInPurchaseRepository.save(pip);

                Product product = productRepository.findByBarCode(justAdded.getProduct().getBarCode()).get(0);
                int newQuantity = product.getQuantity() - pip.getQuantity();
                if (newQuantity < 0) {
                    throw new QuantityProductUnavailableException();
                }
                product.setQuantity(newQuantity);
                productRepository.save(product);

            }

            return result;
        }catch (OptimisticLockingFailureException e) {
            attempt++;
            if (attempt >= maxRetries){
                throw new OptimisticLockingFailureException("OptimisticLockingFailureException", e);
            }

        }}throw new RuntimeException("Failed to save order after " + maxRetries + " attempts");
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(User user) throws UserNotFoundException {
        if ( !userRepository.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        return orderRepository.findByBuyer(user);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserInPeriod(User user, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException {
        if ( !userRepository.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >= 0 ) {
            throw new DateWrongRangeException();
        }
        return orderRepository.findByBuyerInPeriod(startDate, endDate, user);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllPurchases() {
        return orderRepository.findAll();
    }


}
