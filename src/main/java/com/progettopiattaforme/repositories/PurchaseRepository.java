package com.progettopiattaforme.repositories;


import com.progettopiattaforme.entites.Purchase;
import com.progettopiattaforme.entites.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.Date;
import java.util.List;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByBuyer(User user);
    List<Purchase> findByPurchaseTime(Date date);
    List<Purchase> findByBuyerOrderByPurchaseTime(User user);

    @Query("select p from Purchase p where p.purchaseTime > ?1 and p.purchaseTime < ?2 and p.buyer = ?3")
    List<Purchase> findByBuyerInPeriod(Date startDate, Date endDate, User user);

}
