package com.brayden.firstrestapibooks.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// Switched from @Getter/@Setter to @Data for simpler test setup in the test files.
//@Getter
//@Setter
@Data
public class BookRequestDTO {
    private String name;
    private String author;
    private String price;
}
