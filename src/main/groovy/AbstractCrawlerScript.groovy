import groovy.util.logging.Log4j
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.Level
import org.apache.log4j.PatternLayout

@Log4j
abstract class AbstractCrawlerScript {

    AbstractCrawlerScript() {
        log.level = Level.INFO
        log.addAppender(new ConsoleAppender(new PatternLayout()))
    }

    abstract boolean process(File f)

    static String exec(String string, File dir, long timeout = 2*60*1000, String logCommand = "running $string") {
        log.info logCommand
        def sout = new StringBuilder(), serr = new StringBuilder()
        def executeCmd = string.execute(null, dir)
        executeCmd.consumeProcessOutput(sout, serr)
        executeCmd.waitForOrKill(timeout)
        if (serr.size() > 0) log.error(serr)
        if (sout.size() > 0) log.info(sout)
        serr.size() > 0 ? serr.toString() : sout.toString()
    }
}
