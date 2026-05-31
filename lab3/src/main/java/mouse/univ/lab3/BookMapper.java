package mouse.univ.lab3;

import mouse.univ.lab3.dto.BookCreateDTO;
import mouse.univ.lab3.dto.BookResponseDTO;
import mouse.univ.lab3.dto.BookUpdateDTO;
import mouse.univ.lab3.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book fromCreateDTO(BookCreateDTO dto) {
        Book book = new Book();
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        return book;
    }

    public BookResponseDTO toResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setAuthor(book.getAuthor());
        dto.setTitle(book.getTitle());
        return dto;
    }

    public Book fromUpdateDTO(BookUpdateDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        return book;
    }
}
