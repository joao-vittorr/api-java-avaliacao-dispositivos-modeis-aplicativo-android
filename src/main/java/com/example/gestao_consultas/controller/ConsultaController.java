package com.example.gestao_consultas.controller;

import com.example.gestao_consultas.model.Consulta;
import com.example.gestao_consultas.model.Notificacao;
import com.example.gestao_consultas.repository.ConsultaRepository;
import com.example.gestao_consultas.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @PostMapping
    public ResponseEntity<String> createConsulta(@RequestBody Consulta consulta) {
        // (mesmo código de antes)

        // Adiciona notificação após criação
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem("Nova consulta agendada: " + consulta.getDescricao());
        notificacao.setDataHoraEnvio(LocalDateTime.now());
        notificacaoRepository.save(notificacao);

        return ResponseEntity.ok("Consulta criada com sucesso e notificação enviada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConsulta(@PathVariable Long id, @RequestBody Consulta consultaDetails) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isPresent()) {
            Consulta consulta = consultaOpt.get();
            consulta.setDescricao(consultaDetails.getDescricao());
            consulta.setMedico(consultaDetails.getMedico());
            consulta.setDataHora(consultaDetails.getDataHora());
            consultaRepository.save(consulta);

            Notificacao notificacao = new Notificacao();
            notificacao.setMensagem("Consulta atualizada: " + consulta.getDescricao());
            notificacao.setDataHoraEnvio(LocalDateTime.now());
            notificacaoRepository.save(notificacao);

            return ResponseEntity.ok("Consulta atualizada com sucesso e notificação enviada");
        } else {
            return ResponseEntity.badRequest().body("Consulta não encontrada.");
        }
    }
}
