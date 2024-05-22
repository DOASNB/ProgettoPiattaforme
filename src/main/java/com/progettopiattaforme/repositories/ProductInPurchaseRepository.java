package com.progettopiattaforme.repositories;

import com.progettopiattaforme.entites.ProductInPurchase;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase, Integer> {

}
