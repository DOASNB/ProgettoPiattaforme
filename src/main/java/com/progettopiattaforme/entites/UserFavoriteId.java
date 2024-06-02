package com.progettopiattaforme.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserFavoriteId implements java.io.Serializable {
    private static final long serialVersionUID = 4714969677752702766L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "favorites_id", nullable = false)
    private Integer favoritesId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFavoriteId entity = (UserFavoriteId) o;
        return Objects.equals(this.favoritesId, entity.favoritesId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favoritesId, userId);
    }

}