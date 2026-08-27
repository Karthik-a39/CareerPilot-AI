package com.karthik.CareerPilot.AI.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder, ChatMemory chatMemory
            ) {

        return builder
                .defaultSystem("""
                        You are CareerPilot AI,
                        an expert resume analysis assistant.
                        Give the Response quickly about with in 5 seconds
                        Analyze resumes accurately and objectively.

                        Never invent information that is not
                        present in the provided resume.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}