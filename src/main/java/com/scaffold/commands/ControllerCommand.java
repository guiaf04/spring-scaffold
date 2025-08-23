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
        description = "Controller package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.controller"
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
        names = {"--service-package", "--service-pkg"},
        description = "Service class package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.service"
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
            
            // Auto-detect base package if using defaults
            String detectedBasePackage = ProjectUtils.detectBasePackage();
            
            // Use detected packages if still using defaults
            if ("com.example.controller".equals(packageName)) {
                packageName = ProjectUtils.getControllerPackage(detectedBasePackage);
                log.debug("Auto-detected controller package: {}", packageName);
            }
            
            if ("com.example.model".equals(modelPackage)) {
                modelPackage = ProjectUtils.getModelPackage(detectedBasePackage);
                log.debug("Auto-detected model package: {}", modelPackage);
            }
            
            if ("com.example.service".equals(servicePackage)) {
                servicePackage = ProjectUtils.getServicePackage(detectedBasePackage);
                log.debug("Auto-detected service package: {}", servicePackage);
            }
            
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(controllerName);
                log.info("Inferred model: {}", modelName);
            }
            ControllerGenerator generator = new ControllerGenerator();
            boolean success = generator.generate(
                controllerName,
                packageName,
                modelName,
                modelPackage,
                servicePackage,
                basePath,
                includeCrud,
                includeSwagger,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Controller " + controllerName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + controllerName + ".java");
                
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

    private String inferModelName(String controllerName) {
        if (controllerName.endsWith("Controller")) {
            return controllerName.substring(0, controllerName.length() - 10);
        }
        return controllerName + "Model";
    }
}
