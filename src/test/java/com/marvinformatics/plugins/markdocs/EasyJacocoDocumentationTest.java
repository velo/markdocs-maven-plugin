package com.marvinformatics.plugins.markdocs;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Easy JaCoCo Plugin Documentation Generation Tests")
class EasyJacocoDocumentationTest {

  private PluginDescriptorParser parser;
  private MarkdownGenerator generator;
  private PluginDescriptor descriptor;
  private File pluginXml;
  private File expectedDocsDir;

  @BeforeEach
  void setUp() throws Exception {
    parser = new PluginDescriptorParser();
    generator = new MarkdownGenerator();
    pluginXml = new File("src/test/resources/examples/easyjacoco/plugin.xml");
    expectedDocsDir = new File("src/test/resources/examples/easyjacoco/docs");

    assertThat(pluginXml).exists();
    assertThat(expectedDocsDir).exists();

    descriptor = parser.parse(pluginXml);
  }

  @Test
  @DisplayName("Should correctly parse plugin descriptor")
  void testPluginDescriptorParsing() {
    assertThat(descriptor.getName()).isEqualTo("easy-jacoco-maven-plugin");
    assertThat(descriptor.getGroupId()).isEqualTo("com.marvinformatics.jacoco");
    assertThat(descriptor.getArtifactId()).isEqualTo("easy-jacoco-maven-plugin");
    assertThat(descriptor.getVersion()).isEqualTo("0.0.1-SNAPSHOT");
    assertThat(descriptor.getDescription())
        .isNotNull()
        .contains("This project aims at making jacoco easy");

    List<Goal> goals = descriptor.getGoals();
    assertThat(goals).hasSize(5);

    // Verify goal names
    List<String> goalNames = goals.stream().map(Goal::getName).toList();
    assertThat(goalNames)
        .containsExactlyInAnyOrder(
            "check-project", "help", "instrument-jar", "persist-report-project", "report-project");
  }

  @Test
  @DisplayName("Should generate properly structured overview page")
  void testOverviewPageGeneration() throws IOException {
    String generated =
        generator.generateOverview(descriptor, "Easy JaCoCo Maven Plugin Goals Documentation");

    // Verify structure
    assertThat(generated)
        .startsWith("# Easy JaCoCo Maven Plugin Goals Documentation")
        .contains("This directory provides detailed documentation")
        .contains("easy-jacoco-maven-plugin");

    // Verify all goals are listed with proper links
    assertThat(generated)
        .contains("[check-project](check-project.md)")
        .contains("[help](help.md)")
        .contains("[instrument-jar](instrument-jar.md)")
        .contains("[persist-report-project](persist-report-project.md)")
        .contains("[report-project](report-project.md)");

    // Verify goal descriptions are included and properly formatted
    assertThat(generated).contains(": ");
  }

  @Test
  @DisplayName("Should generate check-project goal page with correct structure")
  void testCheckProjectGoalGeneration() {
    Goal checkProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "check-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("check-project goal not found"));

    String generated = generator.generateGoalPage(checkProjectGoal);

    // Verify structure
    assertThat(generated)
        .contains("# Check project Goal")
        .contains("**Goal Name:** `check-project`")
        .contains("**Phase:** `verify`")
        .contains("**Usage:**")
        .contains("**Parameters:**");

    // Verify parameters
    assertThat(generated)
        .contains("**dataFileExcludes**")
        .contains("**haltOnFailure**")
        .contains("**projectRules**");

    // Verify parameter formatting
    assertThat(generated)
        .contains("Required")
        .contains("Optional")
        .contains("(List,")
        .contains("(boolean,");

    // Verify configuration example exists if configurable parameters present
    List<Parameter> configurableParams =
        checkProjectGoal.getParameters().stream()
            .filter(
                p ->
                    p.isEditable()
                        && p.getType() != null
                        && !p.getType().equals("org.apache.maven.execution.MavenSession")
                        && !p.getType().equals("org.apache.maven.project.MavenProject"))
            .toList();

    if (!configurableParams.isEmpty()) {
      assertThat(generated)
          .contains("**Example Configuration:**")
          .contains("<plugin>")
          .contains("<configuration>");
    }
  }

  @Test
  @DisplayName("Should generate help goal page with description")
  void testHelpGoalGeneration() {
    Goal helpGoal =
        descriptor.getGoals().stream()
            .filter(g -> "help".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("help goal not found"));

    String generated = generator.generateGoalPage(helpGoal);

    // Verify basic structure
    assertThat(generated)
        .contains("# Help Goal")
        .contains("**Goal Name:** `help`")
        .contains("**Description:**")
        .contains("**Usage:**");

    // Verify description content
    assertThat(helpGoal.getDescription()).isNotNull();
    assertThat(generated).contains("Display help information");

    // Verify parameters
    assertThat(generated).contains("**Parameters:**").contains("**detail**").contains("**goal**");
  }

