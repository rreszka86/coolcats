package com.coolcats.coolcats.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "PostTitle", nullable = false)
    private String title;

    @JoinColumn(name = "CreatedAt", nullable = false)
    private Timestamp createdAt;

    @JoinColumn(name = "PostLikes", nullable = false)
    private int likes;

    @JoinColumn(name = "PicturePath", nullable = false)
    private String picturePath;

    @JoinColumn(name = "PostStatus", nullable = false)
    private int status;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
}
