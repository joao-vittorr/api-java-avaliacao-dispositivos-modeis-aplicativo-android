package com.example.gestao_consultas.scheduler;

import com.example.gestao_consultas.model.Consulta;
import com.example.gestao_consultas.model.Notificacao;
import com.example.gestao_consultas.repository.ConsultaRepository;
import com.example.gestao_consultas.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConsultaScheduler {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Scheduled(fixedRate = 3600000) // Executa a cada 1 hora
    public void enviarNotificacoesConsultasProximas() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime umDiaAfront = agora.plusDays(1);

        List<Consulta> consultasProximas = consultaRepository.findByDataHoraBetween(agora, umDiaAfront);

        for (Consulta consulta : consultasProximas) {
            Notificacao notificacao = new Notificacao();
            notificacao.setMensagem("Lembrete: Consulta com " + consulta.getMedico() + " às " + consulta.getDataHora().toLocalTime());
            notificacao.setDataHoraEnvio(agora);
            notificacaoRepository.save(notificacao);
        }
    }
}