  @Test
  @DisplayName("Should generate instrument-jar goal page with required parameters")
  void testInstrumentJarGoalGeneration() {
    Goal instrumentJarGoal =
        descriptor.getGoals().stream()
            .filter(g -> "instrument-jar".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("instrument-jar goal not found"));

    String generated = generator.generateGoalPage(instrumentJarGoal);

    // Verify structure
    assertThat(generated)
        .contains("# Instrument jar Goal")
        .contains("**Goal Name:** `instrument-jar`")
        .contains("**Phase:** `package`");

    // Verify description
    assertThat(instrumentJarGoal.getDescription()).isNotNull();
    assertThat(generated).contains("Maven Mojo for instrumenting jar files");

    // Verify required parameters
    assertThat(generated).contains("**destination**").contains("**source**").contains("Required");

    // Verify configuration example
    assertThat(generated).contains("**Example Configuration:**");
  }

  @Test
  @DisplayName("Should generate report-project goal page with comprehensive parameters")
  void testReportProjectGoalGeneration() {
    Goal reportProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "report-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("report-project goal not found"));

    String generated = generator.generateGoalPage(reportProjectGoal);

    // Verify structure
    assertThat(generated)
        .contains("# Report project Goal")
        .contains("**Goal Name:** `report-project`")
        .contains("**Phase:** `verify`");

    // Verify description
    assertThat(reportProjectGoal.getDescription()).isNotNull();
    assertThat(generated).contains("Generates an aggregated coverage report");

    // Verify it has many parameters (this goal has the most parameters)
    assertThat(generated)
        .contains("**Parameters:**")
        .contains("**dataFileExcludes**")
        .contains("**outputDirectory**")
        .contains("**formats**");
  }

  @Test
  @DisplayName("Should format parameters correctly with types and requirements")
  void testParameterFormatting() {
    Goal checkProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "check-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("check-project goal not found"));

    String generated = generator.generateGoalPage(checkProjectGoal);

    // Test different parameter types and requirements
    assertThat(generated).contains("(List, Optional)").contains("(boolean, Required)");

    // Test default values formatting
    assertThat(generated).contains("*Default:* None.");

    // Test parameter descriptions are properly formatted
    assertThat(generated).containsPattern("\\*\\*\\w+\\*\\* \\([^)]+\\)\\s+[A-Z]");
  }

  @Test
  @DisplayName("Should have correct phases and descriptions for specific goals")
  void testGoalPhasesAndDescriptions() {
    List<Goal> goals = descriptor.getGoals();

    // Verify specific goals have expected phases
    Goal checkProject =
        goals.stream().filter(g -> "check-project".equals(g.getName())).findFirst().orElse(null);
    assertThat(checkProject).isNotNull().extracting(Goal::getPhase).isEqualTo("verify");

    Goal instrumentJar =
        goals.stream().filter(g -> "instrument-jar".equals(g.getName())).findFirst().orElse(null);
    assertThat(instrumentJar).isNotNull().extracting(Goal::getPhase).isEqualTo("package");

    Goal reportProject =
        goals.stream().filter(g -> "report-project".equals(g.getName())).findFirst().orElse(null);
    assertThat(reportProject).isNotNull().extracting(Goal::getPhase).isEqualTo("verify");

    // Verify goals with descriptions have them
    assertThat(
            goals.stream()
                .filter(g -> "help".equals(g.getName()))
                .findFirst()
                .get()
                .getDescription())
        .isNotNull();

    assertThat(instrumentJar.getDescription()).isNotNull();
    assertThat(reportProject.getDescription()).isNotNull();
  }

  @Test
  @DisplayName("Should generate valid markdown structure")
  void testMarkdownStructureValidation() {
    String overview = generator.generateOverview(descriptor, "Test Title");

    // Verify markdown structure
    assertThat(overview)
        .startsWith("# ")
        .contains("- [")
        .containsPattern("\\[\\w+.*\\]\\(\\w+\\.md\\)");

    // Test goal page structure
    Goal anyGoal = descriptor.getGoals().get(0);
    String goalPage = generator.generateGoalPage(anyGoal);

    assertThat(goalPage)
        .startsWith("# ")
        .contains("**Goal Name:**")
        .contains("`" + anyGoal.getName() + "`");

    if (anyGoal.getPhase() != null) {
      assertThat(goalPage).contains("`" + anyGoal.getPhase() + "`");
    }
  }
}
