package project.models;

import org.apache.shiro.crypto.hash.Hash;
import project.tools.PasswordEncryptor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users",
        uniqueConstraints=  @UniqueConstraint(columnNames = {"id", "username"}))

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    public User() { }

    public User(long id)
    {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;

        this.password = PasswordEncryptor.encrypt(password);
    }

    // Getter and setter methods

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

}
