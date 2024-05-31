package io.github.busy_spin.fix_showcase;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import java.sql.SQLOutput;
import java.util.HashMap;

@SpringBootApplication
public class FixProtocolShowcaseApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(FixProtocolShowcaseApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

	@Bean
	public PromptProvider myPromptProvider() {
		return new PromptProvider() {
			@Override
			public AttributedString getPrompt() {
				return new AttributedString("fix-shell>");
			}
		};
	}
}
