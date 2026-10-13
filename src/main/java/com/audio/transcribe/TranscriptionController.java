package com.audio.transcribe;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/transcribe")
@CrossOrigin(origins = "http://localhost:5173/")
public class TranscriptionController {

    private final WhisperService whisperService;
    private final AiService aiService;


    public TranscriptionController(WhisperService whisperService, AiService aiService) {
        this.whisperService = whisperService;
        this.aiService = aiService;
    }

    @PostMapping
    public String transcribeAudio(@RequestParam("file") MultipartFile file)throws Exception{
        Path audioFile= Files.createTempFile("audio",".wav");
        file.transferTo(audioFile.toFile());

        String transcription = whisperService.transcribe(audioFile);

        String cleanedTranscription = aiService.cleanTranscript(transcription);

        return cleanedTranscription;

    }
}
