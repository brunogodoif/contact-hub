package br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "professionals")
public class ProfessionalEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Date birthDate;

    @OneToMany(mappedBy = "professional", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<ContactEntity> contacts;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

//    @Override
//    public String toString() {
//        return "ProfessionalEntity{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", role='" + role + '\'' +
//                ", birthDate=" + birthDate +
//                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
//                '}';
//    }
}