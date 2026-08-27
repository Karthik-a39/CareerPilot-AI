package com.karthik.CareerPilot.AI.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final VectorStore vectorStore;


    public void injectKnowledge() throws IOException{
        List<Document> documents=new ArrayList<>();

        documents.add(
                loadDocument(
                        "knowledge/docker.txt",
                        "docker"
                )
        );

        documents.add(
                loadDocument(
                        "knowledge/spring-boot.txt",
                        "spring-boot"
                )
        );

        documents.add(
                loadDocument(
                        "knowledge/system-design.txt",
                        "system-design"
                )
        );

        TokenTextSplitter splitter=TokenTextSplitter.builder().build();

        List<Document> chunks=splitter.apply(documents);

        vectorStore.add(chunks);

    }


    public Document loadDocument(String path,String topic) throws  IOException{

        ClassPathResource resource=new ClassPathResource(path);
        String content=resource.getContentAsString(StandardCharsets.UTF_8);
        return new Document(
                content,
                java.util.Map.of(
                        "topic",topic,
                        "source",path
                )
        );
    }
}
