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
public class User {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "user_id")
    private final int userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation", nullable = false)
    private final Timestamp creation;

    @Column(name = "pseudonym", nullable = false)
    private String pseudonym;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserData userData;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserLog userLog;


}
