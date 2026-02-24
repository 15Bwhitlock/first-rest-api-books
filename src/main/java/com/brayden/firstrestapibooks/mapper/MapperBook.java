package com.brayden.firstrestapibooks.mapper;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.model.Book;
import org.modelmapper.ModelMapper;

public class MapperBook {
    private static final ModelMapper mapper = new ModelMapper();
    public static Book dtoRequestToModel(BookRequestDTO dto){
        return mapper.map(dto, Book.class);
    }
    public static BookResponseDTO modelToResponseDto(Book book) {
        return mapper.map(book, BookResponseDTO.class);
    }
}
