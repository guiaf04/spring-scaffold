package com.scaffold.commands;

import com.scaffold.generators.ControllerGenerator;
import com.scaffold.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "controller",
    description = {
        "Generate a REST controller with basic CRUD endpoints.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold controller UserController|@",
        "  @|yellow spring-scaffold controller ProductController -m Product|@",
        "  @|yellow spring-scaffold controller CustomerController --path /api/v2|@"
    },
    mixinStandardHelpOptions = true
)
public class ControllerCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Controller name (ex: UserController, ProductController)"
    )
    private String controllerName;

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Controller package (relative or absolute). Use 'controller' for automatic package detection"
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
        names = {"--service-package", "--service-pkg"},
        description = "Service class package (relative or absolute). Leave empty for auto-detection"
    )
    private String servicePackage;

    @Option(
        names = {"--path"},
        description = "REST API base path (default: ${DEFAULT-VALUE})",
        defaultValue = "/api/v1"
    )
    private String basePath;

    @Option(
        names = {"--crud"},
        description = "Include complete CRUD operations"
    )
    private boolean includeCrud = false;

    @Option(
        names = {"--swagger"},
        description = "Include Swagger/OpenAPI annotations"
    )
    private boolean includeSwagger = false;

    @Option(
        names = {"--validation"},
        description = "Include request validations (default: true)"
    )
    private boolean includeValidation = true;

    @Option(
        names = {"-o", "--output"},
        description = "Output directory (default: current directory)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Generating controller: {}", controllerName);
            if (controllerName == null || controllerName.trim().isEmpty()) {
                System.err.println("❌ Controller name is required");
                return 1;
            }
            
            // Resolve package names using auto-detection
            String resolvedControllerPackage = resolvePackageName(packageName, "controller");
            String resolvedModelPackage = resolvePackageName(modelPackage, "model");
            String resolvedServicePackage = resolvePackageName(servicePackage, "service");
            
            log.info("Using controller package: {}", resolvedControllerPackage);
            log.info("Using model package: {}", resolvedModelPackage);
            log.info("Using service package: {}", resolvedServicePackage);
            
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(controllerName);
                log.info("Inferred model: {}", modelName);
            }
            
            ControllerGenerator generator = new ControllerGenerator();
            boolean success = generator.generate(
                controllerName,
                resolvedControllerPackage,
                modelName,
                resolvedModelPackage,
                resolvedServicePackage,
                basePath,
                includeCrud,
                includeSwagger,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Controller " + controllerName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    resolvedControllerPackage.replace(".", "/") + "/" + controllerName + ".java");
                
                if (modelName != null) {
                    System.out.println("🔗 Associated model: " + modelName);
                    System.out.println("🌐 Base endpoints: " + basePath + "/" + 
                        modelName.toLowerCase() + "s");
                }
                
                return 0;
            } else {
                System.err.println("❌ Failed to generate controller");
                return 1;
            }

        } catch (Exception e) {
            log.error("Error generating controller", e);
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

    private String inferModelName(String controllerName) {
        if (controllerName.endsWith("Controller")) {
            return controllerName.substring(0, controllerName.length() - 10);
        }
        return controllerName + "Model";
    }
}
