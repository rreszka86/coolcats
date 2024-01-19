package com.coolcats.coolcats.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int id;

    @JoinColumn(name = "UserName", nullable = false)
    private String name;

    @JoinColumn(name = "UserPasswd", nullable = false)
    private String passwd;

    @JoinColumn(name = "UserEmail", nullable = false)
    private String email;

    @JoinColumn(name = "UserStatus", nullable = false)
    private int status;

    @JoinColumn(name = "JoinDate", nullable = false)
    private Timestamp joinDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
}
