package com.scaffold.commands;

import com.scaffold.generators.ControllerGenerator;
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
        description = "Include complete CRUD operations (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCrud;

    @Option(
        names = {"--swagger"},
        description = "Include Swagger/OpenAPI annotations (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeSwagger;

    @Option(
        names = {"--validation"},
        description = "Include request validations (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeValidation;

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
