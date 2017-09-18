package edu.cherkassy.test.lviv.testlviv;

import edu.cherkassy.test.lviv.testlviv.repository.impls.BookRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestLvivApplication {
    public static void main(String[] args) {
        BookRepositoryImpl repository = SpringApplication.run(TestLvivApplication.class, args).getBean(BookRepositoryImpl.class);
        if (repository != null) {
            System.out.println("Hello");
            repository.getAllBooks().forEach(System.out::println);
        }
    }
}
