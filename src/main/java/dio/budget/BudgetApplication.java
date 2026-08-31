package dio.budget;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class BudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetApplication.class, args);
    }

}
