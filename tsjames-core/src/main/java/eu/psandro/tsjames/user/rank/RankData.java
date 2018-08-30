package eu.psandro.tsjames.user.rank;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "rank_indicies")
@EqualsAndHashCode
public final class RankData implements Serializable {

    public static final RankData DEFAULT = new RankData();

    static {
        DEFAULT.setSimpleName("Default");
    }

    RankData() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "rank_id")
    private int rankId;

    @Setter
    @Column(name = "simple_name")
    private String simpleName;

    @Setter
    @Column(name = "teamspeak_group")
    private int teamspeakGroup;

    @ElementCollection
    @Column(name = "permissions")
    private Set<String> permissions = new HashSet<>();

}
