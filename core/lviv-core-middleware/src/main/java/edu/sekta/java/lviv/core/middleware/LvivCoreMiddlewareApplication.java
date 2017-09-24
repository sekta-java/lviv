package edu.sekta.java.lviv.core.middleware;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class LvivCoreMiddlewareApplication {
	@Bean
	CommandLineRunner commandLineRunner(BookRepository bookRepository) {
		return (args) -> {
			Book b = new Book();
			b.setTitle("Book 1");
			Author a1 = new Author();
			Author a2 = new Author();
			a1.setName("A1");
			a2.setName("A2");
			a1.getBooks().add(b);
			a2.getBooks().add(b);
			b.getAuthors().add(a1);
			b.getAuthors().add(a2);

			bookRepository.save(b);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(LvivCoreMiddlewareApplication.class, args);
	}

}

@RestController
@RequestMapping("/api/books")
class BookController {
	private final BookRepository bookRepository;

	@Autowired
	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@GetMapping
	public Page<Book> getAll(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}
}

@ControllerAdvice
class RestExceptionHandler {
	@ExceptionHandler(NotImplementedException.class)
	public void notAllowed(Throwable throwable, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(Exception.class)
	public void serverError(Throwable throwable, HttpServletResponse response) throws IOException {
		response.getOutputStream().println("{\"message\": \"Server temporary unavailable\"}");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
	}
}

@Repository
interface BookRepository extends JpaRepository<Book, Long>{
}


@Entity
@Data
class Book {
	@Id
	@GeneratedValue
	private Long id;
	private String title;

	@ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
	private List<Author> authors = new ArrayList<>();
}

@Entity
@Data
class Author {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "author_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	private List<Book> books = new ArrayList<>();
}