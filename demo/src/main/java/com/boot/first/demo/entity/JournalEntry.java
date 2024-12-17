package com.boot.first.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "journaldb")
public class JournalEntry {

    @Id
    private ObjectId id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime date;
}
