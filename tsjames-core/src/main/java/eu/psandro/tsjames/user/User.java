package eu.psandro.tsjames.user;


import eu.psandro.tsjames.rank.RankData;
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


    @NaturalId(mutable = true)
    @Column(name = "username", nullable = false, unique = true, insertable = true)
    @Setter
    private String username;

    @Column(name = "email", nullable = false, unique = true, insertable = true)
    @Setter
    private String email;

    @Column(name = "passwd", nullable = false, unique = false, insertable = true)
    @Setter
    private String passwordHash;

    @JoinColumn(name = "data_id", unique = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false, targetEntity = UserData.class)
    private UserData userData = new UserData();

    @JoinColumn(name = "rank_id", unique = true, insertable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    private RankData userRank;

    protected User(@NonNull String username, @NonNull String email, @NonNull String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
