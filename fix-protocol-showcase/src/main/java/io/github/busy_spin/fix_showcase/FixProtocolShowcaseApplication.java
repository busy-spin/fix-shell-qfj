package io.github.busy_spin.fix_showcase;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FixProtocolShowcaseApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(FixProtocolShowcaseApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

}
