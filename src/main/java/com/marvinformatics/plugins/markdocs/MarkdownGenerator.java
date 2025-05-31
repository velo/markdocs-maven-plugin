package com.marvinformatics.plugins.markdocs;

import java.util.List;

public class MarkdownGenerator {

    public String generateOverview(PluginDescriptor descriptor, String title) {
        StringBuilder sb = new StringBuilder();
        
        String pluginTitle = title != null ? title : (descriptor.getName() + " Goals Documentation");
        sb.append("# ").append(pluginTitle).append("\n\n");
        
        sb.append("This directory provides detailed documentation for each Maven goal available in the ");
        sb.append(descriptor.getName()).append(". Click on the goal name for full details:\n\n");
        
        List<Goal> goals = descriptor.getGoals();
        if (!goals.isEmpty()) {
            for (Goal goal : goals) {
                sb.append("- [").append(goal.getName()).append("](").append(goal.getName()).append(".md)");
                if (goal.getDescription() != null) {
                    String shortDesc = goal.getDescription().split("\\.")[0]; // Take first sentence
                    if (shortDesc.length() > 80) {
                        shortDesc = shortDesc.substring(0, 77) + "...";
                    }
                    sb.append(": ").append(shortDesc).append(".");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }

    public String generateGoalPage(Goal goal) {
        StringBuilder sb = new StringBuilder();
        
        // Capitalize first letter of goal name for title
        String title = goal.getName().substring(0, 1).toUpperCase() + goal.getName().substring(1).replace("-", " ") + " Goal";
        sb.append("# ").append(title).append("\n\n");
        
        sb.append("**Goal Name:** `").append(goal.getName()).append("`\n\n");
        
        if (goal.getPhase() != null) {
            sb.append("**Phase:** `").append(goal.getPhase()).append("`\n\n");
        }
        
        if (goal.getDescription() != null) {
            sb.append("**Description:**  \n");
            sb.append(goal.getDescription()).append("\n\n");
        }
        
        sb.append("**Usage:**  \n");
        sb.append("This goal is bound to the `").append(goal.getPhase() != null ? goal.getPhase() : "unspecified")
          .append("` phase and is triggered automatically during the Maven build when the plugin is activated.");
        sb.append("\n\n");
        
        List<Parameter> parameters = goal.getParameters();
        if (!parameters.isEmpty()) {
            sb.append("**Parameters:**\n\n");
            
            for (Parameter param : parameters) {
                String paramType = param.getType() != null ? param.getType() : "String";
                String shortType = paramType.contains(".") ? paramType.substring(paramType.lastIndexOf(".") + 1) : paramType;
                String requiredText = param.isRequired() ? "Required" : "Optional";
                
                sb.append("- **").append(param.getName()).append("** (").append(shortType).append(", ").append(requiredText).append(")\n");
                
                if (param.getDescription() != null) {
                    String desc = param.getDescription().trim();
                    sb.append("  ").append(desc);
                    if (!desc.endsWith(".")) {
                        sb.append(".");
                    }
                    sb.append("\n");
                }
                
                if (param.getDefaultValue() != null) {
                    sb.append("  *Default:* `").append(param.getDefaultValue()).append("`\n");
                } else if (!param.isRequired()) {
                    sb.append("  *Default:* None.\n");
                }
                
                sb.append("\n");
            }
        }
        
        // Only show example configuration if there are configurable parameters
        List<Parameter> configurableParams = parameters.stream()
            .filter(p -> p.isEditable() && p.getType() != null && 
                       !p.getType().equals("org.apache.maven.execution.MavenSession") &&
                       !p.getType().equals("org.apache.maven.project.MavenProject"))
            .toList();
            
        if (!configurableParams.isEmpty()) {
            sb.append("**Example Configuration:**\n\n");
            sb.append("```\n");
            sb.append("<plugin>\n");
            sb.append("  <groupId><!-- groupId --></groupId>\n");
            sb.append("  <artifactId><!-- artifactId --></artifactId>\n");
            sb.append("  <version><!-- version --></version>\n");
            sb.append("  <configuration>\n");
            
            for (Parameter param : configurableParams) {
                if (param.isRequired()) {
                    sb.append("    <").append(param.getName()).append("><!-- required --></").append(param.getName()).append(">\n");
                } else {
                    sb.append("    <").append(param.getName()).append("><!-- optional --></").append(param.getName()).append(">\n");
                }
            }
            
            sb.append("  </configuration>\n");
            sb.append("</plugin>\n");
            sb.append("```\n\n");
        }
        
        return sb.toString();
    }
}