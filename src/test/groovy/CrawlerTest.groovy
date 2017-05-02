import org.junit.Assert
import org.junit.runner.RunWith
import org.junit.Test
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
class CrawlerTest {

    static BitbucketCrawler crawler

    @Parameters( name = "{index}: invoking {1}" )
    public static Collection<Object> data() {
        def property = System.getProperty("script", "MadgadgetAnalysis");
        def clazz = CrawlerTest.class.getClassLoader().loadClass(property)
        if(!crawler) crawler = new BitbucketCrawler(System.getProperty("credentials"), clazz.newInstance())
        return crawler.getRepos().stream().map({r-> [r, r.name] as Object[]}).collect()
    }

    private Object repo;
    private String name;

    public CrawlerTest(Map repo, String name) {
        this.repo = repo;
        this.name = name;
    }

    @Test
    public void test() {
        Assert.assertTrue("Invocation negative for: "+name, crawler.invokeScript(repo));
    }
}
