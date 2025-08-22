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
        "Gera uma classe de serviço com lógica de negócio.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold service UserService|@",
        "  @|yellow spring-scaffold service ProductService -m Product|@",
        "  @|yellow spring-scaffold service CustomerService --no-interface|@"
    },
    mixinStandardHelpOptions = true
)
public class ServiceCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome da classe service (ex: UserService, ProductService)"
    )
    private String serviceName;

    @Option(
        names = {"-p", "--package"},
        description = "Pacote do service (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.service"
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
        names = {"--repository-package"},
        description = "Pacote da classe repository (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.repository"
    )
    private String repositoryPackage;

    @Option(
        names = {"--interface"},
        description = "Gerar interface do service (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean generateInterface;

    @Option(
        names = {"--crud"},
        description = "Incluir métodos CRUD básicos (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCrud;

    @Option(
        names = {"--transactional"},
        description = "Incluir annotations @Transactional (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeTransactional;

    @Option(
        names = {"--validation"},
        description = "Incluir validações de negócio (padrão: ${DEFAULT-VALUE})",
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
            log.info("🚀 Gerando service: {}", serviceName);
            if (serviceName == null || serviceName.trim().isEmpty()) {
                System.err.println("❌ Nome do service é obrigatório");
                return 1;
            }
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(serviceName);
                log.info("Model inferido: {}", modelName);
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
                System.out.println("✅ Service " + serviceName + " gerado com sucesso!");
                System.out.println("📁 Localização: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + serviceName + ".java");
                
                if (generateInterface) {
                    System.out.println("📁 Interface: " + outputDirectory + "/" + 
                        packageName.replace(".", "/") + "/" + serviceName.replace("Impl", "") + ".java");
                }
                
                if (modelName != null) {
                    System.out.println("🔗 Model associado: " + modelName);
                }
                
                return 0;
            } else {
                System.err.println("❌ Falha ao gerar service");
                return 1;
            }

        } catch (Exception e) {
            log.error("Erro ao gerar service", e);
            System.err.println("❌ Erro inesperado: " + e.getMessage());
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
