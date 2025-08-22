package com.scaffold.generators;

import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ControllerGenerator {

    private final TemplateEngine templateEngine;

    public ControllerGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public boolean generate(
            String controllerName,
            String packageName,
            String modelName,
            String modelPackage,
            String servicePackage,
            String basePath,
            boolean includeCrud,
            boolean includeSwagger,
            boolean includeValidation,
            String outputDirectory) {
        
        try {
            log.info("Gerando controller {} no pacote {}", controllerName, packageName);
            
            Map<String, Object> context = new HashMap<>();
            context.put("packageName", packageName);
            context.put("controllerName", controllerName);
            context.put("modelName", modelName);
            context.put("modelPackage", modelPackage);
            context.put("servicePackage", servicePackage);
            context.put("basePath", basePath);
            context.put("includeCrud", includeCrud);
            context.put("includeSwagger", includeSwagger);
            context.put("includeValidation", includeValidation);
            
            String serviceName = modelName + "Service";
            String serviceInstanceName = Character.toLowerCase(serviceName.charAt(0)) + serviceName.substring(1);
            String modelInstanceName = Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1);
            String resourcePath = modelInstanceName + "s";
            
            context.put("serviceName", serviceName);
            context.put("serviceInstanceName", serviceInstanceName);
            context.put("modelInstanceName", modelInstanceName);
            context.put("resourcePath", resourcePath);
            
            log.info("Configurações:");
            log.info("- Controller: {}", controllerName);
            log.info("- Pacote: {}", packageName);
            log.info("- Model: {}", modelName);
            log.info("- Service: {}", serviceName);
            log.info("- Resource Path: {}", resourcePath);
            log.info("- Base Path: {}", basePath);
            
            String code = templateEngine.processTemplate("controller.java.mustache", context);
            
            String packagePath = packageName.replace(".", "/");
            String fullPath = outputDirectory + "/src/main/java/" + packagePath;
            FileUtils.createDirectories(fullPath);
            
            String fileName = fullPath + "/" + controllerName + ".java";
            FileUtils.createFile(fileName, code);
            
            log.info("Controller {} gerado com sucesso em {}", controllerName, fileName);
            return true;
            
        } catch (Exception e) {
            log.error("Erro ao gerar controller", e);
            return false;
        }
    }
}
