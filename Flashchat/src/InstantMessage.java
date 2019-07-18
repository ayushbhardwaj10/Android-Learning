package com.ab.flashchatnewfirebase;

public class InstantMessage {
    private String Message;
    private String Author;

    public InstantMessage(String message, String author) {
        Message = message;
        Author = author;
    }

    public InstantMessage() {

    }

    public String getMessage() {
        return Message;
    }

    public String getAuthor() {
        return Author;
    }
}
