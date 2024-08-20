package com.example.gestao_consultas.repository;

import com.example.gestao_consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Consulta consultas dentro de um intervalo de datas
    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}
