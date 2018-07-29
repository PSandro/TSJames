package eu.psandro.tsjames.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "james_user_data")
public final class UserData implements Serializable {

    UserData() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private long dataId;

    @Setter
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

}
