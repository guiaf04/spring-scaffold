package com.scaffold.commands;

import com.scaffold.generators.RepositoryGenerator;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

/**
 * Comando para gerar interfaces Repository
 */
@Slf4j
@Command(
    name = "repository",
    description = {
        "Gera uma interface repository para acesso a dados.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold repository UserRepository|@",
        "  @|yellow spring-scaffold repository ProductRepository -m Product|@",
        "  @|yellow spring-scaffold repository CustomerRepository --type mongodb|@"
    },
    mixinStandardHelpOptions = true
)
public class RepositoryCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome da interface repository (ex: UserRepository, ProductRepository)"
    )
    private String repositoryName;

    @Option(
        names = {"-p", "--package"},
        description = "Pacote do repository (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.repository"
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
        names = {"--type"},
        description = "Tipo de repository: ${COMPLETION-CANDIDATES} (padrão: ${DEFAULT-VALUE})",
        defaultValue = "JPA"
    )
    private RepositoryType repositoryType;

    @Option(
        names = {"--id-type"},
        description = "Tipo do ID da entidade (padrão: ${DEFAULT-VALUE})",
        defaultValue = "Long"
    )
    private String idType;

    @Option(
        names = {"--custom-queries"},
        description = "Incluir exemplos de queries customizadas (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeCustomQueries;

    @Option(
        names = {"--pagination"},
        description = "Incluir suporte a paginação (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includePagination;

    @Option(
        names = {"-o", "--output"},
        description = "Diretório de saída (padrão: diretório atual)"
    )
    private String outputDirectory = ".";

    /**
     * Enum para tipos de repository suportados
     */
    public enum RepositoryType {
        JPA, MONGODB, REACTIVE_MONGO, REACTIVE_R2DBC
    }

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Gerando repository: {}", repositoryName);
            
            // Validar entrada
            if (repositoryName == null || repositoryName.trim().isEmpty()) {
                System.err.println("❌ Nome do repository é obrigatório");
                return 1;
            }

            // Se não especificado, inferir model do nome do repository
            if (modelName == null || modelName.trim().isEmpty()) {
                modelName = inferModelName(repositoryName);
                log.info("Model inferido: {}", modelName);
            }

            // Configurar gerador
            RepositoryGenerator generator = new RepositoryGenerator();
            
            // Gerar repository
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
                System.out.println("✅ Repository " + repositoryName + " gerado com sucesso!");
                System.out.println("📁 Localização: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + repositoryName + ".java");
                System.out.println("🔧 Tipo: " + repositoryType);
                
                if (modelName != null) {
                    System.out.println("🔗 Model associado: " + modelName);
                    System.out.println("🔑 Tipo do ID: " + idType);
                }
                
                return 0;
            } else {
                System.err.println("❌ Falha ao gerar repository");
                return 1;
            }

        } catch (Exception e) {
            log.error("Erro ao gerar repository", e);
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Infere o nome do model a partir do nome do repository
     * Ex: UserRepository -> User, ProductRepository -> Product
     */
    private String inferModelName(String repositoryName) {
        if (repositoryName.endsWith("Repository")) {
            return repositoryName.substring(0, repositoryName.length() - 10);
        }
        return repositoryName + "Model";
    }
}
