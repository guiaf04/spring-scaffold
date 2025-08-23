package com.scaffold;

import com.scaffold.commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "spring-scaffold",
    mixinStandardHelpOptions = true,
    version = "Spring Scaffold CLI 2.0.0",
    description = {
        "@|bold Spring Scaffold CLI v2.0.0|@",
        "",
        "🚀 FUNCTIONAL code generator for Spring Boot applications.",
        "Automatically creates models, controllers, services, repositories and complete projects.",
        "",
        "@|underline Available commands:|@",
        "  model      - Generate model/entity classes with JPA and validations",
        "  controller - Generate REST controllers with complete CRUD",
        "  service    - Generate service classes with interfaces",
        "  repository - Generate JPA repositories with custom queries",
        "  security   - Generate Spring Security configuration with JWT",
        "  project    - Create complete Spring Boot projects"
    },
    subcommands = {
        ModelCommand.class,
        ControllerCommand.class,
        ServiceCommand.class,
        RepositoryCommand.class,
        SecurityCommand.class,
        ProjectCommand.class,
        CommandLine.HelpCommand.class
    },
    commandListHeading = "%n@|bold Comandos:|@%n",
    footerHeading = "%n@|bold Exemplos:|@%n",
    footer = {
        "  @|yellow spring-scaffold model User -p com.example.model -f \"name:String,email:String\"|@",
        "  @|yellow spring-scaffold controller UserController -m User|@",
        "  @|yellow spring-scaffold security --jwt-secret mySecret|@",
        "  @|yellow spring-scaffold project my-app --package com.example|@",
        "",
        "For more informations about a specific command:",
        "  @|yellow spring-scaffold <comando> --help|@"
    }
)
public class SpringScaffoldCLI implements Runnable {

    @Option(
        names = {"-v", "--verbose"}, 
        description = "Ativa modo verboso para debug"
    )
    private boolean verbose = false;

    @Option(
        names = {"-q", "--quiet"}, 
        description = "Suppress non-essential outputs"
    )
    private boolean quiet = false;

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new SpringScaffoldCLI())
            .setColorScheme(createColorScheme());

        commandLine.setExecutionExceptionHandler((ex, cmd, parseResult) -> {
            cmd.getErr().println(cmd.getColorScheme().errorText("❌ Error: " + ex.getMessage()));
            if (parseResult.hasMatchedOption("--verbose")) {
                ex.printStackTrace(cmd.getErr());
            }
            return 1;
        });

        int exitCode = commandLine.execute(args);
        
        System.exit(exitCode);
    }

    private static CommandLine.Help.ColorScheme createColorScheme() {
        return new CommandLine.Help.ColorScheme.Builder()
            .commands(CommandLine.Help.Ansi.Style.bold, CommandLine.Help.Ansi.Style.fg_blue)
            .options(CommandLine.Help.Ansi.Style.fg_yellow)
            .parameters(CommandLine.Help.Ansi.Style.fg_yellow)
            .optionParams(CommandLine.Help.Ansi.Style.italic)
            .errors(CommandLine.Help.Ansi.Style.fg_red, CommandLine.Help.Ansi.Style.bold)
            .stackTraces(CommandLine.Help.Ansi.Style.italic)
            .build();
    }
}
