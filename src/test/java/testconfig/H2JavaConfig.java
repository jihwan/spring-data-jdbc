package testconfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	DataSourceAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class
})
public class H2JavaConfig {
}
