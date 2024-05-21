package com.crud.cadastrousuario.domain.service;

import com.crud.cadastrousuario.domain.dto.AuthorDTO;


import com.crud.cadastrousuario.domain.exception.BadRequestException;
import com.crud.cadastrousuario.domain.exception.NotFoundException;
import com.crud.cadastrousuario.domain.model.Author;

import com.crud.cadastrousuario.domain.model.Book;
import com.crud.cadastrousuario.domain.repository.AuthorRepository;
import com.crud.cadastrousuario.domain.repository.AuthorRepositorySpec;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CrudAuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final Integer STATUS_FLAG_ACTIVE = 1;

    private final Integer STATUS_FLAG_INACTIVE = 0;

    private String bookServiceUrl = "http://library-gateway:8082/book-ms/api/v1/books";



    public List<AuthorDTO> findAuthor(Pageable pageable, AuthorDTO filter) {

        log.info("Executed the process of searching for author paged user in the database, paeable={} ", pageable);

        Page<Author> pageAuthor = authorRepository.findAll(
                AuthorRepositorySpec.filter(filter),
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize()));

        List<Author> authors = pageAuthor.getContent();

        return authors.stream()
                .map(author -> new AuthorDTO(author))
                .collect(Collectors.toList());


    }

    public AuthorDTO findAuthorByIDActive(Long id) {

        log.info("Executed the process of searching for author by id in the database");

        Author authorSave  = isIdAndFlagActive(id);
        return  new AuthorDTO(authorSave);

    }

    public AuthorDTO save(AuthorDTO authorCreateDTO) {

       log.info("Executed the process of saving author to the database");

        Author author = new Author(authorCreateDTO);
        author = authorRepository.save(author);
        return new AuthorDTO(author);

    }

    public AuthorDTO updateAuthorByID(Long id, AuthorDTO authorCreateDTO) {

        log.info("Executed the process of updating author by id in the database");

        Author author = new Author(authorCreateDTO);
        isIdAndFlagActive(id);
        author.setId(id);
        author = authorRepository.save(author);
        return new AuthorDTO(author);

    }

    public void deleteAuthorByID(Long id)  {

        log.info("Executed the process of delete author by id in the database");

        Author author = isIdAndFlagActive(id);
        author.setFlag(STATUS_FLAG_INACTIVE);
        authorRepository.save(author);
    }

    public Author isIdAndFlagActive(Long id) {

        log.info("Executed the process of validating author id in the database");

        return authorRepository.findByIdAndFlagEquals(id , STATUS_FLAG_ACTIVE)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " does not exist or Flag inactive."));



    }

    public AuthorDTO isAuthorAvailable() {

        Pageable pageable = PageRequest.of(0,1);
        Page<Author> page = authorRepository.findAll(pageable);
         List<Author> authors = page.getContent();
         if (authors.isEmpty()) {
             throw new BadRequestException("There are no authors");
         }

         return new AuthorDTO(authors.get(0));

    }


    @Transactional
    public Author addBookToAuthor(Long bookId, Long authorId) {

        Book book = restTemplate.getForObject(bookServiceUrl + "/" + bookId, Book.class);
        Optional.ofNullable(book).orElseThrow(() -> new BadRequestException("The book does not exist"));
      //  Book book = bookRepository.findById(bookId).orElseThrow();
        Author author = authorRepository.findById(authorId).orElseThrow();

        book.getAuthors().add(author);
        author.getBooks().add(book);


        authorRepository.save(author);
        return author;
    }
}
