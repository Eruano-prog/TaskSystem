package job.test.TaskSystem.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Task System Api",
                description = "Task Managment System", version = "1.0.0",
                contact = @Contact(
                        name = "Головлёв Фёдор",
                        email = "fdgolovlev@gmail.com",
                        url = "https://github.com/Eruano-prog"
                )
        )
)
public class ApiConfig {

}