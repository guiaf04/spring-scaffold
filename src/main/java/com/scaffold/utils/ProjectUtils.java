package com.scaffold.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class ProjectUtils {

    /**
     * Detects the base package of the current project by looking for the main application class
     * or existing model/entity classes.
     * 
     * @return The detected base package or "com.example" as fallback
     */
    public static String detectBasePackage() {
        try {
            // Look for Spring Boot main class pattern (*Application.java)
            Optional<String> mainClassPackage = findMainClassPackage();
            if (mainClassPackage.isPresent()) {
                return mainClassPackage.get();
            }

            // Look for existing model/entity classes
            Optional<String> modelPackage = findExistingModelPackage();
            if (modelPackage.isPresent()) {
                String basePackage = modelPackage.get();
                // Remove .model suffix to get base package
                if (basePackage.endsWith(".model")) {
                    return basePackage.substring(0, basePackage.length() - 6);
                }
                return basePackage;
            }

            log.debug("Could not detect base package, using default: com.example");
            return "com.example";

        } catch (Exception e) {
            log.warn("Error detecting base package: {}", e.getMessage());
            return "com.example";
        }
    }

    /**
     * Generates the model package based on the base package.
     */
    public static String getModelPackage(String basePackage) {
        return basePackage + ".model";
    }

    /**
     * Generates the repository package based on the base package.
     */
    public static String getRepositoryPackage(String basePackage) {
        return basePackage + ".repository";
    }

    /**
     * Generates the service package based on the base package.
     */
    public static String getServicePackage(String basePackage) {
        return basePackage + ".service";
    }

    /**
     * Generates the controller package based on the base package.
     */
    public static String getControllerPackage(String basePackage) {
        return basePackage + ".controller";
    }

    private static Optional<String> findMainClassPackage() {
        try {
            Path srcPath = Paths.get("src/main/java");
            if (!Files.exists(srcPath)) {
                return Optional.empty();
            }

            try (Stream<Path> paths = Files.walk(srcPath)) {
                return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith("Application.java"))
                    .map(ProjectUtils::extractPackageFromPath)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
            }
        } catch (IOException e) {
            log.debug("Error searching for main class: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<String> findExistingModelPackage() {
        try {
            Path srcPath = Paths.get("src/main/java");
            if (!Files.exists(srcPath)) {
                return Optional.empty();
            }

            // Look for common model/entity directories
            String[] modelDirNames = {"model", "entity", "domain"};
            
            for (String dirName : modelDirNames) {
                try (Stream<Path> paths = Files.walk(srcPath)) {
                    Optional<String> packageFound = paths
                        .filter(Files::isDirectory)
                        .filter(path -> path.getFileName().toString().equals(dirName))
                        .map(ProjectUtils::extractPackageFromPath)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst();
                    
                    if (packageFound.isPresent()) {
                        return packageFound;
                    }
                }
            }

            return Optional.empty();
        } catch (IOException e) {
            log.debug("Error searching for model package: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<String> extractPackageFromPath(Path path) {
        try {
            String pathString = path.toString();
            String srcJavaPath = "src" + File.separator + "main" + File.separator + "java" + File.separator;
            
            int startIndex = pathString.indexOf(srcJavaPath);
            if (startIndex == -1) {
                return Optional.empty();
            }
            
            String packagePath = pathString.substring(startIndex + srcJavaPath.length());
            
            // If it's a file, remove the filename
            if (packagePath.endsWith(".java")) {
                int lastSeparator = packagePath.lastIndexOf(File.separator);
                if (lastSeparator > 0) {
                    packagePath = packagePath.substring(0, lastSeparator);
                }
            }
            
            // Convert file separators to package dots
            String packageName = packagePath.replace(File.separator, ".");
            
            return packageName.isEmpty() ? Optional.empty() : Optional.of(packageName);
            
        } catch (Exception e) {
            log.debug("Error extracting package from path {}: {}", path, e.getMessage());
            return Optional.empty();
        }
    }
}
