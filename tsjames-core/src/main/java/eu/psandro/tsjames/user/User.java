package eu.psandro.tsjames.user;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "james_user")
@EqualsAndHashCode
public class User implements Serializable {

    User() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "user_id", unique = true, nullable = false)
    private int userId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation")
    private Date creation;

    @Column(name = "username", nullable = false, unique = true)
    @Setter
    private String username;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserData userData;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserLog userLog;


}
