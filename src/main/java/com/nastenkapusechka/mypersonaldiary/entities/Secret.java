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
@Table(name = "secret")
@ToString
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title shouldn't be empty!")
    private String title;
    @NotBlank(message = "Content shouldn't be empty!")
    @Size(max = 255, message = "Too long. >255 symbols")
    private String content;
    @Column(name = "date_of_creation")
    private LocalDate dateOfCreating;

    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    {
        dateOfCreating= LocalDate.now();

    }

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
