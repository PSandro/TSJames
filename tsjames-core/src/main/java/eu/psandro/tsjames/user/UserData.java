package eu.psandro.tsjames.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@EqualsAndHashCode
@Table(name = "user_data")
public final class UserData implements Serializable {

    UserData(){}

    @OneToOne
    @JoinColumn(name = "user_id")
    @Id
    private User user;

    @Setter
    @Column(name = "teamspeak_id")
    private String teamspeakId;
}
