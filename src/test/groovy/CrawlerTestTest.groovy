import groovy.json.JsonSlurper
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized.class)
class CrawlerTestTest {

    static BitbucketCrawler crawler

    @Parameters( name = "{index}: invoking {1}" )
    public static Collection<Object[]> data() {
        def property = System.getProperty("script", "MadgadgetAnalysis");
        def clazz = CrawlerTest.class.getClassLoader().loadClass(property)
        if(!crawler) crawler = new BitbucketCrawler(System.getProperty("credentials"), clazz.newInstance())

        def json = new JsonSlurper().parseText(CrawlerTest.class.getResource("dumper.json").text) as ArrayList
        def result = new LinkedList<>()
        json.forEach({ r->
            result.add([r[0], r[1]] as Object[])
        })
        return result
    }

    private Object repo;
    private String name;

    public CrawlerTestTest(Map repo, String name) {
        this.repo = repo;
        this.name = name;
    }

    @Test
    public void test() {
        Assert.assertTrue("Invocation negative for: "+name, crawler.invokeScript(repo));
    }
}
