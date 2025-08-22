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
        "Create a new Spring Boot project with complete structure.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold project my-app|@",
        "  @|yellow spring-scaffold project ecommerce --package com.example.ecommerce|@",
        "  @|yellow spring-scaffold project blog-api --dependencies web,jpa,security|@"
    },
    mixinStandardHelpOptions = true
)
public class ProjectCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Project name (will be used as artifactId)"
    )
    private String projectName;

    @Option(
        names = {"--package", "--pkg", "-p"},
        description = "Project base package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example"
    )
    private String basePackage;

    @Option(
        names = {"--group-id", "--group", "-g"},
        description = "Maven Group ID (default: --package value)"
    )
    private String groupId;

    @Option(
        names = {"--spring-version", "--spring", "-s"},
        description = "Spring Boot version (default: ${DEFAULT-VALUE})",
        defaultValue = "3.2.0"
    )
    private String springBootVersion;

    @Option(
        names = {"--java-version", "--java", "-j"},
        description = "Java version (default: ${DEFAULT-VALUE})",
        defaultValue = "17"
    )
    private String javaVersion;

    @Option(
        names = {"--dependencies", "--deps", "-d"},
        description = {
            "Dependencies separated by comma.",
            "Options: web, jpa, security, validation, actuator, test, devtools, lombok, h2, mysql, postgresql, mongodb",
            "Example: web,jpa,security,validation"
        },
        split = ","
    )
    private String[] dependencies = {"web", "jpa", "test"};

    @Option(
        names = {"--database", "--db"},
        description = "Database type: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
        defaultValue = "H2"
    )
    private DatabaseType database;

    @Option(
        names = {"--packaging"},
        description = "Packaging type: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
        defaultValue = "JAR"
    )
    private PackagingType packaging;

    @Option(
        names = {"--docker"},
        description = "Include Docker files (Dockerfile and docker-compose) (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeDocker;

    @Option(
        names = {"--gitignore"},
        description = "Include .gitignore file (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeGitignore;

    @Option(
        names = {"--readme"},
        description = "Include README.md file (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeReadme;

    @Option(
        names = {"-o", "--output"},
        description = "Output directory (default: current directory)"
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
            log.info("🚀 Creating Spring Boot project: {}", projectName);
            if (projectName == null || projectName.trim().isEmpty()) {
                System.err.println("❌ Project name is required");
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
                System.out.println("✅ Project " + projectName + " created successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + projectName);
                System.out.println("🏗️  Project structure:");
                System.out.println("   📦 Base package: " + basePackage);
                System.out.println("   ☕ Java: " + javaVersion);
                System.out.println("   🍃 Spring Boot: " + springBootVersion);
                System.out.println("   💾 Database: " + database);
                System.out.println("   📋 Dependencies: " + String.join(", ", dependencies));
                
                System.out.println("\n🚀 To run the project:");
                System.out.println("   cd " + projectName);
                System.out.println("   mvn spring-boot:run");
                
                return 0;
            } else {
                System.err.println("❌ Failed to create project");
                return 1;
            }

        } catch (Exception e) {
            log.error("Error creating project", e);
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return 1;
        }
    }
}
