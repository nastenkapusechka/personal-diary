package com.nastenkapusechka.mypersonaldiary.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_registration")
    private LocalDate registrationDate;
    private String gender;

    @Column(name = "is_activated")
    private boolean isActivated;
    @Column(name = "activation_code")
    private String activationCode;

    @OneToMany(mappedBy = "user")
    //, cascade = CascadeType.ALL
    private List<Secret> secretList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
                joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActivated=" + isActivated +
                '}';
    }
}
