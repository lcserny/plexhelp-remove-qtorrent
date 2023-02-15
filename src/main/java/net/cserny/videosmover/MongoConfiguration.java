package net.cserny.videosmover;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "mongo")
public interface MongoConfiguration {

    String url();

    String db();

    String collection();
}
