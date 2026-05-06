package com.example.a81.network;

import java.util.ArrayList;
import java.util.List;

public class OpenAIRequest {

    public String model;
    public List<Message> messages;
    public double temperature = 0.7;

    public OpenAIRequest(List<Message> messages) {
        this.model = "https://clarifai.com/openai/chat-completion/models/gpt-oss-20b/versions/57d6764afaf6450383ba5b6231e3f979";
        this.messages = messages;
    }

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}