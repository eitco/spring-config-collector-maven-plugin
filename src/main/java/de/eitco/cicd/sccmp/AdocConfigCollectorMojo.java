package de.eitco.cicd.sccmp;

import de.eitco.cicd.sccmp.model.Property;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This mojo collects all spring-configuration-metadata.json files in the classpath and creates an asciidoc overview page
 * containing the available properties.
 */
@Mojo(name = "adoc-overview", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class AdocConfigCollectorMojo extends AbstractConfigCollectorMojo {

    @Parameter
    String outputDirectory;

    @Parameter(defaultValue = "3")
    int titleLevel = 3;

    @Override
    public void execute() throws MojoExecutionException {

        try {

            CollectedProperties collectedProperties = loadConfigurationMetadata();
            generateAdoc(collectedProperties);

        } catch (IOException e) {
            throw new MojoExecutionException("Failed to load configuration metadata from classpath.", e);
        }
    }

    private void generateAdoc(CollectedProperties collectedProperties) throws MojoExecutionException, IOException {

        StringBuilder adoc = new StringBuilder();
        String titlePrefix = "=".repeat(titleLevel);
        String groupPrefix = "=".repeat(titleLevel + 1);

        adoc.append(titlePrefix).append(" Configuration Properties\n\n");

        for (String groupName : collectedProperties.getGroupNames()) {

            adoc.append("[#_").append(groupName.replace('.', '_')).append("]\n");
            adoc.append(groupPrefix).append(" ").append(groupName).append("\n\n");

            adoc.append("[%autowidth]\n|===\n|Property |Type |Description |Default value\n\n");

            List<Property> properties = collectedProperties.getPropertiesByGroupName().get(groupName);

            for (Property property : properties) {

                adoc
                    .append("|").append(getPropertyName(property, groupName)).append("\n")
                    .append("|").append(property.getType()).append("\n")
                    .append("|").append(property.getDescription()).append("\n")
                    .append("|").append(property.getDefaultValue() != null ? property.getDefaultValue() : "").append("\n\n");
            }

            adoc.append("|===\n\n");
        }

        String out = getOutputPath(outputDirectory);

        Path outputPath = Path.of(out, "configuration-properties.adoc");
        getLog().info("Writing adoc file " + outputPath);

        Files.writeString(outputPath, adoc.toString());
    }
}
