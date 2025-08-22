package com.scaffold.commands;

import com.scaffold.generators.ProjectGenerator;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "project",
    description = {
        "Cria um novo projeto Spring Boot com estrutura completa.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold project my-app|@",
        "  @|yellow spring-scaffold project ecommerce --package com.example.ecommerce|@",
        "  @|yellow spring-scaffold project blog-api --dependencies web,jpa,security|@"
    },
    mixinStandardHelpOptions = true
)
public class ProjectCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome do projeto (será usado como artifactId)"
    )
    private String projectName;

    @Option(
        names = {"--package"},
        description = "Pacote base do projeto (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example"
    )
    private String basePackage;

    @Option(
        names = {"--group-id"},
        description = "Group ID do Maven (padrão: valor do --package)"
    )
    private String groupId;

    @Option(
        names = {"--spring-version"},
        description = "Versão do Spring Boot (padrão: ${DEFAULT-VALUE})",
        defaultValue = "3.2.0"
    )
    private String springBootVersion;

    @Option(
        names = {"--java-version"},
        description = "Versão do Java (padrão: ${DEFAULT-VALUE})",
        defaultValue = "17"
    )
    private String javaVersion;

    @Option(
        names = {"--dependencies"},
        description = {
            "Dependências separadas por vírgula.",
            "Opções: web, jpa, security, validation, actuator, test, devtools, lombok, h2, mysql, postgresql, mongodb",
            "Exemplo: web,jpa,security,validation"
        },
        split = ","
    )
    private String[] dependencies = {"web", "jpa", "test"};

    @Option(
        names = {"--database"},
        description = "Tipo de banco: ${COMPLETION-CANDIDATES} (padrão: ${DEFAULT-VALUE})",
        defaultValue = "H2"
    )
    private DatabaseType database;

    @Option(
        names = {"--packaging"},
        description = "Tipo de empacotamento: ${COMPLETION-CANDIDATES} (padrão: ${DEFAULT-VALUE})",
        defaultValue = "JAR"
    )
    private PackagingType packaging;

    @Option(
        names = {"--docker"},
        description = "Incluir arquivos Docker (Dockerfile e docker-compose) (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeDocker;

    @Option(
        names = {"--gitignore"},
        description = "Incluir arquivo .gitignore (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeGitignore;

    @Option(
        names = {"--readme"},
        description = "Incluir arquivo README.md (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeReadme;

    @Option(
        names = {"-o", "--output"},
        description = "Diretório de saída (padrão: diretório atual)"
    )
    private String outputDirectory = ".";

    public enum DatabaseType {
        H2, MYSQL, POSTGRESQL, MONGODB, SQLSERVER
    }

    public enum PackagingType {
        JAR, WAR
    }

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Criando projeto Spring Boot: {}", projectName);
            if (projectName == null || projectName.trim().isEmpty()) {
                System.err.println("❌ Nome do projeto é obrigatório");
                return 1;
            }
            if (groupId == null || groupId.trim().isEmpty()) {
                groupId = basePackage;
            }
            ProjectGenerator generator = new ProjectGenerator();
            boolean success = generator.generate(
                projectName,
                basePackage,
                groupId,
                springBootVersion,
                javaVersion,
                dependencies,
                database,
                packaging,
                includeDocker,
                includeGitignore,
                includeReadme,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Projeto " + projectName + " criado com sucesso!");
                System.out.println("📁 Localização: " + outputDirectory + "/" + projectName);
                System.out.println("🏗️  Estrutura do projeto:");
                System.out.println("   📦 Pacote base: " + basePackage);
                System.out.println("   ☕ Java: " + javaVersion);
                System.out.println("   🍃 Spring Boot: " + springBootVersion);
                System.out.println("   💾 Banco: " + database);
                System.out.println("   📋 Dependências: " + String.join(", ", dependencies));
                
                System.out.println("\n🚀 Para executar o projeto:");
                System.out.println("   cd " + projectName);
                System.out.println("   mvn spring-boot:run");
                
                return 0;
            } else {
                System.err.println("❌ Falha ao criar projeto");
                return 1;
            }

        } catch (Exception e) {
            log.error("Erro ao criar projeto", e);
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            return 1;
        }
    }
}
