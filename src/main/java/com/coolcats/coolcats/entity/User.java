package com.coolcats.coolcats.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UserName", nullable = false, unique = true)
    private String username;

    @Column(name = "UserPasswd", nullable = false)
    private String passwd;

    @Column(name = "UserEmail", nullable = false, unique = true)
    private String email;

    @Column(name = "UserStatus")
    private String status;

    @Column(name = "JoinDate")
    @CreationTimestamp
    private Timestamp joinDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
}
