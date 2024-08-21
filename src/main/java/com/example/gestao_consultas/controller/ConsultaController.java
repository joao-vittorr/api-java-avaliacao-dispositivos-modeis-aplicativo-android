package com.example.gestao_consultas.controller;

import com.example.gestao_consultas.model.Consulta;
import com.example.gestao_consultas.model.Pessoa;
import com.example.gestao_consultas.repository.ConsultaRepository;
import com.example.gestao_consultas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isPresent()) {
            return ResponseEntity.ok(consultaOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createConsulta(@RequestBody Consulta consulta) {
        if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
            Optional<Pessoa> pacienteOpt = pessoaRepository.findById(consulta.getPaciente().getId());
            if (pacienteOpt.isPresent()) {
                consulta.setPaciente(pacienteOpt.get());
                consultaRepository.save(consulta);
                return ResponseEntity.ok("Consulta criada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Paciente não encontrado.");
            }
        } else {
            return ResponseEntity.badRequest().body("Paciente não fornecido.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateConsulta(@PathVariable Long id, @RequestBody Consulta updatedConsulta) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isPresent()) {
            Consulta consulta = consultaOpt.get();
            consulta.setDescricao(updatedConsulta.getDescricao());
            consulta.setMedico(updatedConsulta.getMedico());
            consulta.setDataHora(updatedConsulta.getDataHora());

            if (updatedConsulta.getPaciente() != null && updatedConsulta.getPaciente().getId() != null) {
                Optional<Pessoa> pacienteOpt = pessoaRepository.findById(updatedConsulta.getPaciente().getId());
                if (pacienteOpt.isPresent()) {
                    consulta.setPaciente(pacienteOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("Paciente não encontrado.");
                }
            }

            consultaRepository.save(consulta);
            return ResponseEntity.ok("Consulta atualizada com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsulta(@PathVariable Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isPresent()) {
            try {
                consultaRepository.deleteById(id);
                return ResponseEntity.ok("Consulta excluída com sucesso");
            } catch (Exception e) {
                // Log the exception and return an appropriate response
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao excluir a consulta.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
