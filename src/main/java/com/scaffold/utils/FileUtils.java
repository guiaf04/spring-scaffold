package com.scaffold.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Utilitários para manipulação de arquivos
 */
@Slf4j
public class FileUtils {

    /**
     * Cria um arquivo com o conteúdo especificado
     */
    public static boolean createFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            
            // Criar diretórios pai se não existirem
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
                log.info("Diretórios criados: {}", parentDir);
            }
            
            // Verificar se arquivo já existe
            if (Files.exists(path)) {
                log.warn("Arquivo já existe: {}", filePath);
                return false;
            }
            
            // Criar arquivo
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
            log.info("Arquivo criado: {}", filePath);
            return true;
            
        } catch (IOException e) {
            log.error("Erro ao criar arquivo: {}", filePath, e);
            return false;
        }
    }

    /**
     * Converte pacote Java para caminho de diretório
     * Ex: com.example.model -> com/example/model
     */
    public static String packageToPath(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return "";
        }
        return packageName.replace(".", "/");
    }

    /**
     * Constrói caminho completo do arquivo
     */
    public static String buildFilePath(String outputDirectory, String packageName, String className) {
        StringBuilder path = new StringBuilder();
        
        // Diretório de saída
        if (outputDirectory != null && !outputDirectory.isEmpty() && !outputDirectory.equals(".")) {
            path.append(outputDirectory);
            if (!outputDirectory.endsWith("/")) {
                path.append("/");
            }
        }
        
        // Adicionar src/main/java se não estiver presente
        String pathStr = path.toString();
        if (!pathStr.contains("src/main/java") && !pathStr.isEmpty()) {
            path.append("src/main/java/");
        } else if (pathStr.isEmpty()) {
            path.append("src/main/java/");
        }
        
        // Pacote
        if (packageName != null && !packageName.isEmpty()) {
            path.append(packageToPath(packageName)).append("/");
        }
        
        // Nome da classe
        path.append(className);
        if (!className.endsWith(".java")) {
            path.append(".java");
        }
        
        return path.toString();
    }

    /**
     * Verifica se um arquivo existe
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Cria diretórios se não existirem
     */
    public static boolean createDirectories(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Diretórios criados: {}", directoryPath);
            }
            return true;
        } catch (IOException e) {
            log.error("Erro ao criar diretórios: {}", directoryPath, e);
            return false;
        }
    }
}
