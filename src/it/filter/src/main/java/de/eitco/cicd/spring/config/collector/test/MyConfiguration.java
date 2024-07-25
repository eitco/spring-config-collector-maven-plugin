package de.eitco.cicd.spring.config.collector.test;



import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cicd.test.properties")
public class MyConfiguration {

    /**
     * a sample configured integer value
     */
    private int configInt;

    /**
     * a sample configured string value
     */
    private String configString;

    /**
     * a sample configured string value that has a default value
     */
    private String configStringWitDefault = "defaultValue!";

    public int getConfigInt() {
        return configInt;
    }

    public MyConfiguration setConfigInt(int configInt) {
        this.configInt = configInt;
        return this;
    }

    public String getConfigString() {
        return configString;
    }

    public MyConfiguration setConfigString(String configString) {
        this.configString = configString;
        return this;
    }

    public String getConfigStringWitDefault() {
        return configStringWitDefault;
    }

    public MyConfiguration setConfigStringWitDefault(String configStringWitDefault) {
        this.configStringWitDefault = configStringWitDefault;
        return this;
    }
}
