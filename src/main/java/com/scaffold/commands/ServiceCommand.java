package com.scaffold.commands;

import com.scaffold.generators.ServiceGenerator;
import com.scaffold.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "service",
    description = {
        "Generate a service class with business logic.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold service UserService|@",
        "  @|yellow spring-scaffold service ProductService -m Product|@",
        "  @|yellow spring-scaffold service CustomerService --no-interface|@"
    },
    mixinStandardHelpOptions = true
)
public class ServiceCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Service class name (ex: UserService, ProductService)"
    )
    private String serviceName;

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Service package (relative or absolute). Use 'service' for automatic package detection"
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
        names = {"--repository-package", "--repo-pkg"},
        description = "Repository class package (relative or absolute). Leave empty for auto-detection"
    )
    private String repositoryPackage;

    @Option(
        names = {"--interface"},
        description = "Generate service interface (default: ${DEFAULT-VALUE})"
    )
    private boolean generateInterface = true;

    @Option(
        names = {"--crud"},
        description = "Include basic CRUD methods"
    )
    private boolean includeCrud = false;

    @Option(
        names = {"--transactional"},
        description = "Include @Transactional annotations"
    )
    private boolean includeTransactional = false;

    @Option(
        names = {"--validation"},
        description = "Include business validations"
    )
    private boolean includeValidation = false;

    @Option(
        names = {"-o", "--output"},
        description = "Output directory (default: current directory)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Generating service: {}", serviceName);
            if (serviceName == null || serviceName.trim().isEmpty()) {
                System.err.println("❌ Service name is required");
                return 1;
            }
            
            // Resolve package names using auto-detection
            String resolvedServicePackage = resolvePackageName(packageName, "service");
            String resolvedModelPackage = resolvePackageName(modelPackage, "model");
            String resolvedRepositoryPackage = resolvePackageName(repositoryPackage, "repository");
            
            log.info("Using service package: {}", resolvedServicePackage);
            log.info("Using model package: {}", resolvedModelPackage);
            log.info("Using repository package: {}", resolvedRepositoryPackage);
            
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(serviceName);
                log.info("Inferred model: {}", modelName);
            }
            
            ServiceGenerator generator = new ServiceGenerator();
            boolean success = generator.generate(
                serviceName,
                resolvedServicePackage,
                modelName,
                resolvedModelPackage,
                resolvedRepositoryPackage,
                generateInterface,
                includeCrud,
                includeTransactional,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Service " + serviceName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    resolvedServicePackage.replace(".", "/") + "/" + serviceName + ".java");
                
                if (generateInterface) {
                    System.out.println("📁 Interface: " + outputDirectory + "/" + 
                        resolvedServicePackage.replace(".", "/") + "/" + serviceName.replace("Impl", "") + ".java");
                }
                
                if (modelName != null) {
                    System.out.println("🔗 Associated model: " + modelName);
                }
                
                return 0;
            } else {
                System.err.println("❌ Failed to generate service");
                return 1;
            }

        } catch (Exception e) {
            log.error("Error generating service", e);
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return 1;
        }
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

    private String inferModelName(String serviceName) {
        if (serviceName.endsWith("Service")) {
            return serviceName.substring(0, serviceName.length() - 7);
        }
        return serviceName + "Model";
    }
}
