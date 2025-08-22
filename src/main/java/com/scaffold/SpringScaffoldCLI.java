package com.scaffold;

import com.scaffold.commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Classe principal do Spring Scaffold CLI
 * 
 * Aplicativo de linha de comando para auxiliar na construção de aplicações 
 * Java e Spring Boot, oferecendo geração automática de scaffold para models, 
 * controllers, services, repositories e templates de projeto.
 */
@Command(
    name = "spring-scaffold",
    mixinStandardHelpOptions = true,
    version = "Spring Scaffold CLI 1.0.0",
    description = {
        "@|bold Spring Scaffold CLI|@",
        "",
        "Gerador de código para aplicações Spring Boot.",
        "Cria automaticamente models, controllers, services, repositories e templates de projeto.",
        "",
        "@|underline Comandos disponíveis:|@",
        "  model      - Gera classes model/entity",
        "  controller - Gera controllers REST",
        "  service    - Gera classes de serviço",
        "  repository - Gera repositories JPA",
        "  project    - Cria novo projeto Spring Boot"
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
        // Quando executado sem subcomandos, mostra ajuda
        CommandLine.usage(this, System.out);
    }

    /**
     * Método principal de entrada da aplicação
     */
    public static void main(String[] args) {
        // Cria instância do CommandLine com tratamento de cores
        CommandLine commandLine = new CommandLine(new SpringScaffoldCLI())
            .setColorScheme(createColorScheme());

        // Configura tratamento de erros personalizado
        commandLine.setExecutionExceptionHandler((ex, cmd, parseResult) -> {
            cmd.getErr().println(cmd.getColorScheme().errorText("❌ Erro: " + ex.getMessage()));
            if (parseResult.hasMatchedOption("--verbose")) {
                ex.printStackTrace(cmd.getErr());
            }
            return 1;
        });

        // Executa o comando e obtém código de saída
        int exitCode = commandLine.execute(args);
        
        // Sai com o código apropriado
        System.exit(exitCode);
    }

    /**
     * Cria esquema de cores personalizado para o CLI
     */
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
