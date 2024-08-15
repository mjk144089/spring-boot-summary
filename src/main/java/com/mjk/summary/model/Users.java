package com.mjk.summary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;

@Entity
@Table(name = "users")
@ToString
@Data
public class Users {
    @Id
    private String uid;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column
    private Date birth;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('male','female','other','prefer_not_to_say') default 'prefer_not_to_say'")
    private Gender gender;

    @Column(length = 255)
    private String email;

    @Column(name = "profile_img", columnDefinition = "text")
    private String profileImg;

    @Column(name = "is_social_login", columnDefinition = "tinyint(1) default '0'")
    private boolean isSocialLogin;

    public enum Gender {
        male, female, other, prefer_not_to_say
    }
}
