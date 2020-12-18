package com.nastenkapusechka.mypersonaldiary.entities;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@Table(name = "secrets")
@ToString
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title shouldn't be empty!")
    private String title;

    @NotBlank(message = "Content shouldn't be empty!")
    @Size(max = 1000, message = "Too long. >1000 symbols")
    @Column(length = 1000)
    private String content;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreating;

    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Secret secret = (Secret) o;
        return id == secret.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
