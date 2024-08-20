package com.example.gestao_consultas.repository;

import com.example.gestao_consultas.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByConsultaId(Long consultaId);
}
