package eu.psandro.tsjames.user;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
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
    @Column(name = "user_id", unique = true, nullable = false, insertable = true, updatable = false)
    private long userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation", nullable = false, insertable = true)
    @Setter(AccessLevel.PROTECTED)
    private Date creation;

    @NaturalId(mutable = true)
    @Column(name = "username", nullable = false, unique = true, insertable = true)
    @Setter
    private String username;

    @JoinColumn(name = "data_id", unique = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private UserData userData;

    protected User(@NonNull Date creation, @NonNull String username, @NonNull UserData userData) {
        this.creation = creation;
        this.username = username;
        this.userData = userData;
    }

}
