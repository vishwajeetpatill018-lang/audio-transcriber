package com.audio.transcribe;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();

    }

    public String cleanTranscript(String transcript) {
        return chatClient.prompt().user(
                """
                        Clean up the following transcript.
                        Remove unnecessary filler words such as 'uh' ,'um' and repeated words.
                        Correct Obvious grammar and punctional mistakes.
                        Do not change the meaniing of the transcript.
                        Return only the clean transcript.
                        
                        Transcript:
                        %s
                        """.formatted(transcript)).call().content();
    }
}


