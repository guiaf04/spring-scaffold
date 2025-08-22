package com.scaffold.generators;

import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerador de classes Service
 */
@Slf4j
public class ServiceGenerator {

    private final TemplateEngine templateEngine;

    public ServiceGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public boolean generate(
            String serviceName,
            String packageName,
            String modelName,
            String modelPackage,
            String repositoryPackage,
            boolean generateInterface,
            boolean includeCrud,
            boolean includeTransactional,
            boolean includeValidation,
            String outputDirectory) {
        
        try {
            log.info("Gerando service {} no pacote {}", serviceName, packageName);
            
            // Preparar contexto para os templates
            Map<String, Object> context = new HashMap<>();
            context.put("packageName", packageName);
            context.put("serviceName", serviceName);
            context.put("modelName", modelName);
            context.put("modelPackage", modelPackage);
            context.put("repositoryPackage", repositoryPackage);
            context.put("generateInterface", generateInterface);
            context.put("includeCrud", includeCrud);
            context.put("includeTransactional", includeTransactional);
            context.put("includeValidation", includeValidation);
            
            // Calcular nomes derivados
            String repositoryName = modelName + "Repository";
            String repositoryInstanceName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
            String modelInstanceName = Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1);
            String interfaceName = serviceName;
            String implementationName = serviceName + "Impl";
            
            context.put("repositoryName", repositoryName);
            context.put("repositoryInstanceName", repositoryInstanceName);
            context.put("modelInstanceName", modelInstanceName);
            context.put("interfaceName", interfaceName);
            context.put("implementationName", implementationName);
            
            log.info("Configurações:");
            log.info("- Service: {}", serviceName);
            log.info("- Pacote: {}", packageName);
            log.info("- Model: {}", modelName);
            log.info("- Repository: {}", repositoryName);
            log.info("- Gerar Interface: {}", generateInterface);
            
            String packagePath = packageName.replace(".", "/");
            String fullPath = outputDirectory + "/src/main/java/" + packagePath;
            FileUtils.createDirectories(fullPath);
            
            boolean success = true;
            
            // Gerar interface se solicitado
            if (generateInterface) {
                String interfaceCode = templateEngine.processTemplate("service-interface.java.mustache", context);
                String interfaceFileName = fullPath + "/" + interfaceName + ".java";
                FileUtils.createFile(interfaceFileName, interfaceCode);
                log.info("Interface {} gerada em {}", interfaceName, interfaceFileName);
                
                // Gerar implementação
                String implCode = templateEngine.processTemplate("service-impl.java.mustache", context);
                String implFileName = fullPath + "/" + implementationName + ".java";
                FileUtils.createFile(implFileName, implCode);
                log.info("Implementação {} gerada em {}", implementationName, implFileName);
            } else {
                // Gerar apenas a classe service
                String serviceCode = templateEngine.processTemplate("service.java.mustache", context);
                String serviceFileName = fullPath + "/" + serviceName + ".java";
                FileUtils.createFile(serviceFileName, serviceCode);
                log.info("Service {} gerado em {}", serviceName, serviceFileName);
            }
            
            log.info("Service gerado com sucesso!");
            return success;
            
        } catch (Exception e) {
            log.error("Erro ao gerar service", e);
            return false;
        }
    }
}
