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
@Table(name = "james_rank")
@EqualsAndHashCode
public final class RankData implements Serializable {

    public static final RankData DEFAULT = new RankData();

    static {
        DEFAULT.setSimpleName("Default");
        DEFAULT.addPermission("human");
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
    @CollectionTable(name = "james_rank_permission")
    @Getter
    private Set<String> permissions = new HashSet<>();


    private void addPermission(@NonNull String permission) {
        this.permissions.add(permission);
    }


}
