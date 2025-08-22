package com.scaffold.generators;

import com.scaffold.commands.RepositoryCommand.RepositoryType;
import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerador de interfaces Repository
 */
@Slf4j
public class RepositoryGenerator {

    private final TemplateEngine templateEngine;

    public RepositoryGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public boolean generate(
            String repositoryName,
            String packageName,
            String modelName,
            String modelPackage,
            RepositoryType repositoryType,
            String idType,
            boolean includeCustomQueries,
            boolean includePagination,
            String outputDirectory) {
        
        try {
            log.info("Gerando repository {} no pacote {}", repositoryName, packageName);
            
            // Preparar contexto para o template
            Map<String, Object> context = new HashMap<>();
            context.put("packageName", packageName);
            context.put("repositoryName", repositoryName);
            context.put("modelName", modelName);
            context.put("modelPackage", modelPackage);
            context.put("repositoryType", repositoryType.name());
            context.put("idType", idType);
            context.put("includeCustomQueries", includeCustomQueries);
            context.put("includePagination", includePagination);
            
            // Calcular nomes e tipos derivados
            String modelInstanceName = Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1);
            context.put("modelInstanceName", modelInstanceName);
            
            // Determinar o tipo base do repository
            String baseType;
            switch (repositoryType) {
                case JPA:
                    baseType = "JpaRepository";
                    break;
                case MONGODB:
                    baseType = "MongoRepository";
                    break;
                case REACTIVE_MONGO:
                    baseType = "ReactiveMongoRepository";
                    break;
                case REACTIVE_R2DBC:
                    baseType = "ReactiveCrudRepository";
                    break;
                default:
                    baseType = "JpaRepository";
            }
            context.put("baseType", baseType);
            
            log.info("Configurações:");
            log.info("- Repository: {}", repositoryName);
            log.info("- Pacote: {}", packageName);
            log.info("- Model: {}", modelName);
            log.info("- Tipo Base: {}", baseType);
            log.info("- Tipo ID: {}", idType);
            
            // Gerar código usando template
            String code = templateEngine.processTemplate("repository.java.mustache", context);
            
            // Criar estrutura de diretórios
            String packagePath = packageName.replace(".", "/");
            String fullPath = outputDirectory + "/src/main/java/" + packagePath;
            FileUtils.createDirectories(fullPath);
            
            // Escrever arquivo
            String fileName = fullPath + "/" + repositoryName + ".java";
            FileUtils.createFile(fileName, code);
            
            log.info("Repository {} gerado com sucesso em {}", repositoryName, fileName);
            return true;
            
        } catch (Exception e) {
            log.error("Erro ao gerar repository", e);
            return false;
        }
    }
}
