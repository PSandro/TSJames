package eu.psandro.tsjames.user;


import eu.psandro.tsjames.user.rank.RankData;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "james_user")
public class User implements Serializable {


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

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false, targetEntity = UserData.class, mappedBy = "user")
    private UserData userData;

    @JoinColumn(name = "rank_id", unique = true, insertable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    private RankData userRank;


    protected User(@NonNull String username, @NonNull String email, @NonNull String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(passwordHash, user.passwordHash) &&
                Objects.equals(userData.getDataId(), user.userData.getDataId()) &&
                Objects.equals(userRank, user.userRank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, passwordHash, userData.getDataId(), userRank);
    }
}
