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
        "🚀 Gerador de código FUNCIONAL para aplicações Spring Boot.",
        "Cria automaticamente models, controllers, services, repositories e projetos completos.",
        "",
        "@|underline Comandos disponíveis:|@",
        "  model      - Gera classes model/entity com JPA e validações",
        "  controller - Gera controllers REST com CRUD completo",
        "  service    - Gera classes de serviço com interfaces",
        "  repository - Gera repositories JPA com queries customizadas",
        "  project    - Cria projetos Spring Boot completos"
    },
    subcommands = {
        ModelCommand.class,
        ControllerCommand.class,
        ServiceCommand.class,
        RepositoryCommand.class,
        ProjectCommand.class,
        CommandLine.HelpCommand.class
    },
    commandListHeading = "%n@|bold Comandos:|@%n",
    footerHeading = "%n@|bold Exemplos:|@%n",
    footer = {
        "  @|yellow spring-scaffold model User -p com.example.model -f \"name:String,email:String\"|@",
        "  @|yellow spring-scaffold controller UserController -m User|@",
        "  @|yellow spring-scaffold project my-app --package com.example|@",
        "",
        "Para mais informações sobre um comando específico:",
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
        description = "Suprime saídas não essenciais"
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
            cmd.getErr().println(cmd.getColorScheme().errorText("❌ Erro: " + ex.getMessage()));
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
