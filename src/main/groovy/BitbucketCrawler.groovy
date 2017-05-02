import groovy.util.logging.Log4j
import groovyx.net.http.HTTPBuilder
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.Level
import org.apache.log4j.PatternLayout

@Log4j
class BitbucketCrawler {

    def http = new HTTPBuilder('https://git.ib-ci.com')
    def credentials
    def workDirectory = File.createTempDir()
    def projects
    AbstractCrawlerScript closure
    LinkedHashSet repos = new LinkedHashSet<>()

    public BitbucketCrawler(def credentials, AbstractCrawlerScript closure, String... projects) {
        this.closure = closure
        this.credentials = credentials
        this.projects = new HashSet<String>(Arrays.asList(projects))
        http.setHeaders(['Authorization': "Basic ${credentials.bytes.encodeBase64().toString()}", 'Content-Type': 'application/json'])
        log.level = Level.INFO
        log.addAppender(new ConsoleAppender(new PatternLayout()))
    }

    Set getRepos() {
        if(!repos.empty) return repos
        crawl()
        repos
    }

    def crawl() {
        def path = '/rest/api/1.0/projects'
        def queryParams = ['limit': 1, 'start': 0]
        scroll(queryParams, path, { o ->
            crawlProject(o.values[0].key)
        })
    }

    def sendRequest(path, queryParams) {
        http.get(path: path, query: queryParams)
    }

    def crawlProject(String key) {
        if (!projects.isEmpty() && !projects.contains(key)) return
        log.info "getting repos from ${key} project"
        def path = "/rest/api/1.0/projects/${key}/repos"
        def queryParams = ['limit': 1, 'start': 0]
        scroll(queryParams, path, { o -> this.@repos.add(o.values[0]) })
    }

    void scroll(queryParams, path, closure) {
        while (true) {
            def object = sendRequest(path, queryParams)
            if (object.size == 0) {
                return
            }
            closure(object)
            if (object.isLastPage) {
                return
            }
            queryParams.put('start', object.nextPageStart)
        }
    }

    public boolean invokeScript(repo) {
        def url = resolveHttpUrl(repo);
        exec("git clone ${url}", workDirectory, "cloning $repo.name")
        def folder = new File(workDirectory, repo.name)
        if(!folder.exists()) {
            throw new RuntimeException("folder not cloned ${repo.name}")
        }
        exec("pwd", folder)
        exec("ls -la")

        boolean result = closure.process(folder)

        exec("rm -rf ${repo.slug}")
        result
    }

    private void exec(String string, File dir = workDirectory, String logCommand = "running $string") {
        AbstractCrawlerScript.exec(string, dir, 2*60*1000, logCommand)
    }

    def resolveSshUrl(object) {
        def urls = object.links.clone
        for (def url : urls) {
            if (url.name.equals('ssh')) return url.href
        }
        log.error "no ssh url for ${object.values[0].slug}"
    }

    def resolveHttpUrl(object) {
        def urls = object.links.clone
        for (def url : urls) {
            if (url.name.equals('http')) {
                def split = url.href.split('@')
                return "https://${credentials}@${split[1]}"
            }
        }
        log.error "no http url for ${object.values[0].slug}"
    }
}
