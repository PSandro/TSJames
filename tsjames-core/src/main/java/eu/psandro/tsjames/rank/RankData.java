package eu.psandro.tsjames.rank;


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

    RankData() {
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int rankId;

    @Setter
    @Column(name = "simple_name")
    private String simpleName;

    @Setter
    @Column(name = "teamspeak_group")
    private int teamspeakGroup;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "perm_name", nullable = false, insertable = false, updatable = false)
    private Set<RankPermission> permissions = new HashSet<>();

}
