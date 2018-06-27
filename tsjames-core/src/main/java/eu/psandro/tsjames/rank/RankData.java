package eu.psandro.tsjames.rank;


import eu.psandro.tsjames.user.UserRank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "rank_indicies")
public final class RankData {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JoinColumn(name = "rank_id")
    private final UserRank rank;

    @Setter
    @Column(name = "simple_name")
    private String simpleName;

    @Setter
    @Column(name = "teamspeak_group")
    private int teamspeakGroup;
}
