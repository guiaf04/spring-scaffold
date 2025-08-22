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
        "Gera um controller REST com endpoints CRUD básicos.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold controller UserController|@",
        "  @|yellow spring-scaffold controller ProductController -m Product|@",
        "  @|yellow spring-scaffold controller CustomerController --path /api/v2|@"
    },
    mixinStandardHelpOptions = true
)
public class ControllerCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome do controller (ex: UserController, ProductController)"
    )
    private String controllerName;

    @Option(
        names = {"-p", "--package"},
        description = "Pacote do controller (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.controller"
    )
    private String packageName;

    @Option(
        names = {"-m", "--model"},
        description = "Nome da classe model associada (ex: User, Product)"
    )
    private String modelName;

    @Option(
        names = {"--model-package"},
        description = "Pacote da classe model (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.model"
    )
    private String modelPackage;

    @Option(
        names = {"--service-package"},
        description = "Pacote da classe service (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.service"
    )
    private String servicePackage;

    @Option(
        names = {"--path"},
        description = "Path base da API REST (padrão: ${DEFAULT-VALUE})",
        defaultValue = "/api/v1"
    )
    private String basePath;

    @Option(
        names = {"--crud"},
        description = "Incluir operações CRUD completas (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCrud;

    @Option(
        names = {"--swagger"},
        description = "Incluir annotations Swagger/OpenAPI (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeSwagger;

    @Option(
        names = {"--validation"},
        description = "Incluir validações de request (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeValidation;

    @Option(
        names = {"-o", "--output"},
        description = "Diretório de saída (padrão: diretório atual)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Gerando controller: {}", controllerName);
            if (controllerName == null || controllerName.trim().isEmpty()) {
                System.err.println("❌ Nome do controller é obrigatório");
                return 1;
            }
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(controllerName);
                log.info("Model inferido: {}", modelName);
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
                System.out.println("✅ Controller " + controllerName + " gerado com sucesso!");
                System.out.println("📁 Localização: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + controllerName + ".java");
                
                if (modelName != null) {
                    System.out.println("🔗 Model associado: " + modelName);
                    System.out.println("🌐 Endpoints base: " + basePath + "/" + 
                        modelName.toLowerCase() + "s");
                }
                
                return 0;
            } else {
                System.err.println("❌ Falha ao gerar controller");
                return 1;
            }

        } catch (Exception e) {
            log.error("Erro ao gerar controller", e);
            System.err.println("❌ Erro inesperado: " + e.getMessage());
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
