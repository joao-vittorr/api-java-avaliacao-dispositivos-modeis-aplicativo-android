package com.example.gestao_consultas.controller;

import com.example.gestao_consultas.model.Consulta;
import com.example.gestao_consultas.model.Pessoa;
import com.example.gestao_consultas.repository.ConsultaRepository;
import com.example.gestao_consultas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@RequestBody Consulta consulta) {
        if (consulta.getPaciente() != null && consulta.getPaciente().getId() != null) {
            Optional<Pessoa> pacienteOpt = pessoaRepository.findById(consulta.getPaciente().getId());
            if (pacienteOpt.isPresent()) {
                consulta.setPaciente(pacienteOpt.get());
                Consulta savedConsulta = consultaRepository.save(consulta);
                return ResponseEntity.ok(savedConsulta);
            } else {
                return ResponseEntity.badRequest().body(null); // Paciente não encontrado
            }
        } else {
            return ResponseEntity.badRequest().body(null); // Paciente não fornecido
        }
    }
}


