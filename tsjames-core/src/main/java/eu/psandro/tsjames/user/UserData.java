package eu.psandro.tsjames.user;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Entity
@Table(name = "james_user_data")
public final class UserData implements Serializable {



    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    private long dataId;


    @OneToOne
    @PrimaryKeyJoinColumn
    @Setter
    private User user;

    @Setter
    @NaturalId
    @Column(name = "teamspeak_id", nullable = true, insertable = true, updatable = true)
    private String teamspeakId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation", nullable = false, insertable = true)
    @Setter(AccessLevel.PROTECTED)
    private Date creation;

    @PrePersist
    protected void onCreate() {
        this.creation = new Date();
    }

    @Override
    public String toString() {
        return "UserData{" +
                "dataId=" + dataId +
                ", teamspeakId='" + teamspeakId + '\'' +
                ", creation=" + creation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return dataId == userData.dataId &&
                Objects.equals(user.getUserId(), userData.user.getUserId()) &&
                Objects.equals(teamspeakId, userData.teamspeakId) &&
                Objects.equals(creation, userData.creation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, user.getUserId(), teamspeakId, creation);
    }
}
