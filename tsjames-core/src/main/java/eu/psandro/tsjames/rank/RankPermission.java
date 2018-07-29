package eu.psandro.tsjames.rank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */

@Entity
@EqualsAndHashCode
@Table(name = "rank_permission")
@Getter
public final class RankPermission implements Serializable {
    //TODO add caching
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id")
    private long permissionId;

    @NaturalId
    @Column(name = "perm_name", nullable = false, unique = true)
    private String name;


    RankPermission(String name) {
        this.name = name;
    }

    RankPermission() {}

}
