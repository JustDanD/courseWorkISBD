package itmo.zavar.isbdcyberpunk.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String role;

    @NotBlank
    @Size(min = 6, max = 40)
    @Getter
    @Setter
    private String password;
}
