package com.example.gestao_consultas.controller;

import com.example.gestao_consultas.model.Pessoa;
import com.example.gestao_consultas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PessoaController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Pessoa> getAllPessoas() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoaById(@PathVariable Long id) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        return pessoa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pessoa createPessoa(@RequestParam("nome") String nome,
                               @RequestParam("cpf") String cpf,
                               @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setCpf(cpf);

        if (foto != null && !foto.isEmpty()) {
            String fileName = savePhoto(foto);
            pessoa.setFoto(fileName);
        } else {
            pessoa.setFoto("default.jpg"); // ou qualquer outro valor padrão
        }

        return pessoaRepository.save(pessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable Long id,
                                               @RequestParam("nome") String nome,
                                               @RequestParam("cpf") String cpf,
                                               @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isPresent()) {
            Pessoa existingPessoa = pessoaOptional.get();
            existingPessoa.setNome(nome);
            existingPessoa.setCpf(cpf);

            if (foto != null && !foto.isEmpty()) {
                String fileName = savePhoto(foto);
                existingPessoa.setFoto(fileName);
            }

            return ResponseEntity.ok(pessoaRepository.save(existingPessoa));
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        if (pessoaRepository.existsById(id)) {
            pessoaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> getPessoaByCpf(@PathVariable String cpf) {
        Optional<Pessoa> pessoa = pessoaRepository.findByCpf(cpf);
        return pessoa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        // Gera um nome de arquivo único usando UUID
        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // Cria o diretório se não existir
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Salva o arquivo no diretório definido
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(photo.getInputStream(), filePath);

        return fileName;
    }

}
