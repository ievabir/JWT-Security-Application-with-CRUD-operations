package lt.vu.security.auth;

import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationRequest {

    private String email;
    String password;
}
