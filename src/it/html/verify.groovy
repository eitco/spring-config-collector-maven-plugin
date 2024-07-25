import java.nio.file.Files

File baseDirectory = new File("$basedir");

def outputfile = new File(baseDirectory, "target/configuration-properties.html")

assert outputfile.isFile()

String content = Files.readString(outputfile.toPath())

assert content.contains("<td>config-int</td><td>java.lang.Integer</td><td>a sample configured integer value</td><td>0</td>")
assert content.contains("<td>config-string</td><td>java.lang.String</td><td>a sample configured string value</td><td></td>")
assert content.contains("<td>config-string-wit-default</td><td>java.lang.String</td><td>a sample configured string value that has a default value</td><td>defaultValue!</td>")
assert content.contains("<td>pattern.correlation</td><td>java.lang.String</td><td>Appender pattern for log correlation. Supported only with the default Logback setup.</td><td></td>")

