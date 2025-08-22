package com.scaffold.generators;

import com.scaffold.commands.RepositoryCommand.RepositoryType;
import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
            log.info("Generating repository {} in package {}", repositoryName, packageName);
            Map<String, Object> context = new HashMap<>();
            context.put("packageName", packageName);
            context.put("repositoryName", repositoryName);
            context.put("modelName", modelName);
            context.put("modelPackage", modelPackage);
            context.put("repositoryType", repositoryType.name());
            context.put("idType", idType);
            context.put("includeCustomQueries", includeCustomQueries);
            context.put("includePagination", includePagination);
            String modelInstanceName = Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1);
            context.put("modelInstanceName", modelInstanceName);
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
            log.info("- Package: {}", packageName);
            log.info("- Model: {}", modelName);
            log.info("- Base Type: {}", baseType);
            log.info("- ID Type: {}", idType);
            String code = templateEngine.processTemplate("repository.java.mustache", context);
            String packagePath = packageName.replace(".", "/");
            String fullPath = outputDirectory + "/src/main/java/" + packagePath;
            FileUtils.createDirectories(fullPath);
            String fileName = fullPath + "/" + repositoryName + ".java";
            FileUtils.createFile(fileName, code);
            
            log.info("Repository {} generated successfully at {}", repositoryName, fileName);
            return true;
            
        } catch (Exception e) {
            log.error("Error generating repository", e);
            return false;
        }
    }
}
