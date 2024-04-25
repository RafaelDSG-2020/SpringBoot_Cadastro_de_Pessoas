package com.crud.cadastrousuario.domain.repository;

import com.crud.cadastrousuario.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {
}