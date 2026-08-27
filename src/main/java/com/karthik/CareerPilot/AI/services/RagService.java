package com.karthik.CareerPilot.AI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public String ask(String email,String question) {

        // 1. Search PGVector
        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(5)
                .similarityThreshold(0.7)
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(request);

        // 2. Convert retrieved documents into context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // 3. Create RAG prompt
        String prompt = """
        You are a helpful technical assistant.

        You have access to a knowledge base that may contain
        relevant information for the user's question.

        If the provided context contains relevant information,
        use it as the primary source for your answer.

        If the context does not contain enough information,
        you may answer using your general knowledge.

        Do not claim that information came from the knowledge
        base if it did not.

        Be accurate and clearly explain the answer.

        CONTEXT:
        %s

        USER QUESTION:
        %s
        """.formatted(context, question);
        // 4. Send context + question to Gemini
        return chatClient
                .prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,email))
                .call()
                .content();
    }
}