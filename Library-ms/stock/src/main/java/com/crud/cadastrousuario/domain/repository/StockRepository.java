package com.crud.cadastrousuario.domain.repository;

import com.crud.cadastrousuario.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long>  {
  

    Optional<Stock> findTopByBookId(Long bookID);
}