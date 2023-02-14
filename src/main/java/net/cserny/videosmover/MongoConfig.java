package net.cserny.videosmover;

import org.eclipse.microprofile.config.inject.ConfigProperties;

import javax.enterprise.context.Dependent;

@Dependent
//@ConfigProperties(prefix = "mongo")
public class MongoConfig {

    private String url;
    private String db;
    private String collection;

    public String getUrl() {
        return url;
    }

    public String getDb() {
        return db;
    }

    public String getCollection() {
        return collection;
    }
}
