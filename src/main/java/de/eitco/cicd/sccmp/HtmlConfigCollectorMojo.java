package de.eitco.cicd.sccmp;

import j2html.TagCreator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This mojo collects all spring-configuration-metadata.json files in the classpath and creates a HTML overview page
 * containing the available properties.
 */
@Mojo(name = "html-overview", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class HtmlConfigCollectorMojo extends AbstractConfigCollectorMojo {

    @Parameter
    String outputDirectory;

    @Override
    public void execute() throws MojoExecutionException {

        try {

            CollectedProperties collectedProperties = loadConfigurationMetadata();
            generateHtml(collectedProperties);

        } catch (IOException e) {
            throw new MojoExecutionException("Failed to load configuration metadata from classpath.", e);
        }
    }

    private void generateHtml(CollectedProperties collectedProperties) throws MojoExecutionException, IOException {

        String html = TagCreator.html(
            TagCreator.head(
                TagCreator.title("Configuration Properties"),
                TagCreator.styleWithInlineFile("/style.css")
            ),
            TagCreator.body(

                TagCreator.h1("Configuration Properties"),

                TagCreator.each(collectedProperties.getGroupNames(), groupName -> TagCreator.div(

                    TagCreator.h2(groupName),

                    TagCreator.table(
                        TagCreator.tbody(
                            TagCreator.tr(
                                TagCreator.th("Name"),
                                TagCreator.th("Type"),
                                TagCreator.th("Description"),
                                TagCreator.th("Default value")
                            ),
                            TagCreator.each(collectedProperties.getPropertiesByGroupName().get(groupName), property -> TagCreator.tr(
                                TagCreator.td(getPropertyName(property, groupName)),
                                TagCreator.td(property.getType()),
                                TagCreator.td(property.getDescription()),
                                TagCreator.td(property.getDefaultValue() != null ? property.getDefaultValue().toString() : "")
                            ))
                        )
                    )
                ))
            )
        ).render();

        String out = getOutputPath(outputDirectory);

        Path outputPath = Path.of(out, "configuration-properties.html");
        getLog().info("Writing html file " + outputPath);

        Files.writeString(outputPath, html);
    }
}
