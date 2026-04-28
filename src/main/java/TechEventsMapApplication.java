import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.technicalbeep.techeventsmap")
@ConfigurationPropertiesScan("com.technicalbeep.techeventsmap.config")
public class TechEventsMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechEventsMapApplication.class, args);
    }
}
