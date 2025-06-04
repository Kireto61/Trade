package com.example.diplomenproekt.services;

import com.example.diplomenproekt.models.entities.enums.CategoryEnum;
import com.example.diplomenproekt.models.viewModel.MaxPriceViewModel;
import com.example.diplomenproekt.models.viewModel.ProductViewModel;

import java.util.List;

public interface ProductsService {

    List<ProductViewModel> getAllProducts();

    List<ProductViewModel> getAllOf(CategoryEnum currency);

    ProductViewModel findProductById(Long id) throws Throwable;

    boolean checkQuantityAvailability(Integer portion, Long id) throws Throwable;

    void addProductToCart(Integer portion, Long id, Object email) throws Throwable;

    MaxPriceViewModel getProductMaxPrice();
}
