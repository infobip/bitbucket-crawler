import org.junit.Assert
import org.junit.Test

class A_PrerequisitesTest {

    @Test
    public void containsParameters(){
        def script = System.getProperty("script");
        Assert.assertNotNull(script)
        Assert.assertNotEquals("", script)
        def credentials = System.getProperty("credentials");
        Assert.assertNotEquals("", credentials)
        Assert.assertTrue(credentials.contains(":"))
    }

    @Test
    public void validScript(){
        def property = System.getProperty("script");
        def clazz = getClass().getClassLoader().loadClass(property)
        Assert.assertTrue(AbstractCrawlerScript.class.isAssignableFrom(clazz))
    }
}
