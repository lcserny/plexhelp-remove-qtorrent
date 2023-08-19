package net.cserny;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;
import net.cserny.videosmover.ApplicationConfiguration;
import net.cserny.videosmover.DownloadHistoryService;
import net.cserny.videosmover.TorrentFile;
import net.cserny.videosmover.TorrentService;

@Slf4j
public class RemoveQTorrentCommand implements ApplicationRunner {

    @Autowired
    ApplicationContext context;

    @Autowired
    TorrentService torrentService;

    @Autowired
    DownloadHistoryService videosMoverDatabaseService;

    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[] { RemoveQTorrentCommand.class, ApplicationConfiguration.class }, args);
    }

    @Override
    public void run(ApplicationArguments arguments) {
        List<String> args = arguments.getNonOptionArgs();

        if (args.isEmpty()) {
            log.error("Please provide hash of torrent as first argument");
            SpringApplication.exit(context);
        } else {
            String hash = args.get(0);

            String sid = torrentService.generateSid();
            List<TorrentFile> files = torrentService.listFiles(sid, hash);
            videosMoverDatabaseService.updateDownloadsHistory(files);
            torrentService.delete(sid, hash, false);
        }
    }
}
