package com.scaffold.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldInfo {
    
    private String name;
    
    private String type;
    
    private boolean required = false;
    
    private Integer maxLength;
    
    private String minValue;
    
    private String maxValue;
    
    private String pattern;
    
    private String comment;
    
    private boolean unique = false;
    
    private boolean nullable = true;

    public FieldInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public FieldInfo(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public boolean isWrapperType() {
        return type.equals("Integer") || type.equals("Long") || 
               type.equals("Double") || type.equals("Float") || 
               type.equals("Boolean") || type.equals("Byte") || 
               type.equals("Short") || type.equals("Character");
    }

    public boolean isStringType() {
        return "String".equals(type);
    }

    public boolean isNumericType() {
        return type.equals("Integer") || type.equals("Long") || 
               type.equals("Double") || type.equals("Float") || 
               type.equals("BigDecimal") || type.equals("BigInteger") ||
               type.equals("int") || type.equals("long") || 
               type.equals("double") || type.equals("float");
    }

    public boolean isDateType() {
        return type.equals("LocalDate") || type.equals("LocalDateTime") || 
               type.equals("Date") || type.equals("Timestamp") ||
               type.equals("LocalTime") || type.equals("Instant");
    }

    public boolean isBooleanType() {
        return "Boolean".equals(type) || "boolean".equals(type);
    }

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
