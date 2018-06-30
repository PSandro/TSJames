package eu.psandro.tsjames.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_log")
@EqualsAndHashCode
public class UserLog implements Serializable {


    UserLog(){}

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "log_id")
    private int logId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "log_service")
    private String logService;

    @Column(name = "log_action")
    private String logAction;

    @Column(name = "log_result")
    private String logResult;
}
