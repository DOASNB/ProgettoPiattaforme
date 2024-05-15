package com.progettopiattaforme.services;

import com.progettopiattaforme.entites.Delivery;
import com.progettopiattaforme.entites.Purchase;
import com.progettopiattaforme.entites.User;
import support.exceptions.DeliveryAlreadyEndedException;
import support.exceptions.DeliveryAlreadyStartedException;
import com.progettopiattaforme.repositories.DeliveryRepository;

import com.progettopiattaforme.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional(readOnly =false)
    public Delivery addDelivery(Delivery delivery) {
       return deliveryRepository.save(delivery);
    }

    @Transactional(readOnly = true)
    public List<Delivery> showAllDeliveries() { return deliveryRepository.findAll(); };

    @Transactional(readOnly = true)
    public List<Delivery> showAllDeliveries(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Delivery> pagedResult = deliveryRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Delivery showDeliveryByPurchase(Purchase purchase) {
        return deliveryRepository.findByPurchase(purchase);
    };

    @Transactional(readOnly = true)
    public List<Delivery> showDeliveriesByUser(User user){
        List<Delivery> result = new ArrayList<>();
        for(Purchase p : purchaseRepository.findByBuyerOrderByPurchaseTime(user) ){
            result.add(showDeliveryByPurchase(p));
        }
        return result;
    };

    @Transactional(readOnly = false)
    public void startDelivery(Delivery delivery) throws DeliveryAlreadyStartedException {
        if(delivery.isShipped()) {throw new DeliveryAlreadyStartedException();};

        delivery.setShipped(true);
    }

    @Transactional(readOnly = false)
    public void endDelivery(Delivery delivery) throws DeliveryAlreadyEndedException {
        if(delivery.isDelivered()) {throw new DeliveryAlreadyEndedException();};

        delivery.setDelivered(true);
    }


}
