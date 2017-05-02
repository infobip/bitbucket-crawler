import groovy.util.logging.Log4j

@Log4j
class MadgadgetAnalysis extends AbstractCrawlerScript {

    boolean process(File dir) {
        if (!mvnProject(dir)) return true
        def out = exec("mvn dependency:tree", dir, 5*60*1000)
        if(buildFailing(out)){
            log.error "build failure ${dir}"
            return false
        }
        if(containsVulnerableJar(out)){
            log.error "still vulnerable ${dir}"
            return false
        }
        return true
    }

    static boolean buildFailing(String out) {
        out.contains("BUILD FAILURE") || out.contains("[FATAL]")
    }

    static boolean containsVulnerableJar(String line) {
        line.contains("commons-collections:jar:3.0:compile") ||
        line.contains("commons-collections:jar:3.1:compile") ||
        line.contains("commons-collections:jar:3.2:compile") ||
        line.contains("commons-collections:jar:3.2.1:compile")
    }

    static boolean mvnProject(File file) {
        file.listFiles().find { f -> "pom.xml".equals(f.name) } != null
    }
}



