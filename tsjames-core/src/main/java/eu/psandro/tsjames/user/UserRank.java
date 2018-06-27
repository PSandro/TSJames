package eu.psandro.tsjames.user;


import eu.psandro.tsjames.rank.RankData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "user_rank")

public final class UserRank {

    @OneToOne
    @JoinColumn(name = "user_id")
    private final User user;

    @OneToOne
    @JoinColumn(name = "rank_id")
    private RankData rank;
}
