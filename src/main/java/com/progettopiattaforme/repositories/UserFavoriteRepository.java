package com.progettopiattaforme.repositories;

import com.progettopiattaforme.entites.UserFavorite;
import com.progettopiattaforme.entites.UserFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {



}
