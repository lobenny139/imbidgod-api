import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
		// swagger config@local
		com.imbidgod.api.config.SwaggerConfig.class,
		// JWT config@local
		com.imbidgod.api.config.WebSecurityConfig.class,
		// config@tom-db-service
		com.imbidgod.db.beanServiceConfig.ServiceAccessConfig.class,
		////config@tom-redis-service
//		com.tom.redis.beanServiceConfig.ServiceAccessConfig.class
})

@ComponentScan	(basePackages = {
		// controller@local
		"com.imbidgod.api.controller",
		"com.imbidgod.jwt",
		"com.imbidgod.jwt.service",
		// aop@local
		"com.imbidgod.api.aop",

		// aop@imbidgod-db-service
		"com.imbidgod.db.service.aop",

		// table access service@tom-db-service
		"com.imbidgod.db.service.provider" ,
		"com.imbidgod.asyncService.provider",
		"com.imbidgod.cronService.provider",

		// cache service@tom-db-service
		//"com.imbidgod.cache.service.provider"
})

@EnableJpaRepositories(
		// repositiry@tom-db-service
		"com.imbidgod.db.repository"
)

@EntityScan(basePackages = {
		// entity@tom-db-entity
		"com.imbidgod.db.entity"
})

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
