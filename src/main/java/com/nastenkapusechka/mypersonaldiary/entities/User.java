package com.nastenkapusechka.mypersonaldiary.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Data
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Username is empty!")
    private String username;

    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String password;

    @Transient
    @NotBlank(message = "Password is empty!")
    @Size(min = 8, message = "Too short. <8 symbols")
    private String repeatPassword;

    @NotBlank(message = "First name is empty!")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is empty!")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_registration")
    private LocalDate registrationDate;
    private String gender;

    @OneToMany(mappedBy = "user")
    //, cascade = CascadeType.ALL
    private List<Secret> secretList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
                joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
}
