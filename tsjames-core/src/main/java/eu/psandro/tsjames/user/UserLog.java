package eu.psandro.tsjames.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_log")
public class UserLog {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "log_id")
    private final int logId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private final User user;

    @Column(name = "log_service")
    private final String logService;

    @Column(name = "log_action")
    private final String logAction;

    @Column(name = "log_result")
    private final String logResult;
}
