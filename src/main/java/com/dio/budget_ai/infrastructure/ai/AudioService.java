package com.dio.budget_ai.infrastructure.ai;

import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AudioService {

    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    public AudioService(
            TranscriptionModel transcriptionModel,
            TextToSpeechModel textToSpeechModel) {

        this.transcriptionModel = transcriptionModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    /**
     * Converte um arquivo de áudio em texto.
     */
    public String transcrever(Resource audio) {

        try {

            return transcriptionModel.transcribe(audio);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Não foi possível transcrever o áudio. "
                            + "Verifique os créditos da API.",
                    e
            );
        }
    }

    /**
     * Converte uma resposta em texto para áudio.
     */
    public byte[] gerarAudio(String texto) {

        try {

            return textToSpeechModel.call(texto);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Não foi possível gerar o áudio. "
                            + "Verifique os créditos da API.",
                    e
            );
        }
    }
}