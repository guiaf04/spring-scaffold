package com.scaffold.commands;

import com.scaffold.generators.SecurityGenerator;
import com.scaffold.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Slf4j
@Command(
    name = "security",
    description = {
        "Generate Spring Security configuration with JWT authentication.",
        "",
        "@|underline Examples:|@",
        "  @|yellow spring-scaffold security|@",
        "  @|yellow spring-scaffold security --pkg com.example.security|@",
        "  @|yellow spring-scaffold security --jwt-secret mySecretKey|@"
    },
    mixinStandardHelpOptions = true
)
public class SecurityCommand implements Callable<Integer> {

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Security configuration package (relative or absolute). Use 'security' for automatic package detection or full package like 'com.example.security'"
    )
    private String packageName;

    @Option(
        names = {"--jwt-secret"},
        description = "JWT secret key (default: generated random key)",
        defaultValue = "myJwtSecretKeyForDevelopmentOnly123456789"
    )
    private String jwtSecret;

    @Option(
        names = {"--jwt-expiration"},
        description = "JWT token expiration time in milliseconds (default: 24 hours)",
        defaultValue = "86400000"
    )
    private long jwtExpiration;

    @Option(
        names = {"--user-entity"},
        description = "User entity class name (default: User)",
        defaultValue = "User"
    )
    private String userEntity;

    @Option(
        names = {"--user-package"},
        description = "User entity package (relative or absolute). Use 'model' for automatic package detection"
    )
    private String userPackage;

    @Option(
        names = {"--cors"},
        description = "Enable CORS configuration (default: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean enableCors;

    @Option(
        names = {"-o", "--output", "--out"},
        description = "Output directory (default: current directory)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🔐 Generating Spring Security configuration with JWT...");

            // Resolve package names
            String resolvedPackageName = resolvePackageName(packageName, "security");
            String resolvedUserPackage = resolvePackageName(userPackage, "model");
            
            // Calculate repository package
            String repositoryPackage = resolvedUserPackage.replaceAll("\\.model$", ".repository");

            SecurityGenerator generator = new SecurityGenerator();
            generator.setPackageName(resolvedPackageName);
            generator.setJwtSecret(jwtSecret);
            generator.setJwtExpiration(jwtExpiration);
            generator.setUserEntity(userEntity);
            generator.setUserPackage(resolvedUserPackage);
            generator.setUserRepositoryPackage(repositoryPackage);
            generator.setEnableCors(enableCors);
            generator.setOutputDirectory(outputDirectory);

            generator.generate();

            System.out.println("✅ Spring Security configuration generated successfully!");
            System.out.println("📁 Package: " + resolvedPackageName);
            System.out.println("🔑 JWT Secret: " + (jwtSecret.length() > 20 ? jwtSecret.substring(0, 20) + "..." : jwtSecret));
            System.out.println("⏰ JWT Expiration: " + jwtExpiration + "ms (" + (jwtExpiration / 3600000) + "h)");
            
            return 0;
        } catch (Exception e) {
            log.error("❌ Error generating Spring Security configuration: {}", e.getMessage(), e);
            System.err.println("❌ Error: " + e.getMessage());
            return 1;
        }
    }

    private String resolvePackageName(String inputPackage, String defaultSubPackage) {
        if (inputPackage == null || inputPackage.trim().isEmpty()) {
            // No package specified, use default sub-package
            String basePackage = ProjectUtils.detectBasePackage();
            return basePackage != null ? basePackage + "." + defaultSubPackage : "com.example." + defaultSubPackage;
        }

        if (inputPackage.contains(".")) {
            // Absolute package
            return inputPackage;
        } else {
            // Relative package
            String basePackage = ProjectUtils.detectBasePackage();
            return basePackage != null ? basePackage + "." + inputPackage : "com.example." + inputPackage;
        }
    }
}
