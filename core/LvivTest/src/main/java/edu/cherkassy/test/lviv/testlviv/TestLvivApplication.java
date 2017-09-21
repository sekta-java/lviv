package edu.cherkassy.test.lviv.testlviv;

import edu.cherkassy.test.lviv.testlviv.service.BookService;
import edu.cherkassy.test.lviv.testlviv.service.impls.BookServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestLvivApplication {
    public static void main(String[] args) {
        BookService repository = SpringApplication
                .run(TestLvivApplication.class, args)
                .getBean(BookServiceImpl.class);

        if (repository != null) {
            System.out.println("\n*****TEST getAllBooks() method: ******");
            repository.getAllBooks().forEach(System.out::println);

            System.out.println("\n*****TEST  getBookById() method: *****");
            System.out.println(repository.getBookById(2));
            System.out.println(repository.getBookById(1));
        }
    }
}
