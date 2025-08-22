package com.scaffold.commands;

import com.scaffold.generators.ServiceGenerator;
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
        description = "Service package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.service"
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
        names = {"--repository-package", "--repo-pkg"},
        description = "Repository class package (default: ${DEFAULT-VALUE})",
        defaultValue = "com.example.repository"
    )
    private String repositoryPackage;

    @Option(
        names = {"--interface"},
        description = "Generate service interface (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean generateInterface;

    @Option(
        names = {"--crud"},
        description = "Include basic CRUD methods (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCrud;

    @Option(
        names = {"--transactional"},
        description = "Include @Transactional annotations (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeTransactional;

    @Option(
        names = {"--validation"},
        description = "Include business validations (default: ${DEFAULT-VALUE})",
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
            log.info("🚀 Generating service: {}", serviceName);
            if (serviceName == null || serviceName.trim().isEmpty()) {
                System.err.println("❌ Service name is required");
                return 1;
            }
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(serviceName);
                log.info("Inferred model: {}", modelName);
            }
            ServiceGenerator generator = new ServiceGenerator();
            boolean success = generator.generate(
                serviceName,
                packageName,
                modelName,
                modelPackage,
                repositoryPackage,
                generateInterface,
                includeCrud,
                includeTransactional,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Service " + serviceName + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + serviceName + ".java");
                
                if (generateInterface) {
                    System.out.println("📁 Interface: " + outputDirectory + "/" + 
                        packageName.replace(".", "/") + "/" + serviceName.replace("Impl", "") + ".java");
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

    private String inferModelName(String serviceName) {
        if (serviceName.endsWith("Service")) {
            return serviceName.substring(0, serviceName.length() - 7);
        }
        return serviceName + "Model";
    }
}
