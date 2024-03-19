package com.example.diplomenproekt.models.bindingModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProductAddBindingModel {
    private String name;
    private Double price;
    private Integer quantity;
    private String category;
    private String company;
    private String url;

    public ProductAddBindingModel() {
    }

    @Size(min = 5, max = 20)
    @NotBlank
    public String getName() {
        return name;
    }

    public ProductAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    @Positive
    @NotNull
    public Double getPrice() {
        return price;
    }

    public ProductAddBindingModel setPrice(Double price) {
        this.price = price;
        return this;
    }

    @Positive
    @NotNull
    public Integer getQuantity() {
        return quantity;
    }

    public ProductAddBindingModel setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @NotBlank
    public String getCategory() {
        return category;
    }

    public ProductAddBindingModel setCategory(String category) {
        this.category = category;
        return this;
    }

    @NotBlank
    public String getCompany() {
        return company;
    }

    public ProductAddBindingModel setCompany(String company) {
        this.company = company;
        return this;
    }

    @NotBlank
    public String getUrl() {
        return url;
    }

    public ProductAddBindingModel setUrl(String url) {
        this.url = url;
        return this;
    }
}
