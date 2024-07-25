
[![License](https://img.shields.io/github/license/eitco/spring-config-collector-maven-plugin.svg?style=for-the-badge)](https://opensource.org/license/mit)


[![Build status](https://img.shields.io/github/actions/workflow/status/eitco/spring-config-collector-maven-plugin/deploy.yaml?branch=main&style=for-the-badge&logo=github)](https://github.com/eitco/spring-config-collector-maven-plugin/actions/workflows/deploy.yaml)
[![Maven Central Version](https://img.shields.io/maven-central/v/de.eitco.cicd/spring-config-collector-maven-plugin?style=for-the-badge&logo=apachemaven)](https://central.sonatype.com/artifact/de.eitco.cicd/spring-config-collector-maven-plugin)

# spring config collector maven plugin

This maven plugin collects all spring configuration properties of the project and its dependencies and creates 
documentation for them, either in plain html or in asciidoc.

It does so by collection spring boots own configuration meta information files, that can be found inside
jars in `META-INF/spring-configuration-metadata.json`.

> 📘 Note that this only finds configuration properties defined by classes annotated with `@ConfigurationProperties`. 
> `@Value` configuration properties will not be found.

# usage 

First, add this plugin to your pom:

````xml
<plugin>
    <groupId>de.eitco.cicd</groupId>
    <artifactId>spring-config-collector-maven-plugin</artifactId>
    <version>4.0.1</version>
</plugin>
````

Decide whether you want to generate an adoc file or an html file, and add 
an execution of the corresponding goal (This example simply executes both goals): 

````xml
<plugin>
    <groupId>de.eitco.cicd</groupId>
    <artifactId>spring-config-collector-maven-plugin</artifactId>
    <version>4.0.1</version>
    <executions>
        <execution>
            <id>adoc</id>
            <goals>
                <goal>adoc-overview</goal>
                <goal>html-overview</goal>
            </goals>
        </execution>
    </executions>
</plugin>
````

This will write the generated files to the target directory. You can specify the directory to write to using the 
`outputDirectory` parameter: 

````xml
<plugin>
    <groupId>de.eitco.cicd</groupId>
    <artifactId>spring-config-collector-maven-plugin</artifactId>
    <version>4.0.1</version>
    <executions>
        <execution>
            <id>adoc</id>
            <goals>
                <goal>adoc-overview</goal>
                <goal>html-overview</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/config-documentation</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
````

When building the project this will create an html file and an adoc file containing information about every member of all configuration properties of your application. 

This does however include spring standard properties, this might or might not be desired. Should this not be desired 
you can filter property names using a regular expression:

````xml
<plugin>
    <groupId>de.eitco.cicd</groupId>
    <artifactId>spring-config-collector-maven-plugin</artifactId>
    <version>4.0.1</version>
    <executions>
        <execution>
            <id>adoc</id>
            <goals>
                <goal>adoc-overview</goal>
                <goal>html-overview</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}</outputDirectory>
                <filteringRegex>my\.property\.prefix\..*|other-prefix.*</filteringRegex>
            </configuration>
        </execution>
    </executions>
</plugin>
````
In this case only properties that begin with `my.property.prefix` or `other-prefix` will be listed in the resulting files.

For further examples see the [integration test directory](tree/main/src/it).