package com.example.gestao_consultas.repository;

import com.example.gestao_consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
