package com.progettopiattaforme.repositories;


import com.progettopiattaforme.entites.Order;
import com.progettopiattaforme.entites.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.Date;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByBuyer(User user);
    List<Order> findByPurchaseTime(Date date);
    List<Order> findByBuyerOrderByPurchaseTime(User user);

    @Query("select p from Order p where p.purchaseTime > ?1 and p.purchaseTime < ?2 and p.buyer = ?3")
    List<Order> findByBuyerInPeriod(Date startDate, Date endDate, User user);

}
