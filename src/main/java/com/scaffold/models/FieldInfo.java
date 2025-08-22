package com.scaffold.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa informações sobre um campo/atributo de uma classe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldInfo {
    
    /**
     * Nome do campo
     */
    private String name;
    
    /**
     * Tipo do campo (String, Integer, Long, etc.)
     */
    private String type;
    
    /**
     * Se o campo é obrigatório (para validação)
     */
    private boolean required = false;
    
    /**
     * Tamanho máximo (para String)
     */
    private Integer maxLength;
    
    /**
     * Valor mínimo (para números)
     */
    private String minValue;
    
    /**
     * Valor máximo (para números)
     */
    private String maxValue;
    
    /**
     * Padrão regex para validação
     */
    private String pattern;
    
    /**
     * Comentário/descrição do campo
     */
    private String comment;
    
    /**
     * Se é um campo único
     */
    private boolean unique = false;
    
    /**
     * Se permite null no banco
     */
    private boolean nullable = true;

    /**
     * Construtor simplificado para uso básico
     */
    public FieldInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Construtor com obrigatório
     */
    public FieldInfo(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    /**
     * Verifica se o tipo é uma classe wrapper do Java
     */
    public boolean isWrapperType() {
        return type.equals("Integer") || type.equals("Long") || 
               type.equals("Double") || type.equals("Float") || 
               type.equals("Boolean") || type.equals("Byte") || 
               type.equals("Short") || type.equals("Character");
    }

    /**
     * Verifica se o tipo é uma String
     */
    public boolean isStringType() {
        return "String".equals(type);
    }

    /**
     * Verifica se o tipo é numérico
     */
    public boolean isNumericType() {
        return type.equals("Integer") || type.equals("Long") || 
               type.equals("Double") || type.equals("Float") || 
               type.equals("BigDecimal") || type.equals("BigInteger") ||
               type.equals("int") || type.equals("long") || 
               type.equals("double") || type.equals("float");
    }

    /**
     * Verifica se o tipo é uma data
     */
    public boolean isDateType() {
        return type.equals("LocalDate") || type.equals("LocalDateTime") || 
               type.equals("Date") || type.equals("Timestamp") ||
               type.equals("LocalTime") || type.equals("Instant");
    }

    /**
     * Verifica se o tipo é boolean
     */
    public boolean isBooleanType() {
        return "Boolean".equals(type) || "boolean".equals(type);
    }

    /**
     * Retorna o import necessário para o tipo, se houver
     */
    public String getRequiredImport() {
        switch (type) {
            case "BigDecimal":
                return "java.math.BigDecimal";
            case "BigInteger":
                return "java.math.BigInteger";
            case "LocalDate":
                return "java.time.LocalDate";
            case "LocalDateTime":
                return "java.time.LocalDateTime";
            case "LocalTime":
                return "java.time.LocalTime";
            case "Instant":
                return "java.time.Instant";
            case "Date":
                return "java.util.Date";
            case "Timestamp":
                return "java.sql.Timestamp";
            case "List":
                return "java.util.List";
            case "Set":
                return "java.util.Set";
            case "Map":
                return "java.util.Map";
            default:
                return null;
        }
    }

    /**
     * Retorna a annotation JPA apropriada para o tipo
     */
    public String getJpaColumnAnnotation() {
        StringBuilder annotation = new StringBuilder("@Column");
        
        boolean hasAttributes = false;
        StringBuilder attributes = new StringBuilder();
        
        if (!nullable) {
            attributes.append("nullable = false");
            hasAttributes = true;
        }
        
        if (unique) {
            if (hasAttributes) attributes.append(", ");
            attributes.append("unique = true");
            hasAttributes = true;
        }
        
        if (maxLength != null && isStringType()) {
            if (hasAttributes) attributes.append(", ");
            attributes.append("length = ").append(maxLength);
            hasAttributes = true;
        }
        
        if (hasAttributes) {
            annotation.append("(").append(attributes).append(")");
        }
        
        return annotation.toString();
    }

    /**
     * Retorna as annotations de validação Bean Validation
     */
    public String getValidationAnnotations() {
        StringBuilder annotations = new StringBuilder();
        
        if (required) {
            annotations.append("@NotNull\n    ");
        }
        
        if (isStringType()) {
            if (required) {
                annotations.append("@NotBlank\n    ");
            }
            if (maxLength != null) {
                annotations.append("@Size(max = ").append(maxLength).append(")\n    ");
            }
            if (pattern != null) {
                annotations.append("@Pattern(regexp = \"").append(pattern).append("\")\n    ");
            }
        }
        
        if (isNumericType()) {
            if (minValue != null) {
                annotations.append("@Min(").append(minValue).append(")\n    ");
            }
            if (maxValue != null) {
                annotations.append("@Max(").append(maxValue).append(")\n    ");
            }
        }
        
        String result = annotations.toString();
        return result.endsWith("\n    ") ? result.substring(0, result.length() - 5) : result;
    }
}
