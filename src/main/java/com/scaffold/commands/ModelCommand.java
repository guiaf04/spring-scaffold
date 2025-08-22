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

@Slf4j
@Command(
    name = "model",
    description = {
        "Gera uma classe model/entity com annotations JPA e Lombok.",
        "",
        "@|underline Exemplos:|@",
        "  @|yellow spring-scaffold model User|@",
        "  @|yellow spring-scaffold model Product name:String price:BigDecimal|@",
        "  @|yellow spring-scaffold model Customer --pkg com.example.entity|@"
    },
    mixinStandardHelpOptions = true
)
public class ModelCommand implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Nome da classe model (ex: User, Product, Customer)"
    )
    private String className;

    @Parameters(
        index = "1..*",
        arity = "0..*",
        description = "Campos no formato 'nome:tipo' (ex: name:String age:Integer)"
    )
    private List<String> fieldParams = new ArrayList<>();

    @Option(
        names = {"-p", "--package", "--pkg"},
        description = "Pacote da classe (padrão: ${DEFAULT-VALUE})",
        defaultValue = "com.example.model"
    )
    private String packageName;

    @Option(
        names = {"-f", "--fields"},
        description = {
            "Lista de campos no formato 'nome:tipo,nome:tipo' (alternativa aos parâmetros posicionais)",
            "Tipos suportados: String, Integer, Long, Boolean, BigDecimal, LocalDate, LocalDateTime",
            "Exemplo: 'name:String,age:Integer,active:Boolean'"
        },
        split = ","
    )
    private String[] fields = {};

    @Option(
        names = {"-t", "--table", "--tbl"},
        description = "Nome da tabela no banco (padrão: nome da classe em snake_case)"
    )
    private String tableName;

    @Option(
        names = {"--jpa", "--entity"},
        description = "Incluir annotations JPA (@Entity, @Table, etc.) (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean includeJpa;

    @Option(
        names = {"--lombok", "--data"},
        description = "Usar Lombok annotations (@Data, @Entity, etc.) (padrão: ${DEFAULT-VALUE})",
        defaultValue = "true"
    )
    private boolean useLombok;

    @Option(
        names = {"--validation", "--valid"},
        description = "Incluir Bean Validation annotations (padrão: ${DEFAULT-VALUE})",
        defaultValue = "false"
    )
    private boolean includeValidation;

    @Option(
        names = {"-o", "--output", "--out"},
        description = "Diretório de saída (padrão: diretório atual)"
    )
    private String outputDirectory = ".";

    @Override
    public Integer call() throws Exception {
        try {
            log.info("🚀 Gerando model: {}", className);
            if (className == null || className.trim().isEmpty()) {
                System.err.println("❌ Nome da classe é obrigatório");
                return 1;
            }
            List<FieldInfo> fieldInfoList = parseFields();
            ModelGenerator generator = new ModelGenerator();
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
                    System.err.println("⚠️  Campo inválido ignorado: " + field);
                    System.err.println("   Formato esperado: nome:tipo");
                }
            }
        }
        
        if (fieldInfoList.isEmpty()) {
            log.info("Nenhum campo especificado, adicionando apenas ID");
        }
        
        return fieldInfoList;
    }
}
