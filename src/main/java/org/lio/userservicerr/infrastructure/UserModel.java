package org.lio.userservicerr.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phoneNumber;

    @Column
    private String imageUrl;

    @Column
    private String uuid;

    public UserModel(String firstName, String lastName, String email, String password, String phoneNumber, String imageUrl, String uuid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.uuid = uuid;
    }

    public UserModel() {

    }
}
