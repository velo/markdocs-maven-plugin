package com.marvinformatics.plugins.markdocs;

import java.util.List;

public class MarkdownGenerator {

    public String generateOverview(PluginDescriptor descriptor, String title) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("# ").append(title != null ? title : descriptor.getName()).append("\n\n");
        
        if (descriptor.getDescription() != null) {
            sb.append(descriptor.getDescription()).append("\n\n");
        }
        
        sb.append("## Plugin Information\n\n");
        sb.append("- **Group ID:** ").append(descriptor.getGroupId()).append("\n");
        sb.append("- **Artifact ID:** ").append(descriptor.getArtifactId()).append("\n");
        sb.append("- **Version:** ").append(descriptor.getVersion()).append("\n\n");
        
        List<Goal> goals = descriptor.getGoals();
        if (!goals.isEmpty()) {
            sb.append("## Goals\n\n");
            sb.append("This plugin provides the following goals:\n\n");
            
            for (Goal goal : goals) {
                sb.append("### [").append(goal.getName()).append("](").append(goal.getName()).append(".md)\n\n");
                if (goal.getDescription() != null) {
                    sb.append(goal.getDescription()).append("\n\n");
                }
                if (goal.getPhase() != null) {
                    sb.append("**Default Phase:** ").append(goal.getPhase()).append("\n\n");
                }
            }
        }
        
        sb.append("## Usage\n\n");
        sb.append("Add the plugin to your `pom.xml`:\n\n");
        sb.append("```xml\n");
        sb.append("<plugin>\n");
        sb.append("    <groupId>").append(descriptor.getGroupId()).append("</groupId>\n");
        sb.append("    <artifactId>").append(descriptor.getArtifactId()).append("</artifactId>\n");
        sb.append("    <version>").append(descriptor.getVersion()).append("</version>\n");
        sb.append("</plugin>\n");
        sb.append("```\n\n");
        
        return sb.toString();
    }

    public String generateGoalPage(Goal goal) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("# ").append(goal.getName()).append("\n\n");
        
        if (goal.getDescription() != null) {
            sb.append(goal.getDescription()).append("\n\n");
        }
        
        sb.append("## Goal Information\n\n");
        sb.append("- **Goal Name:** ").append(goal.getName()).append("\n");
        if (goal.getPhase() != null) {
            sb.append("- **Default Phase:** ").append(goal.getPhase()).append("\n");
        }
        if (goal.getImplementation() != null) {
            sb.append("- **Implementation:** ").append(goal.getImplementation()).append("\n");
        }
        sb.append("\n");
        
        List<Parameter> parameters = goal.getParameters();
        if (!parameters.isEmpty()) {
            sb.append("## Parameters\n\n");
            
            for (Parameter param : parameters) {
                sb.append("### ").append(param.getName()).append("\n\n");
                
                if (param.getDescription() != null) {
                    sb.append(param.getDescription()).append("\n\n");
                }
                
                sb.append("- **Type:** ").append(param.getType() != null ? param.getType() : "String").append("\n");
                sb.append("- **Required:** ").append(param.isRequired() ? "Yes" : "No").append("\n");
                sb.append("- **User Property:** ").append(param.getName()).append("\n");
                
                if (param.getDefaultValue() != null) {
                    sb.append("- **Default Value:** ").append(param.getDefaultValue()).append("\n");
                }
                
                sb.append("\n");
            }
        }
        
        sb.append("## Usage Example\n\n");
        sb.append("```xml\n");
        sb.append("<plugin>\n");
        sb.append("    <groupId><!-- group-id --></groupId>\n");
        sb.append("    <artifactId><!-- artifact-id --></artifactId>\n");
        sb.append("    <version><!-- version --></version>\n");
        sb.append("    <executions>\n");
        sb.append("        <execution>\n");
        sb.append("            <goals>\n");
        sb.append("                <goal>").append(goal.getName()).append("</goal>\n");
        sb.append("            </goals>\n");
        
        boolean hasConfigurableParams = parameters.stream()
            .anyMatch(p -> p.isEditable() && !p.isRequired());
            
        if (hasConfigurableParams) {
            sb.append("            <configuration>\n");
            for (Parameter param : parameters) {
                if (param.isEditable() && !param.isRequired()) {
                    sb.append("                <").append(param.getName()).append(">");
                    if (param.getDefaultValue() != null) {
                        sb.append(param.getDefaultValue());
                    } else {
                        sb.append("<!-- value -->");
                    }
                    sb.append("</").append(param.getName()).append(">\n");
                }
            }
            sb.append("            </configuration>\n");
        }
        
        sb.append("        </execution>\n");
        sb.append("    </executions>\n");
        sb.append("</plugin>\n");
        sb.append("```\n\n");
        
        return sb.toString();
    }
}