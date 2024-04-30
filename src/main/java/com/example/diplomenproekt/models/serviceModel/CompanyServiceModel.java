package com.example.diplomenproekt.models.serviceModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyServiceModel {
    private String name;
    private Integer vat;
    private String income;
}
