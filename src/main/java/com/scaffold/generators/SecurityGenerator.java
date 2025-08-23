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

        // Add Maven dependencies
        addSecurityDependencies();

        // Update User repository to include JWT methods
        updateUserRepository(config);

        log.info("Spring Security configuration generated successfully");
    }

    private void addSecurityDependencies() throws IOException {
        log.info("Adding Spring Security and JWT dependencies to pom.xml...");
        
        Path pomFile = Paths.get(outputDirectory, "pom.xml");
        if (!Files.exists(pomFile)) {
            log.warn("pom.xml not found at: {}", pomFile);
            return;
        }

        String pomContent = Files.readString(pomFile);
        
        // Check if dependencies already exist
        if (pomContent.contains("spring-boot-starter-security") && pomContent.contains("jjwt")) {
            log.info("Security dependencies already exist in pom.xml");
            return;
        }

        // Find the dependencies section
        String dependenciesStart = "<dependencies>";
        String dependenciesEnd = "</dependencies>";
        
        int startIndex = pomContent.indexOf(dependenciesStart);
        int endIndex = pomContent.indexOf(dependenciesEnd);
        
        if (startIndex == -1 || endIndex == -1) {
            log.warn("Could not find dependencies section in pom.xml");
            return;
        }

        StringBuilder newPomContent = new StringBuilder();
        newPomContent.append(pomContent.substring(0, endIndex));
        
        // Add Spring Security dependency if not present
        if (!pomContent.contains("spring-boot-starter-security")) {
            newPomContent.append("\n\t\t<!-- Spring Security -->\n");
            newPomContent.append("\t\t<dependency>\n");
            newPomContent.append("\t\t\t<groupId>org.springframework.boot</groupId>\n");
            newPomContent.append("\t\t\t<artifactId>spring-boot-starter-security</artifactId>\n");
            newPomContent.append("\t\t</dependency>\n");
        }
        
        // Add JWT dependency if not present
        if (!pomContent.contains("jjwt")) {
            newPomContent.append("\n\t\t<!-- JWT -->\n");
            newPomContent.append("\t\t<dependency>\n");
            newPomContent.append("\t\t\t<groupId>io.jsonwebtoken</groupId>\n");
            newPomContent.append("\t\t\t<artifactId>jjwt</artifactId>\n");
            newPomContent.append("\t\t\t<version>0.9.1</version>\n");
            newPomContent.append("\t\t</dependency>\n");
        }
        
        newPomContent.append(pomContent.substring(endIndex));
        
        Files.writeString(pomFile, newPomContent.toString());
        log.info("Security dependencies added to pom.xml");
    }

    private void updateUserRepository(SecurityConfig config) throws IOException {
        log.info("Updating User repository with JWT authentication methods...");
        
        // Build repository path
        String repositoryPath = userRepositoryPackage.replace(".", "/");
        Path repositoryFile = Paths.get(outputDirectory, "src/main/java", repositoryPath, userEntity + "Repository.java");
        
        if (!Files.exists(repositoryFile)) {
            log.warn("User repository not found at: {}", repositoryFile);
            return;
        }
        
        try {
            String content = Files.readString(repositoryFile);
            
            // Check if JWT methods already exist
            if (content.contains("findByUsername") && content.contains("existsByUsername")) {
                log.info("JWT methods already exist in User repository");
                return;
            }
            
            // Add Optional import if not present
            if (!content.contains("import java.util.Optional;")) {
                String importLine = "import java.util.Optional;\n";
                int packageIndex = content.indexOf("import ");
                if (packageIndex > 0) {
                    content = content.substring(0, packageIndex) + importLine + content.substring(packageIndex);
                }
            }
            
            // Add JWT methods before the closing brace
            String jwtMethods = "\n    // JWT Authentication methods\n" +
                              "    Optional<" + userEntity + "> findByUsername(String username);\n" +
                              "    Boolean existsByUsername(String username);\n";
            
            // Find the last closing brace and add methods before it
            int lastBrace = content.lastIndexOf("}");
            if (lastBrace > 0) {
                String updatedContent = content.substring(0, lastBrace) + jwtMethods + "}";
                Files.writeString(repositoryFile, updatedContent);
                log.info("Updated User repository with JWT methods");
            }
            
        } catch (IOException e) {
            log.error("Failed to update User repository: {}", e.getMessage());
        }
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
