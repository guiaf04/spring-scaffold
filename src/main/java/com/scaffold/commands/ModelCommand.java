package com.scaffold.commands;

import com.scaffold.generators.ModelGenerator;
import com.scaffold.models.FieldInfo;
import com.scaffold.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "model",
    description = {
        "Generate a model/entity class with JPA and Lombok annotations.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold model User|@",
        "  @|yellow spring-scaffold model Product name:String price:BigDecimal|@",
        "  @|yellow spring-scaffold model Customer --pkg com.example.entity|@"
    },
    mixinStandardHelpOptions = true
)
public class ModelCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Model class name (ex: User, Product, Customer)"
    )
    private String className;

    @Parameters(
        index = "1..*",
        arity = "0..*",
        description = "Fields in 'name:type' format (ex: name:String age:Integer)"
    )
    private List<String> fieldParams = new ArrayList<>();

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Class package (relative or absolute). Use 'model' for automatic package detection or full package like 'com.example.model'"
    )
    private String packageName;

    @Option(
        names = {"-f", "--fields"},
        description = {
            "Field list in 'name:type,name:type' format (alternative to positional parameters)",
            "Supported types: String, Integer, Long, Boolean, BigDecimal, LocalDate, LocalDateTime",
            "Example: 'name:String,age:Integer,active:Boolean'"
        },
        split = ","
    )
    private String[] fields = {};

    @Option(
        names = {"-t", "--table", "--tbl"},
        description = "Database table name (default: class name in snake_case)"
    )
    private String tableName;

    @Option(
        names = {"--jpa", "--entity"},
        description = "Include JPA annotations (@Entity, @Table, etc.) (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeJpa;

    @Option(
        names = {"--lombok", "--data"},
        description = "Use Lombok annotations (@Data, @Entity, etc.) (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean useLombok;

    @Option(
        names = {"--validation", "--valid"},
        description = "Include Bean Validation annotations (default: ${DEFAULT-VALUE})",
        defaultValue = "false"
    )
    private boolean includeValidation;

    @Option(
        names = {"-o", "--output", "--out"},
        description = "Output directory (default: current directory)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Generating model: {}", className);
            if (className == null || className.trim().isEmpty()) {
                System.err.println("❌ Class name is required");
                return 1;
            }

            // Resolve package name using auto-detection
            String resolvedPackage = resolvePackageName(packageName, "model");
            log.info("Using package: {}", resolvedPackage);

            List<FieldInfo> fieldInfoList = parseFields();
            ModelGenerator generator = new ModelGenerator();
            boolean success = generator.generate(
                className,
                resolvedPackage,
                fieldInfoList,
                tableName,
                includeJpa,
                useLombok,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Model " + className + " generated successfully!");
                System.out.println("📁 Location: " + outputDirectory + "/" + 
                    resolvedPackage.replace(".", "/") + "/" + className + ".java");
                return 0;
            } else {
                System.err.println("❌ Failed to generate model");
                return 1;
            }

        } catch (Exception e) {
            log.error("Error generating model", e);
            System.err.println("❌ Unexpected error: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Resolves the package name using auto-detection logic.
     * 
     * @param userPackage The package specified by the user (can be null, relative, or absolute)
     * @param defaultSubPackage The default sub-package to use (e.g., "model", "controller")
     * @return The resolved full package name
     */
    private String resolvePackageName(String userPackage, String defaultSubPackage) {
        String basePackage = ProjectUtils.detectBasePackage();
        
        if (userPackage == null || userPackage.trim().isEmpty()) {
            // No package specified, use default: basePackage.subPackage
            return basePackage + "." + defaultSubPackage;
        }
        
        userPackage = userPackage.trim();
        
        // If it contains dots, it's likely a full package name
        if (userPackage.contains(".")) {
            return userPackage;
        }
        
        // Otherwise, treat it as a relative package and append to base
        return basePackage + "." + userPackage;
    }

    private List<FieldInfo> parseFields() {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        
        List<String> allFields = new ArrayList<>();
        
        if (fieldParams != null && !fieldParams.isEmpty()) {
            allFields.addAll(fieldParams);
        }
        
        if (fields != null && fields.length > 0) {
            for (String field : fields) {
                allFields.add(field);
            }
        }
        
        for (String field : allFields) {
            if (field != null && !field.trim().isEmpty()) {
                String[] parts = field.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String type = parts[1].trim();
                    
                    if (!name.isEmpty() && !type.isEmpty()) {
                        fieldInfoList.add(new FieldInfo(name, type));
                    }
                } else {
                    System.err.println("⚠️  Invalid field ignored: " + field);
                    System.err.println("   Expected format: name:type");
                }
            }
        }
        
        if (fieldInfoList.isEmpty()) {
            log.info("No fields specified, adding only ID");
        }
        
        return fieldInfoList;
    }
}
