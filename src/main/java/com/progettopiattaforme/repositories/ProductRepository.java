package com.progettopiattaforme.repositories;

import com.progettopiattaforme.entites.Product;
import com.progettopiattaforme.entites.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);
    List<Product> findByBarCode(String name);
    boolean existsByBarCode(String barCode);
    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name ILIKE ?1 OR ?1 IS NULL) OR " +
            "      (p.description ILIKE ?2 OR ?2 IS NULL)")
    List<Product> advancedSearch(String name, String description);

    @Query("select p " +
            "from Product p JOIN UserFavorite AS uf ON p.id = uf.favorites.id " +
            "AND uf.user.id = ?1")
    List<Product> findFavorites(int userId);

    //List<Product> findByFavoredBy(User favoredBy);


}
