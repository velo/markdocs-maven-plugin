package com.marvinformatics.plugins.markdocs;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Expected Documentation Comparison Tests")
class ExpectedDocsComparisonTest {

  private PluginDescriptorParser parser;
  private MarkdownGenerator generator;
  private PluginDescriptor descriptor;
  private File expectedDocsDir;

  @BeforeEach
  void setUp() throws Exception {
    parser = new PluginDescriptorParser();
    generator = new MarkdownGenerator();
    File pluginXml = new File("src/test/resources/examples/easyjacoco/plugin.xml");
    expectedDocsDir = new File("src/test/resources/examples/easyjacoco/docs");

    descriptor = parser.parse(pluginXml);
  }

  @Test
  @DisplayName("Should generate overview structure matching expected format")
  void testOverviewStructureMatchesExpected() throws IOException {
    File expectedReadme = new File(expectedDocsDir, "README.md");
    assertThat(expectedReadme).exists();

    String expected = Files.readString(expectedReadme.toPath(), StandardCharsets.UTF_8);
    String generated =
        generator.generateOverview(descriptor, "Easy JaCoCo Maven Plugin Goals Documentation");

    // Verify key structural elements are present in both
    assertThat(expected).startsWith("# ");
    assertThat(generated).startsWith("# ");

    assertThat(expected).contains("[check-project](check-project.md)");
    assertThat(generated).contains("[check-project](check-project.md)");

    // Verify all goals are mentioned in both
    List<String> goalNames =
        Arrays.asList(
            "check-project", "help", "instrument-jar", "persist-report-project", "report-project");

    for (String goalName : goalNames) {
      assertThat(expected).contains(goalName);
      assertThat(generated).contains(goalName);
    }
  }

  @Test
  @DisplayName("Should generate check-project goal structure similar to expected")
  void testCheckProjectGoalStructureComparison() throws IOException {
    File expectedFile = new File(expectedDocsDir, "check-project.md");
    assertThat(expectedFile).exists();

    String expected = Files.readString(expectedFile.toPath(), StandardCharsets.UTF_8);

    Goal checkProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "check-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("check-project goal not found"));

    String generated = generator.generateGoalPage(checkProjectGoal);

    // Verify structural elements
    assertThat(expected).contains("# ");
    assertThat(generated).contains("# ");

    assertThat(expected).contains("check-project");
    assertThat(generated).contains("check-project");

    assertThat(expected).contains("verify");
    assertThat(generated).contains("verify");

    // Verify key parameters are mentioned in both
    List<String> keyParams = Arrays.asList("dataFileIncludes", "haltOnFailure", "projectRules");
    for (String param : keyParams) {
      assertThat(expected).contains(param);
      assertThat(generated).contains(param);
    }
  }

  @Test
  @DisplayName("Should generate help goal structure similar to expected")
  void testHelpGoalStructureComparison() throws IOException {
    File expectedFile = new File(expectedDocsDir, "help.md");
    assertThat(expectedFile).exists();

    String expected = Files.readString(expectedFile.toPath(), StandardCharsets.UTF_8);

    Goal helpGoal =
        descriptor.getGoals().stream()
            .filter(g -> "help".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("help goal not found"));

    String generated = generator.generateGoalPage(helpGoal);

    // Verify key elements
    assertThat(expected).contains("help");
    assertThat(generated).contains("help");

    assertThat(expected).contains("help information");
    assertThat(generated).contains("help information");

    // Verify parameters
    List<String> helpParams = Arrays.asList("detail", "goal", "indentSize", "lineLength");
    for (String param : helpParams) {
      assertThat(expected).contains(param);
      assertThat(generated).contains(param);
    }
  }

  @Test
  @DisplayName("Should generate instrument-jar goal structure similar to expected")
  void testInstrumentJarGoalStructureComparison() throws IOException {
    File expectedFile = new File(expectedDocsDir, "instrument-jar.md");
    assertThat(expectedFile).exists();

    String expected = Files.readString(expectedFile.toPath(), StandardCharsets.UTF_8);

    Goal instrumentJarGoal =
        descriptor.getGoals().stream()
            .filter(g -> "instrument-jar".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("instrument-jar goal not found"));

    String generated = generator.generateGoalPage(instrumentJarGoal);

    // Verify key elements
    assertThat(expected).contains("instrument-jar");
    assertThat(generated).contains("instrument-jar");

    assertThat(expected).contains("package");
    assertThat(generated).contains("package");

    assertThat(expected).contains("instrument");
    assertThat(generated).contains("instrument");

    // Verify required parameters
    List<String> requiredParams = Arrays.asList("destination", "source");
    for (String param : requiredParams) {
      assertThat(expected).contains(param);
      assertThat(generated).contains(param);
    }
  }

  @Test
  @DisplayName("Should generate report-project goal structure similar to expected")
  void testReportProjectGoalStructureComparison() throws IOException {
    File expectedFile = new File(expectedDocsDir, "report-project.md");
    assertThat(expectedFile).exists();

    String expected = Files.readString(expectedFile.toPath(), StandardCharsets.UTF_8);

    Goal reportProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "report-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("report-project goal not found"));

    String generated = generator.generateGoalPage(reportProjectGoal);

    // Verify key elements
    assertThat(expected).contains("report-project");
    assertThat(generated).contains("report-project");

    assertThat(expected).contains("verify");
    assertThat(generated).contains("verify");

    assertThat(expected).contains("aggregated");
    assertThat(generated).contains("aggregated");

    // Verify common parameters
    List<String> commonParams = Arrays.asList("outputDirectory", "formats", "excludes", "includes");
    for (String param : commonParams) {
      assertThat(expected).contains(param);
      assertThat(generated).contains(param);
    }
  }

