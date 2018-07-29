package eu.psandro.tsjames.rank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author PSandro on 29.07.18
 * @project tsjames
 */

@Entity
@EqualsAndHashCode
@Table(name = "rank_permission")
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class RankPermission implements Serializable {

    //TODO add caching
    @Id
    @Column(name = "perm_name", nullable = false, unique = true, insertable = false, updatable = false)
    private String name;

}
