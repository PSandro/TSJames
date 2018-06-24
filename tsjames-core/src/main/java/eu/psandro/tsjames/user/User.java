package eu.psandro.tsjames.user;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "james_user")
public abstract class User {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "user_id")
    private final int userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation")
    private final Timestamp creation;

    @Column(name = "pseudonym")
    private String pseudonym;

}
