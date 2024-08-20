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

    @Scheduled(fixedRate = 60000) // Executa a cada 1 minuto
    public void enviarNotificacoesConsultasProximas() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime trintaMinutosDepois = agora.plusMinutes(30);

        // Encontrar consultas que estão agendadas para os próximos 30 minutos
        List<Consulta> consultasProximas = consultaRepository.findByDataHoraBetween(agora, trintaMinutosDepois);

        for (Consulta consulta : consultasProximas) {
            // Verificar se a notificação para essa consulta já foi enviada
            if (notificacaoRepository.findByConsultaId(consulta.getId()).isEmpty()) {
                Notificacao notificacao = new Notificacao();
                notificacao.setMensagem("Lembrete: Consulta com " + consulta.getMedico() + " às " + consulta.getDataHora().toLocalTime());
                notificacao.setDataHoraEnvio(agora);
                notificacao.setConsulta(consulta);
                notificacaoRepository.save(notificacao);
            }
        }
    }
}
