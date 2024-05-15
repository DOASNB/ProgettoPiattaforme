package com.progettopiattaforme.repositories;

import com.progettopiattaforme.entites.Delivery;
import com.progettopiattaforme.entites.Purchase;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.Date;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer>{

    List<Delivery> findByAddress(String address);
    Delivery findByPurchase(Purchase purchase);







}
