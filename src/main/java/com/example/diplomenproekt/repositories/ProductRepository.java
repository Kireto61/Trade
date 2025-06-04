package com.example.diplomenproekt.repositories;

import com.example.diplomenproekt.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT products.id,products.category,products.name,\n" +
            "       GREATEST((price),products.minprice)as price,\n" +
            "       products.quantity,products.url,products.minprice" +
            "       FROM products\n" +
            "    WHERE products.category = ?1", nativeQuery = true)
    List<Product> getAllBy(String type);


    @Query(value = "SELECT products.id,products.category,products.name,\n" +
            "       GREATEST((price ),products.minprice)as price,\n" +
            "       products.quantity,products.url,products.minprice" +
            "       FROM products\n" +
            "    WHERE products.id = ?1", nativeQuery = true)
    Optional<Product> gettById(Long id);


    @Query(value = "SELECT products.id,products.category,products.name,\n" +
                   "       GREATEST((price ),products.minprice)as price,\n" +
                   "       products.quantity,products.url,products.minprice" +
                   "       FROM products" , nativeQuery = true)
    List<Product> getAll();


    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET products.price = ?2\n" +
                   "       WHERE products.name = ?1", nativeQuery = true)
    void updatePrice(String currency, double price);
}
