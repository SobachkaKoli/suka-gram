package com.example.sukagram.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "likeOfPost")
public class LikeToPost {

    @Id
    private String id;
    @DBRef
    private User author;
    @DBRef
    private Post post;
}
