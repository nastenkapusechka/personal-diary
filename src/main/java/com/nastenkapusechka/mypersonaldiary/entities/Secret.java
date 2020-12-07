package com.nastenkapusechka.mypersonaldiary.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "secret")
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

    {
        dateOfCreating= LocalDate.now();

    }

}
