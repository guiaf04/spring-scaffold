package com.scaffold.generators;

import com.scaffold.commands.ProjectCommand.DatabaseType;
import com.scaffold.commands.ProjectCommand.PackagingType;
import com.scaffold.templates.TemplateEngine;
import com.scaffold.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerador de projetos Spring Boot
 */
@Slf4j
public class ProjectGenerator {

    private final TemplateEngine templateEngine;

    public ProjectGenerator() {
        this.templateEngine = new TemplateEngine();
    }

    public boolean generate(
            String projectName,
            String basePackage,
            String groupId,
            String springBootVersion,
            String javaVersion,
            String[] dependencies,
            DatabaseType database,
            PackagingType packaging,
            boolean includeDocker,
            boolean includeGitignore,
            boolean includeReadme,
            String outputDirectory) {
        
        try {
            log.info("Criando projeto Spring Boot: {}", projectName);
            
            // Preparar contexto para os templates
            Map<String, Object> context = new HashMap<>();
            context.put("projectName", projectName);
            context.put("basePackage", basePackage);
            context.put("groupId", groupId);
            context.put("springBootVersion", springBootVersion);
            context.put("javaVersion", javaVersion);
            context.put("dependencies", Arrays.asList(dependencies));
            context.put("database", database.name());
            context.put("packaging", packaging.name());
            context.put("includeDocker", includeDocker);
            context.put("includeGitignore", includeGitignore);
            context.put("includeReadme", includeReadme);
            
            // Calcular nomes derivados
            String mainClassName = toPascalCase(projectName) + "Application";
            String artifactId = toKebabCase(projectName);
            
            context.put("mainClassName", mainClassName);
            context.put("artifactId", artifactId);
            
            // Configurações específicas do banco
            Map<String, Object> dbConfig = getDatabaseConfig(database);
            context.putAll(dbConfig);
            
            log.info("Configurações:");
            log.info("- Projeto: {}", projectName);
            log.info("- Artifact ID: {}", artifactId);
            log.info("- Classe Principal: {}", mainClassName);
            log.info("- Banco: {}", database);
            
            String projectPath = outputDirectory + "/" + artifactId;
            
            // Gerar estrutura do projeto
            generateProjectStructure(projectPath, basePackage, context);
            
            log.info("Projeto {} criado com sucesso em {}", projectName, projectPath);
            return true;
            
        } catch (Exception e) {
            log.error("Erro ao criar projeto", e);
            return false;
        }
    }
    
    private void generateProjectStructure(String projectPath, String basePackage, Map<String, Object> context) {
        // Criar estrutura de diretórios
        String srcMainJava = projectPath + "/src/main/java/" + basePackage.replace(".", "/");
        String srcMainResources = projectPath + "/src/main/resources";
        String srcTestJava = projectPath + "/src/test/java/" + basePackage.replace(".", "/");
        
        FileUtils.createDirectories(srcMainJava);
        FileUtils.createDirectories(srcMainResources);
        FileUtils.createDirectories(srcTestJava);
        
        // Gerar pom.xml
        String pomContent = templateEngine.processTemplate("project-pom.xml.mustache", context);
        FileUtils.createFile(projectPath + "/pom.xml", pomContent);
        
        // Gerar classe principal
        String mainClassContent = templateEngine.processTemplate("project-main.java.mustache", context);
        FileUtils.createFile(srcMainJava + "/" + context.get("mainClassName") + ".java", mainClassContent);
        
        // Gerar application.properties
        String propsContent = templateEngine.processTemplate("project-application.properties.mustache", context);
        FileUtils.createFile(srcMainResources + "/application.properties", propsContent);
        
        // Gerar test
        String testContent = templateEngine.processTemplate("project-test.java.mustache", context);
        FileUtils.createFile(srcTestJava + "/" + context.get("mainClassName") + "Tests.java", testContent);
        
        // Arquivos opcionais
        if ((Boolean) context.get("includeDocker")) {
            String dockerContent = templateEngine.processTemplate("project-dockerfile.mustache", context);
            FileUtils.createFile(projectPath + "/Dockerfile", dockerContent);
        }
        
        if ((Boolean) context.get("includeGitignore")) {
            String gitignoreContent = templateEngine.processTemplate("project-gitignore.mustache", context);
            FileUtils.createFile(projectPath + "/.gitignore", gitignoreContent);
        }
        
        if ((Boolean) context.get("includeReadme")) {
            String readmeContent = templateEngine.processTemplate("project-readme.md.mustache", context);
            FileUtils.createFile(projectPath + "/README.md", readmeContent);
        }
    }
    
    private Map<String, Object> getDatabaseConfig(DatabaseType database) {
        Map<String, Object> config = new HashMap<>();
        
        switch (database) {
            case H2:
                config.put("databaseDriver", "org.h2.Driver");
                config.put("databaseUrl", "jdbc:h2:mem:testdb");
                config.put("databaseUsername", "sa");
                config.put("databasePassword", "");
                config.put("hibernateDialect", "org.hibernate.dialect.H2Dialect");
                break;
            case MYSQL:
                config.put("databaseDriver", "com.mysql.cj.jdbc.Driver");
                config.put("databaseUrl", "jdbc:mysql://localhost:3306/database");
                config.put("databaseUsername", "root");
                config.put("databasePassword", "password");
                config.put("hibernateDialect", "org.hibernate.dialect.MySQL8Dialect");
                break;
            case POSTGRESQL:
                config.put("databaseDriver", "org.postgresql.Driver");
                config.put("databaseUrl", "jdbc:postgresql://localhost:5432/database");
                config.put("databaseUsername", "postgres");
                config.put("databasePassword", "password");
                config.put("hibernateDialect", "org.hibernate.dialect.PostgreSQLDialect");
                break;
        }
        
        return config;
    }
    
    private String toPascalCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        
        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            } else {
                capitalizeNext = true;
            }
        }
        
        return result.toString();
    }
    
    private String toKebabCase(String input) {
        return input.toLowerCase()
                   .replaceAll("[^a-z0-9]+", "-")
                   .replaceAll("^-|-$", "");
    }
}
