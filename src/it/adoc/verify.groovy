import java.nio.file.Files

File baseDirectory = new File("$basedir");

def outputfile = new File(baseDirectory, "target/adoc/configuration-properties.adoc")

assert outputfile.isFile()

String content = Files.readString(outputfile.toPath())

assert content.contains("|config-string-wit-default\n" +
        "|java.lang.String\n" +
        "|a sample configured string value that has a default value\n" +
        "|`+defaultValue!+`")


assert content.contains("|config-int\n" +
        "|java.lang.Integer\n" +
        "|a sample configured integer value\n" +
        "|`+0+`\n")

assert content.contains("|config-string\n" +
        "|java.lang.String\n" +
        "|a sample configured string value\n" +
        "|\n")

