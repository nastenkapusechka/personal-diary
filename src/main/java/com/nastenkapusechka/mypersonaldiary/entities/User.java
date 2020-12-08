package com.nastenkapusechka.mypersonaldiary.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username is empty!")
    private String username;
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String password;

    @Transient
    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String repeatPassword;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    //private LocalDate birth;
    private char gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Secret> secretList;

    /*{
        this.birth = LocalDate.now();
    }*/
}
