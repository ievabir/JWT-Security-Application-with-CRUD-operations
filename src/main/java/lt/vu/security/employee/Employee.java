package lt.vu.security.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.vu.security.user.User;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;
    private String position;
    private String title;

    @ManyToOne
    @JoinColumn(referencedColumnName = "email")
    private User reportsTo;

    @JsonIgnoreProperties({"firstName", "lastname", "email", "password", "role", "enabled", "username", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})


    public User getReportsTo() {
        return reportsTo;
    }



}
