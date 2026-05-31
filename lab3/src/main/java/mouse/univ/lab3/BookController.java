package mouse.univ.lab3;

import lombok.AllArgsConstructor;
import mouse.univ.lab3.dto.BookCreateDTO;
import mouse.univ.lab3.dto.BookResponseDTO;
import mouse.univ.lab3.dto.BookUpdateDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookCreateDTO createDTO) {
        return service.createBook(createDTO);
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable Long id) {
        return service.getBookById(id);
    }

    @GetMapping
    public List<BookResponseDTO> getBooks(@RequestParam(required = false) String author) {
        if (author != null && !author.isBlank()) {
            return service.getBooksByAuthor(author);
        }

        return service.getAll();
    }

    @PutMapping
    public BookResponseDTO updateBook(@RequestBody BookUpdateDTO updateDTO) {
        return service.updateBook(updateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteBook(id);
    }

}
