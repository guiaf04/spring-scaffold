package com.scaffold.generators;

import com.scaffold.models.SecurityConfig;
import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
public class SecurityGenerator {
    
    private final TemplateEngine templateEngine;
    
    private String packageName;
    private String jwtSecret;
    private long jwtExpiration;
    private String userEntity;
    private String userPackage;
    private String userRepositoryPackage;
    private boolean enableCors;
    private String outputDirectory;

    public SecurityGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public void generate() throws IOException {
        log.info("Generating Spring Security configuration files...");

        // Create security configuration model
        SecurityConfig config = SecurityConfig.builder()
                .packageName(packageName)
                .jwtSecret(jwtSecret)
                .jwtExpiration(jwtExpiration)
                .userEntity(userEntity)
                .userPackage(userPackage)
                .userRepositoryPackage(userRepositoryPackage)
                .enableCors(enableCors)
                .build();

        // Create package directory
        String packagePath = packageName.replace(".", "/");
        Path securityDir = Paths.get(outputDirectory, "src/main/java", packagePath);
        Files.createDirectories(securityDir);

        // Generate configuration files
        generateSecurityConfig(config, securityDir);
        generateJwtUtils(config, securityDir);
        generateJwtAuthenticationEntryPoint(config, securityDir);
        generateJwtAuthenticationFilter(config, securityDir);
        generateUserDetailsServiceImpl(config, securityDir);
        generateUserPrincipal(config, securityDir);
        generateAuthController(config, securityDir);
        generateJwtRequest(config, securityDir);
        generateJwtResponse(config, securityDir);

        log.info("Spring Security configuration generated successfully");
    }

    private void generateSecurityConfig(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("security-config.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "SecurityConfig");
        FileUtils.createFile(filePath, content);
        log.info("Generated SecurityConfig.java");
    }

    private void generateJwtUtils(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("jwt-utils.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "JwtUtils");
        FileUtils.createFile(filePath, content);
        log.info("Generated JwtUtils.java");
    }

    private void generateJwtAuthenticationEntryPoint(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("jwt-authentication-entry-point.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "JwtAuthenticationEntryPoint");
        FileUtils.createFile(filePath, content);
        log.info("Generated JwtAuthenticationEntryPoint.java");
    }

    private void generateJwtAuthenticationFilter(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("jwt-authentication-filter.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "JwtAuthenticationFilter");
        FileUtils.createFile(filePath, content);
        log.info("Generated JwtAuthenticationFilter.java");
    }

    private void generateUserDetailsServiceImpl(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("user-details-service-impl.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "UserDetailsServiceImpl");
        FileUtils.createFile(filePath, content);
        log.info("Generated UserDetailsServiceImpl.java");
    }

    private void generateUserPrincipal(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("user-principal.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "UserPrincipal");
        FileUtils.createFile(filePath, content);
        log.info("Generated UserPrincipal.java");
    }

    private void generateAuthController(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        // Create controller in parent package/controller
        String controllerPackage = config.getPackageName().replaceAll("\\.security$", ".controller");
        String controllerPath = controllerPackage.replace(".", "/");
        Path controllerDir = Paths.get(outputDirectory, "src/main/java", controllerPath);
        Files.createDirectories(controllerDir);

        // Update config for controller
        SecurityConfig controllerConfig = config.toBuilder()
                .controllerPackage(controllerPackage)
                .build();
        templateData.put("config", controllerConfig);

        String content = templateEngine.processTemplate("auth-controller.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(controllerDir.toString(), "", "AuthController");
        FileUtils.createFile(filePath, content);
        log.info("Generated AuthController.java");
    }

    private void generateJwtRequest(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("jwt-request.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "JwtRequest");
        FileUtils.createFile(filePath, content);
        log.info("Generated JwtRequest.java");
    }

    private void generateJwtResponse(SecurityConfig config, Path outputDir) throws IOException {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("config", config);

        String content = templateEngine.processTemplate("jwt-response.java.mustache", templateData);
        String filePath = FileUtils.buildFilePath(outputDir.toString(), "", "JwtResponse");
        FileUtils.createFile(filePath, content);
        log.info("Generated JwtResponse.java");
    }
}
