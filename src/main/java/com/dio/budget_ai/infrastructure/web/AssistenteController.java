package com.dio.budget_ai.infrastructure.web;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dio.budget_ai.application.ConsultarSaldoUseCase;
import com.dio.budget_ai.application.ListarTransacoesUseCase;
import com.dio.budget_ai.infrastructure.ai.AssistenteFinanceiroService;
import com.dio.budget_ai.infrastructure.ai.AudioService;

@RestController
@RequestMapping("/api")
public class AssistenteController {

    private final AssistenteFinanceiroService assistenteFinanceiroService;
    private final AudioService audioService;
    private final ConsultarSaldoUseCase consultarSaldoUseCase;
    private final ListarTransacoesUseCase listarTransacoesUseCase;

    public AssistenteController(
            AssistenteFinanceiroService assistenteFinanceiroService,
            AudioService audioService,
            ConsultarSaldoUseCase consultarSaldoUseCase,
            ListarTransacoesUseCase listarTransacoesUseCase) {

        this.assistenteFinanceiroService = assistenteFinanceiroService;
        this.audioService = audioService;
        this.consultarSaldoUseCase = consultarSaldoUseCase;
        this.listarTransacoesUseCase = listarTransacoesUseCase;
    }

    /*
     * Recebe uma mensagem em texto e envia para
     * o assistente financeiro.
     */
    @PostMapping("/assistente")
    public ResponseEntity<?> assistente(
            @RequestBody Map<String, String> request) {

        String mensagem = request.get("mensagem");

        if (mensagem == null || mensagem.isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "erro",
                                    "A mensagem é obrigatória."
                            )
                    );
        }

        String resposta =
                assistenteFinanceiroService
                        .processarMensagem(mensagem);

        return ResponseEntity.ok(
                Map.of(
                        "resposta",
                        resposta
                )
        );
    }

    /*
     * Fluxo completo:
     *
     * áudio
     * -> transcrição
     * -> assistente financeiro
     * -> resposta
     * -> áudio
     */
    @PostMapping(
            value = "/assistente/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "audio/mpeg"
    )
    public ResponseEntity<?> assistenteAudio(
            @RequestParam("audio") MultipartFile arquivo) {

        if (arquivo.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Map.of(
                                    "erro",
                                    "O arquivo de áudio é obrigatório."
                            )
                    );
        }

        try {

            // Converte o áudio enviado para texto
            String mensagem =
                    audioService.transcrever(
                            arquivo.getResource()
                    );

            // Processa a mensagem financeira
            String resposta =
                    assistenteFinanceiroService
                            .processarMensagem(mensagem);

            // Converte a resposta novamente para áudio
            byte[] audioResposta =
                    audioService.gerarAudio(resposta);

            return ResponseEntity
                    .ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    "audio/mpeg"
                            )
                    )
                    .body(audioResposta);

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            Map.of(
                                    "erro",
                                    e.getMessage()
                            )
                    );
        }
    }

    /*
     * Consulta o saldo diretamente.
     */
    @GetMapping("/saldo")
    public ResponseEntity<?> saldo() {

        return ResponseEntity.ok(
                Map.of(
                        "saldo",
                        consultarSaldoUseCase.executar()
                )
        );
    }

    /*
     * Lista todas as transações.
     */
    @GetMapping("/transacoes")
    public ResponseEntity<?> transacoes() {

        return ResponseEntity.ok(
                listarTransacoesUseCase.executar()
        );
    }
}