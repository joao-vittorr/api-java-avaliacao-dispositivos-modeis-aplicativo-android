package com.example.gestao_consultas.controller;

import com.example.gestao_consultas.model.Consulta;
import com.example.gestao_consultas.model.Notificacao;
import com.example.gestao_consultas.model.Pessoa;
import com.example.gestao_consultas.repository.ConsultaRepository;
import com.example.gestao_consultas.repository.NotificacaoRepository;
import com.example.gestao_consultas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @GetMapping
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createConsulta(@RequestBody Consulta consulta) {
        if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
            Optional<Pessoa> pacienteOpt = pessoaRepository.findById(consulta.getPaciente().getId());
            if (pacienteOpt.isPresent()) {
                consulta.setPaciente(pacienteOpt.get());
                consultaRepository.save(consulta);

                // Criar notificação
                Notificacao notificacao = new Notificacao();
                notificacao.setMensagem("Nova consulta marcada com " + consulta.getMedico() + " em " + consulta.getDataHora());
                notificacao.setDataHoraEnvio(LocalDateTime.now());
                notificacaoRepository.save(notificacao);

                return ResponseEntity.ok("Consulta criada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Paciente não encontrado.");
            }
        } else {
            return ResponseEntity.badRequest().body("Paciente não fornecido.");
        }
    }
}
