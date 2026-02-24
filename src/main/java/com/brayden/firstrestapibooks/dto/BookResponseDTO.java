package com.brayden.firstrestapibooks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDTO {
    private String id;
    private String name;
    private String author;
    private String price;
}
