package mouse.univ.lab3;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mouse.univ.lab3.dto.BookCreateDTO;
import mouse.univ.lab3.dto.BookResponseDTO;
import mouse.univ.lab3.dto.BookUpdateDTO;
import mouse.univ.lab3.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper bookMapper;

    private final BookRepository bookRepository;

    public BookResponseDTO createBook(BookCreateDTO createDTO) {
        Book book = bookMapper.fromCreateDTO(createDTO);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseDTO(saved);
    }

    public BookResponseDTO getBookById(Long id) {
        Optional<Book> bookById = bookRepository.findById(id);
        if (bookById.isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + "not found");
        }
        Book book = bookById.get();
        return bookMapper.toResponseDTO(book);
    }

    public List<BookResponseDTO> getBooksByAuthor(String author) {
        List<Book> booksByAuthor = bookRepository.getBooksByAuthor(author);
        return booksByAuthor.stream().map(bookMapper::toResponseDTO).toList();
    }

    public BookResponseDTO updateBook(BookUpdateDTO updateDTO) {
        Book book = bookMapper.fromUpdateDTO(updateDTO);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseDTO(saved);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookResponseDTO> getAll() {
        return bookRepository.findAll().stream().map(bookMapper::toResponseDTO).toList();
    }
}
