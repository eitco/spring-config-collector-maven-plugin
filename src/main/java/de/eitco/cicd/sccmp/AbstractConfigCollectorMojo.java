package de.eitco.cicd.sccmp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.eitco.cicd.sccmp.model.ConfigurationMetadata;
import de.eitco.cicd.sccmp.model.Group;
import de.eitco.cicd.sccmp.model.Property;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractConfigCollectorMojo extends AbstractMojo {

    public static final String CONFIG_METADATA_JSON_FILE = "META-INF/spring-configuration-metadata.json";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = ".*")
    String filteringRegex;

    @Parameter(defaultValue = "${project.build.directory}")
    String outputDirectory;

    protected CollectedProperties loadConfigurationMetadata() throws IOException, MojoExecutionException {

        Enumeration<URL> systemResources = getProjectDependencyClassLoader().getResources(CONFIG_METADATA_JSON_FILE);
        List<URL> urls = new ArrayList<>();

        while (systemResources.hasMoreElements()) {

            URL url = systemResources.nextElement();
            urls.add(url);
        }

        getLog().info("Found " + urls.size() + " configuration metadata files in the classpath.");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<String> groupNames = new ArrayList<>();
        Map<String, List<Property>> propertiesByGroupName = new HashMap<>();

        Pattern pattern = Pattern.compile(filteringRegex);

        for (URL url : urls) {

            try (InputStream stream = url.openStream()) {

                getLog().info("Processing " + url);

                ConfigurationMetadata configurationMetadata = mapper.readValue(stream, ConfigurationMetadata.class);

                Objects.requireNonNullElse(configurationMetadata.getGroups(), List.<Group>of()).forEach(group -> {

                    boolean matches = pattern.matcher(group.getName()).matches();

                    if (group.getSourceMethod() == null && matches) {

                        groupNames.add(group.getName());
                        getLog().info("Group " + group.getName() + " added.");
                    } else if (!matches) {
                        getLog().info("Group " + group.getName() + " ignored.");
                    }
                });

                groupNames.forEach(name ->
                    configurationMetadata.getProperties().stream().filter(p -> p.getName().startsWith(name)).forEach(p -> {

                        List<Property> list = propertiesByGroupName.computeIfAbsent(name, n -> new ArrayList<>());
                        list.add(p);
                    }));
            }
        }

        List<String> sortedGroupNames = groupNames.stream().sorted().collect(Collectors.toList());

        Map<String, List<Property>> sortedPropertiesByGroupName = propertiesByGroupName.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream().sorted().collect(Collectors.toList())
                )
            );

        return new CollectedProperties(sortedGroupNames, sortedPropertiesByGroupName);
    }

    private ClassLoader getProjectDependencyClassLoader() throws MojoExecutionException {

        try {
            List<?> classpathElements = project.getRuntimeClasspathElements();

            URL[] urls = new URL[classpathElements.size()];

            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File((String) classpathElements.get(i)).toURI().toURL();
            }

            return new URLClassLoader(urls);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to initialize class loader.");
        }
    }

    protected String getOutputPath(String outputDirectory) throws MojoExecutionException {

        String out = outputDirectory != null ? outputDirectory : project.getBuild().getDirectory();
        boolean mkdirs = Path.of(out).toFile().mkdirs();

        if (!mkdirs && !Files.exists(Path.of(out))) {

            throw new MojoExecutionException("Failed to create output directory " + out);
        }

        return out;
    }

    protected String getPropertyName(Property property, String groupName) {

        return property.getName().substring(groupName.length() + 1);
    }
}
