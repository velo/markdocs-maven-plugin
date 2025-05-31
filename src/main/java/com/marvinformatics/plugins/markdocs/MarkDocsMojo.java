package com.marvinformatics.plugins.markdocs;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.SITE)
public class MarkDocsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "markdocs.pluginDescriptor", 
               defaultValue = "${project.build.directory}/classes/META-INF/maven/plugin.xml")
    private File pluginDescriptor;

    @Parameter(property = "markdocs.outputDirectory", 
               defaultValue = "${project.build.directory}/markdocs")
    private File outputDirectory;

    @Parameter(property = "markdocs.title", defaultValue = "${project.name}")
    private String title;

    private PluginDescriptorParser parser;
    private MarkdownGenerator markdownGenerator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Generating markdown documentation for Maven plugin");

        if (!pluginDescriptor.exists()) {
            throw new MojoExecutionException("Plugin descriptor not found: " + pluginDescriptor.getAbsolutePath());
        }

        try {
            parser = new PluginDescriptorParser();
            markdownGenerator = new MarkdownGenerator();

            PluginDescriptor descriptor = parser.parse(pluginDescriptor);
            
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            generateOverviewPage(descriptor);
            generateGoalPages(descriptor);

            getLog().info("Markdown documentation generated in: " + outputDirectory.getAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate markdown documentation", e);
        }
    }

    private void generateOverviewPage(PluginDescriptor descriptor) throws IOException {
        String content = markdownGenerator.generateOverview(descriptor, title);
        File overviewFile = new File(outputDirectory, "README.md");
        FileUtils.writeStringToFile(overviewFile, content, StandardCharsets.UTF_8);
        getLog().info("Generated overview: " + overviewFile.getName());
    }

    private void generateGoalPages(PluginDescriptor descriptor) throws IOException {
        List<Goal> goals = descriptor.getGoals();
        for (Goal goal : goals) {
            String content = markdownGenerator.generateGoalPage(goal);
            File goalFile = new File(outputDirectory, goal.getName() + ".md");
            FileUtils.writeStringToFile(goalFile, content, StandardCharsets.UTF_8);
            getLog().info("Generated goal page: " + goalFile.getName());
        }
    }
}