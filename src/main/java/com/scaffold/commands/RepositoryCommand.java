package com.scaffold.commands;

import com.scaffold.generators.RepositoryGenerator;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "repository",
    description = {
        "Generate a repository interface for data access.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold repository UserRepository|@",
        "  @|yellow spring-scaffold repository ProductRepository -m Product|@",
        "  @|yellow spring-scaffold repository CustomerRepository --type mongodb|@"
    },
    mixinStandardHelpOptions = true
)
public class RepositoryCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Repository interface name (ex: UserRepository, ProductRepository)"
    )
    private String repositoryName;

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Repository package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.repository"
    )
    private String packageName;

    @Option(
        names = {"-m", "--model", "--entity"},
        description = "Associated model class name (ex: User, Product)"
    )
    private String modelName;

    @Option(
        names = {"--model-package", "--model-pkg"},
        description = "Model class package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.model"
    )
    private String modelPackage;

    @Option(
        names = {"--type", "-t"},
        description = "Repository type: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})",
        defaultValue = "JPA"
    )
    private RepositoryType repositoryType;

    @Option(
        names = {"--id-type"},
        description = "Entity ID type (default: ${DEFAULT-VALUE})",
        defaultValue = "Long"
    )
    private String idType;

    @Option(
        names = {"--custom-queries"},
        description = "Include custom query examples (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCustomQueries;

    @Option(
        names = {"--pagination"},
        description = "Include pagination support (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includePagination;

    @Option(
        names = {"-o", "--output"},
        description = "Output directory (default: current directory)"
    )
    private String outputDirectory = ".";

    public enum RepositoryType {
        JPA, MONGODB, REACTIVE_MONGO, REACTIVE_R2DBC
    }

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Generating repository: {}", repositoryName);
            if (repositoryName == null || repositoryName.trim().isEmpty()) {
                System.err.println("❌ Repository name is required");
                return 1;
            }
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(repositoryName);
                log.info("Inferred model: {}", modelName);
            }
            RepositoryGenerator generator = new RepositoryGenerator();
            boolean success = generator.generate(
                repositoryName,
                packageName,
                modelName,
                modelPackage,
                repositoryType,
                idType,
                includeCustomQueries,
                includePagination,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Repository " + repositoryName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + repositoryName + ".java");
                System.out.println("🔧 Type: " + repositoryType);
                
                if (modelName != null) {
                    System.out.println("🔗 Associated model: " + modelName);
                    System.out.println("🔑 ID type: " + idType);
                }
                
                return 0;
            } else {
                System.err.println("❌ Failed to generate repository");
                return 1;
            }

        } catch (Exception e) {
            log.error("Error generating repository", e);
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return 1;
        }
    }

    private String inferModelName(String repositoryName) {
        if (repositoryName.endsWith("Repository")) {
            return repositoryName.substring(0, repositoryName.length() - 10);
        }
        return repositoryName + "Model";
    }
}
