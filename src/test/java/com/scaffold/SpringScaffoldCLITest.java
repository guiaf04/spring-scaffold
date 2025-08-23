package com.scaffold;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for Spring Scaffold CLI
 */
public class SpringScaffoldCLITest {

    @Test
    @DisplayName("CLI should instantiate without errors")
    public void testCLIInstantiation() {
        // Basic test to ensure the CLI class can be instantiated
        assertDoesNotThrow(() -> {
            SpringScaffoldCLI cli = new SpringScaffoldCLI();
            assertNotNull(cli);
        });
    }

    @Test
    @DisplayName("Utility classes should be available")
    public void testUtilityClasses() {
        // Test that utility classes can be accessed
        assertDoesNotThrow(() -> {
            Class.forName("com.scaffold.utils.FileUtils");
            Class.forName("com.scaffold.templates.TemplateEngine");
        });
    }

    @Test
    @DisplayName("Command classes should be available")
    public void testCommandClasses() {
        // Test that command classes can be accessed
        assertDoesNotThrow(() -> {
            Class.forName("com.scaffold.commands.ProjectCommand");
            Class.forName("com.scaffold.commands.ModelCommand");
        });
    }
}
