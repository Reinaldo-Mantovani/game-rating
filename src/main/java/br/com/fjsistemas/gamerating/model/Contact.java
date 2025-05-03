package br.com.fjsistemas.gamerating.model;

import br.com.fjsistemas.gamerating.enuns.ContactType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "tb_contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Enumerated(EnumType.STRING)
    private ContactType type;
    @Column
    @NotBlank
    private String value;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Contact(){}

    public Contact(Long id, ContactType type, String value, User user) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.user = user;
    }

    public Contact(Long id, ContactType type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
