package io.github.busy_spin.fix_showcase;

import io.github.busy_spin.fix_showcase.commands.AcceptorCommandHandler;
import io.github.busy_spin.fix_showcase.commands.ColorTestCommandHandler;
import io.github.busy_spin.fix_showcase.commands.InitializerCommandHandler;
import org.jline.utils.AttributedString;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.annotation.EnableCommand;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@EnableCommand({
        InitializerCommandHandler.class,
        AcceptorCommandHandler.class,
        ColorTestCommandHandler.class
})
public class FixProtocolShowcaseApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FixProtocolShowcaseApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("fix-shell>");
    }
}
