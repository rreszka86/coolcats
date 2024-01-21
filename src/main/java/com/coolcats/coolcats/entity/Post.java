package com.coolcats.coolcats.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PostTitle", nullable = false)
    private String title;

    @Column(name = "CreatedAt")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "PostLikes", nullable = false)
    private int likes;

    @Column(name = "PicturePath", nullable = false)
    private String picturePath;

    @Column(name = "PostStatus", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
}
