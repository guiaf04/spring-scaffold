package com.scaffold.commands;

import com.scaffold.generators.ModelGenerator;
import com.scaffold.models.FieldInfo;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Comando para gerar classes Model/Entity
 */
@Slf4j
@Command(
    name = "model",
    description = {
        "Gera uma classe model/entity com annotations JPA e Lombok.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold model User|@",
        "  @|yellow spring-scaffold model Product -f \"name:String,price:BigDecimal\"|@",
        "  @|yellow spring-scaffold model Customer -p com.example.entity --table customers|@"
    },
    mixinStandardHelpOptions = true
)
public class ModelCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome da classe model (ex: User, Product, Customer)"
    )
    private String className;

    @Option(
        names = {"-p", "--package"},
        description = "Pacote da classe (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.model"
    )
    private String packageName;

    @Option(
        names = {"-f", "--fields"},
        description = {
            "Lista de campos no formato 'nome:tipo,nome:tipo'",
            "Tipos suportados: String, Integer, Long, Boolean, BigDecimal, LocalDate, LocalDateTime",
            "Exemplo: 'name:String,age:Integer,active:Boolean'"
        },
        split = ","
    )
    private String[] fields = {};

    @Option(
        names = {"-t", "--table"},
        description = "Nome da tabela no banco (padrão: nome da classe em snake_case)"
    )
    private String tableName;

    @Option(
        names = {"--jpa"},
        description = "Incluir annotations JPA (@Entity, @Table, etc.) (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeJpa;

    @Option(
        names = {"--lombok"},
        description = "Usar Lombok annotations (@Data, @Entity, etc.) (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean useLombok;

    @Option(
        names = {"--validation"},
        description = "Incluir Bean Validation annotations (padrão: ${DEFAULT-VALUE})",
        defaultValue = "false"
    )
    private boolean includeValidation;

    @Option(
        names = {"-o", "--output"},
        description = "Diretório de saída (padrão: diretório atual)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Gerando model: {}", className);
            
            // Validar entrada
            if (className == null || className.trim().isEmpty()) {
                System.err.println("❌ Nome da classe é obrigatório");
                return 1;
            }

            // Processar campos
            List<FieldInfo> fieldInfoList = parseFields();
            
            // Configurar gerador
            ModelGenerator generator = new ModelGenerator();
            
            // Gerar model
            boolean success = generator.generate(
                className,
                packageName,
                fieldInfoList,
                tableName,
                includeJpa,
                useLombok,
                includeValidation,
                outputDirectory
            );

            if (success) {
                System.out.println("✅ Model " + className + " gerado com sucesso!");
                System.out.println("📁 Localização: " + outputDirectory + "/" + 
                    packageName.replace(".", "/") + "/" + className + ".java");
                return 0;
            } else {
                System.err.println("❌ Falha ao gerar model");
                return 1;
            }

        } catch (Exception e) {
            log.error("Erro ao gerar model", e);
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            return 1;
        }
    }

    /**
     * Processa os campos fornecidos via linha de comando
     */
    private List<FieldInfo> parseFields() {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        
        for (String field : fields) {
            if (field != null && !field.trim().isEmpty()) {
                String[] parts = field.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String type = parts[1].trim();
                    
                    if (!name.isEmpty() && !type.isEmpty()) {
                        fieldInfoList.add(new FieldInfo(name, type));
                    }
                } else {
                    System.err.println("⚠️  Campo inválido ignorado: " + field);
                    System.err.println("   Formato esperado: nome:tipo");
                }
            }
        }
        
        // Se não foram especificados campos, adicionar campo ID padrão
        if (fieldInfoList.isEmpty()) {
            log.info("Nenhum campo especificado, adicionando apenas ID");
        }
        
        return fieldInfoList;
    }
}
