package com.scaffold.commands;

import com.scaffold.generators.RepositoryGenerator;
import com.scaffold.utils.ProjectUtils;
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
        description = "Repository package (relative or absolute). Use 'repository' for automatic package detection"
    )
    private String packageName;

    @Option(
        names = {"-m", "--model", "--entity"},
        description = "Associated model class name (ex: User, Product)"
    )
    private String modelName;

    @Option(
        names = {"--model-package", "--model-pkg"},
        description = "Model class package (relative or absolute). Leave empty for auto-detection"
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
            
            // Resolve package names using auto-detection
            String resolvedRepositoryPackage = resolvePackageName(packageName, "repository");
            String resolvedModelPackage = resolvePackageName(modelPackage, "model");
            
            log.info("Using repository package: {}", resolvedRepositoryPackage);
            log.info("Using model package: {}", resolvedModelPackage);
            
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(repositoryName);
                log.info("Inferred model: {}", modelName);
            }
            
            RepositoryGenerator generator = new RepositoryGenerator();
            boolean success = generator.generate(
                repositoryName,
                resolvedRepositoryPackage,
                modelName,
                resolvedModelPackage,
                repositoryType,
                idType,
                includeCustomQueries,
                includePagination,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Repository " + repositoryName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    resolvedRepositoryPackage.replace(".", "/") + "/" + repositoryName + ".java");
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

    /**
     * Resolves the package name using auto-detection logic.
     * 
     * @param userPackage The package specified by the user (can be null, relative, or absolute)
     * @param defaultSubPackage The default sub-package to use (e.g., "model", "controller")
     * @return The resolved full package name
     */
    private String resolvePackageName(String userPackage, String defaultSubPackage) {
        String basePackage = ProjectUtils.detectBasePackage();
        
        if (userPackage == null || userPackage.trim().isEmpty()) {
            // No package specified, use default: basePackage.subPackage
            return basePackage + "." + defaultSubPackage;
        }
        
        userPackage = userPackage.trim();
        
        // If it contains dots, it's likely a full package name
        if (userPackage.contains(".")) {
            return userPackage;
        }
        
        // Otherwise, treat it as a relative package and append to base
        return basePackage + "." + userPackage;
    }
}
