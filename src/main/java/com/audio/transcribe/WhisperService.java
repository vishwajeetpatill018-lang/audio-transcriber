package com.audio.transcribe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class WhisperService {

    // Path of the Whisper executable.
    @Value("${whisper.executable}")
    private String whisperExecutable;

    // Path of the Whisper model.
    @Value("${whisper.model}")
    private String whisperModel;

    public String transcribe(Path audioFile) throws Exception {

        // Build the Whisper command.
        // -m  -> Whisper model
        // -f  -> audio file
        // -l  -> English language
        // -nt -> no timestamps
        // -np -> reduce Whisper's console output
        ProcessBuilder processBuilder = new ProcessBuilder(
                whisperExecutable,
                "-m", whisperModel,
                "-f", audioFile.toString(),
                "-l", "en",
                "-nt",
                "-np"
        );

        // Start Whisper.
        Process process = processBuilder.start();

        // Read everything Whisper sends to its output.
        String output = new String(
                process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        // Wait for Whisper to finish.
        process.waitFor();

        // Find the actual transcription.
        // Whisper's transcription appears after the "processing" output.
        String[] lines = output.split("\\R");

        StringBuilder transcription = new StringBuilder();

        for (String line : lines) {

            // Ignore Whisper's diagnostic lines.
            if (line.startsWith("read_audio_data:")
                    || line.startsWith("system_info:")
                    || line.startsWith("main:")
                    || line.startsWith("whisper_")) {
                continue;
            }

            // Keep actual spoken text.
            if (!line.trim().isEmpty()) {
                transcription.append(line.trim()).append(" ");
            }
        }

        return transcription.toString().trim();
    }
}