package eu.psandro.tsjames.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "james_user_data")
public final class UserData implements Serializable {

    protected UserData() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private long dataId;

    @Setter
    @Column(name = "teamspeak_id", nullable = true, insertable = true, updatable = true)
    private String teamspeakId;
}
