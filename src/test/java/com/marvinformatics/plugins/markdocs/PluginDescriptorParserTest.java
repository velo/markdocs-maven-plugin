package com.marvinformatics.plugins.markdocs;

import org.junit.Test;
import java.io.File;

public class PluginDescriptorParserTest {

    @Test
    public void testParseEasyJacocoPlugin() throws Exception {
        PluginDescriptorParser parser = new PluginDescriptorParser();
        File pluginXml = new File("src/test/resources/examples/easyjacoco/plugin.xml");
        
        PluginDescriptor descriptor = parser.parse(pluginXml);
        
        System.out.println("Plugin Name: " + descriptor.getName());
        System.out.println("Group ID: " + descriptor.getGroupId());
        System.out.println("Artifact ID: " + descriptor.getArtifactId());
        System.out.println("Version: " + descriptor.getVersion());
        System.out.println("Description: " + descriptor.getDescription());
        System.out.println("Goals count: " + descriptor.getGoals().size());
        
        for (Goal goal : descriptor.getGoals()) {
            System.out.println("Goal: " + goal.getName());
            System.out.println("  Phase: " + goal.getPhase());
            System.out.println("  Parameters: " + goal.getParameters().size());
        }
        
        // Generate markdown
        MarkdownGenerator generator = new MarkdownGenerator();
        String overview = generator.generateOverview(descriptor, "Test Title");
        System.out.println("\n=== OVERVIEW ===\n" + overview);
        
        if (!descriptor.getGoals().isEmpty()) {
            String goalPage = generator.generateGoalPage(descriptor.getGoals().get(0));
            System.out.println("\n=== FIRST GOAL ===\n" + goalPage);
        }
    }
}