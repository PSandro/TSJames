package eu.psandro.tsjames.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter

@Table(name = "user_data")
public final class UserData {

    @OneToOne
    @JoinColumn(name = "user_id")
    private final User user;

    @Setter
    @Column(name = "teamspeak_id")
    private String teamspeakId;
}
