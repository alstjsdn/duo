package org.duo.duo.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String riotId;

    @Column(nullable = false)
    private String riotTag;

    @Column(length = 200)
    private String bio;

    private String profileImage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public User(String username, String password, String name, Role role, String riotId, String riotTag) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role != null ? role : Role.USER;
        this.riotId = riotId;
        this.riotTag = riotTag;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfile(String bio, String profileImage) {
        this.bio = bio;
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }
}