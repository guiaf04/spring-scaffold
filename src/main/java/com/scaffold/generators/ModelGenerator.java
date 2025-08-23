package com.scaffold.generators;

import com.scaffold.models.FieldInfo;
import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ModelGenerator {

    private final TemplateEngine templateEngine;

    public ModelGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public boolean generate(
            String className,
            String packageName,
            List<FieldInfo> fields,
            String tableName,
            boolean includeJpa,
            boolean useLombok,
            boolean includeValidation,
            String outputDirectory) {
        
        try {
            log.info("Generating model {} in package {}", className, packageName);
            
            // Infer table name if not specified
            if (tableName == null || tableName.trim().isEmpty()) {
                tableName = inferTableName(className);
            }
            
            log.info("Configuration:");
            log.info("- Classe: {}", className);
            log.info("- Package: {}", packageName);
            log.info("- Tabela: {}", tableName);
            log.info("- JPA: {}", includeJpa);
            log.info("- Lombok: {}", useLombok);
            log.info("- Validação: {}", includeValidation);
            log.info("- Campos: {}", fields.size());
            
            for (FieldInfo field : fields) {
                log.info("  - {}: {}", field.getName(), field.getType());
            }
            
            // Preparar contexto do template
            Map<String, Object> context = prepareTemplateContext(
                className, packageName, fields, tableName, includeJpa, useLombok, includeValidation
            );
            
            // Generate code using template
            String generatedCode = templateEngine.processTemplate("model.java.mustache", context);
            
            // Build file path
            String filePath = FileUtils.buildFilePath(outputDirectory, packageName, className);
            
            // Create file
            boolean success = FileUtils.createFile(filePath, generatedCode);
            
            if (success) {
                log.info("Model {} generated successfully at: {}", className, filePath);
            } else {
                log.error("Failed to generate model {}", className);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("Error generating model", e);
            return false;
        }
    }

    private Map<String, Object> prepareTemplateContext(
            String className,
            String packageName,
            List<FieldInfo> fields,
            String tableName,
            boolean includeJpa,
            boolean useLombok,
            boolean includeValidation) {
        
        Map<String, Object> context = new HashMap<>();

        context.put("className", className);
        context.put("packageName", packageName);
        context.put("tableName", tableName);
        context.put("includeJpa", includeJpa);
        context.put("useLombok", useLombok);
        context.put("includeValidation", includeValidation);
        
        List<Map<String, Object>> processedFields = fields.stream()
            .map(this::processField)
            .collect(Collectors.toList());
        context.put("fields", processedFields);

        Set<String> imports = collectImports(fields, includeJpa, includeValidation, useLombok);
        context.put("imports", new ArrayList<>(imports));
        
        return context;
    }

    private Map<String, Object> processField(FieldInfo field) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", field.getName());
        fieldMap.put("type", field.getType());
        fieldMap.put("capitalizedName", capitalize(field.getName()));
        
        // JPA annotations
        String jpaAnnotation = field.getJpaColumnAnnotation();
        if (jpaAnnotation != null && !jpaAnnotation.trim().isEmpty()) {
            fieldMap.put("jpaAnnotation", jpaAnnotation);
        }
        
        // Validation annotations
        String validationAnnotations = field.getValidationAnnotations();
        if (validationAnnotations != null && !validationAnnotations.trim().isEmpty()) {
            fieldMap.put("validationAnnotations", Arrays.asList(validationAnnotations.split("\n")));
        }
        
        return fieldMap;
    }

    private Set<String> collectImports(List<FieldInfo> fields, boolean includeJpa, boolean includeValidation, boolean useLombok) {
        Set<String> imports = new HashSet<>();
        
        // Imports for field types
        for (FieldInfo field : fields) {
            String requiredImport = field.getRequiredImport();
            if (requiredImport != null) {
                imports.add(requiredImport);
            }
        }
        
        // Imports JPA
        if (includeJpa) {
            imports.add("jakarta.persistence.*");
        }
        
        // Imports de validação
        if (includeValidation) {
            imports.add("jakarta.validation.constraints.*");
        }
        
        // Imports Lombok
        if (useLombok) {
            imports.add("lombok.Data");
            imports.add("lombok.NoArgsConstructor");
            imports.add("lombok.AllArgsConstructor");
        } else {
            imports.add("java.util.Objects");
        }
        
        return imports;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String inferTableName(String className) {
        String snakeCase = className.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        
        if (snakeCase.endsWith("y")) {
            return snakeCase.substring(0, snakeCase.length() - 1) + "ies";
        } else if (snakeCase.endsWith("s") || snakeCase.endsWith("sh") || 
                   snakeCase.endsWith("ch") || snakeCase.endsWith("x") || 
                   snakeCase.endsWith("z")) {
            return snakeCase + "es";
        } else {
            return snakeCase + "s";
        }
    }
}
