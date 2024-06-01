package io.github.busy_spin.fix_showcase;

import io.github.busy_spin.fix_showcase.commands.AcceptorCommands;
import io.github.busy_spin.fix_showcase.commands.InitiatorCommands;
import org.jline.utils.AttributedString;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.annotation.EnableCommand;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@EnableCommand({
		InitiatorCommands.class,
		AcceptorCommands.class
})
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
