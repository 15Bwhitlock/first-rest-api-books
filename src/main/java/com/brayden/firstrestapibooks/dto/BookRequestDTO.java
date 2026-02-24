package com.brayden.firstrestapibooks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDTO {
    private String name;
    private String author;
    private String price;
}
