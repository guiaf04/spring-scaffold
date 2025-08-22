package com.scaffold.templates;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class TemplateEngine {

    private final MustacheFactory mustacheFactory;

    public TemplateEngine() {
        this.mustacheFactory = new DefaultMustacheFactory("templates/");
    }

    public String processTemplate(String templateName, Map<String, Object> context) {
        try {
            Mustache mustache = mustacheFactory.compile(templateName);
            StringWriter writer = new StringWriter();
            mustache.execute(writer, context);
            return writer.toString();
        } catch (Exception e) {
            log.error("Erro ao processar template: {}", templateName, e);
            throw new RuntimeException("Erro ao processar template: " + templateName, e);
        }
    }

    public String processTemplateString(String templateContent, Map<String, Object> context) {
        try {
            Mustache mustache = mustacheFactory.compile(new StringReader(templateContent), "inline");
            StringWriter writer = new StringWriter();
            mustache.execute(writer, context);
            return writer.toString();
        } catch (Exception e) {
            log.error("Erro ao processar template inline", e);
            throw new RuntimeException("Erro ao processar template inline", e);
        }
    }
}
