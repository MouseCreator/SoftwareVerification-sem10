package mouse.univ.lab3;


import mouse.univ.lab3.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> getBooksByAuthor(String author);
}