  @Test
  @DisplayName("Should generate persist-report-project goal structure similar to expected")
  void testPersistReportProjectGoalStructureComparison() throws IOException {
    File expectedFile = new File(expectedDocsDir, "persist-report-project.md");
    assertThat(expectedFile).exists();

    String expected = Files.readString(expectedFile.toPath(), StandardCharsets.UTF_8);

    Goal persistReportProjectGoal =
        descriptor.getGoals().stream()
            .filter(g -> "persist-report-project".equals(g.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("persist-report-project goal not found"));

    String generated = generator.generateGoalPage(persistReportProjectGoal);

    // Verify key elements
    assertThat(expected).contains("persist-report-project");
    assertThat(generated).contains("persist-report-project");

    assertThat(expected).contains("validate");
    assertThat(generated).contains("validate");
  }

  @Test
  @DisplayName("Should have corresponding goals for all expected documentation files")
  void testAllExpectedFilesHaveCorrespondingGoals() {
    File[] expectedFiles =
        expectedDocsDir.listFiles((dir, name) -> name.endsWith(".md") && !name.equals("README.md"));
    assertThat(expectedFiles).isNotNull();
    assertThat(expectedFiles).isNotEmpty();

    List<String> goalNames =
        descriptor.getGoals().stream().map(Goal::getName).collect(Collectors.toList());

    for (File expectedFile : expectedFiles) {
      String fileName = expectedFile.getName();
      String goalName = fileName.substring(0, fileName.lastIndexOf(".md"));

      assertThat(goalNames).contains(goalName);
      System.out.println("✓ Found goal '" + goalName + "' for expected file '" + fileName + "'");
    }
  }

  @Test
  @DisplayName("Should generate all required sections for each goal page")
  void testGeneratedContentHasRequiredSections() {
    for (Goal goal : descriptor.getGoals()) {
      String generated = generator.generateGoalPage(goal);

      // Every goal page should have these sections
      assertThat(generated)
          .as("Goal %s should have title", goal.getName())
          .contains("# " + goal.getName().substring(0, 1).toUpperCase());

      assertThat(generated)
          .as("Goal %s should have goal name section", goal.getName())
          .contains("**Goal Name:**");

      assertThat(generated)
          .as("Goal %s should have usage section", goal.getName())
          .contains("**Usage:**");

      if (!goal.getParameters().isEmpty()) {
        assertThat(generated)
            .as("Goal %s should have parameters section", goal.getName())
            .contains("**Parameters:**");
      }

      if (goal.getPhase() != null) {
        assertThat(generated)
            .as("Goal %s should have phase section", goal.getName())
            .contains("**Phase:**");
      }
    }
  }

  @Test
  @DisplayName("Should maintain consistent markdown link format across overview and goals")
  void testMarkdownLinkConsistency() throws IOException {
    String overview =
        generator.generateOverview(descriptor, "Easy JaCoCo Maven Plugin Goals Documentation");

    // Extract all goal links from overview
    List<String> goalNames =
        descriptor.getGoals().stream().map(Goal::getName).collect(Collectors.toList());

    for (String goalName : goalNames) {
      String expectedLink = "[" + goalName + "](" + goalName + ".md)";
      assertThat(overview)
          .as("Overview should contain link: %s", expectedLink)
          .contains(expectedLink);

      // Verify corresponding goal page can be generated
      Goal goal =
          descriptor.getGoals().stream()
              .filter(g -> goalName.equals(g.getName()))
              .findFirst()
              .orElseThrow(() -> new AssertionError("Goal not found: " + goalName));

      String goalPage = generator.generateGoalPage(goal);
      assertThat(goalPage)
          .as("Should be able to generate page for %s", goalName)
          .isNotNull()
          .isNotBlank();
    }
  }

  @Test
  @DisplayName("Should generate proper parameter documentation for each goal")
  void testParameterDocumentationQuality() {
    for (Goal goal : descriptor.getGoals()) {
      if (goal.getParameters().isEmpty()) {
        continue;
      }

      String generated = generator.generateGoalPage(goal);

      // Should have parameters section
      assertThat(generated).contains("**Parameters:**");

      // Each parameter should be properly formatted
      for (Parameter param : goal.getParameters()) {
        assertThat(generated)
            .as("Goal %s should document parameter %s", goal.getName(), param.getName())
            .contains("**" + param.getName() + "**");

        if (param.isRequired()) {
          assertThat(generated)
              .as("Required parameter %s should be marked as Required", param.getName())
              .contains("Required");
        } else {
          assertThat(generated)
              .as("Optional parameter %s should be marked as Optional", param.getName())
              .contains("Optional");
        }

        if (param.getType() != null) {
          String shortType =
              param.getType().contains(".")
                  ? param.getType().substring(param.getType().lastIndexOf(".") + 1)
                  : param.getType();
          assertThat(generated)
              .as("Parameter %s should show type information", param.getName())
              .contains(shortType);
        }
      }
    }
  }
}
