package com.example.gestao_consultas.repository;

import com.example.gestao_consultas.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
}
