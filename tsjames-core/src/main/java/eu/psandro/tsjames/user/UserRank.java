package eu.psandro.tsjames.user;


import eu.psandro.tsjames.rank.RankData;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "user_rank")
@EqualsAndHashCode

public final class UserRank implements Serializable {

    UserRank() {}

    @OneToOne
    @JoinColumn(name = "user_id")
    @Id
    private User user;

    @OneToOne
    @JoinColumn(name = "rank_id")
    private RankData rank;
}
