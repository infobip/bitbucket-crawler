import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

@groovy.util.logging.Log4j() public abstract class AbstractCrawlerScript
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
public AbstractCrawlerScript
() {}
public static  java.lang.String exec(java.lang.String string, java.io.File dir, long timeout) { return (java.lang.String)null;}
public static  java.lang.String exec(java.lang.String string, java.io.File dir) { return (java.lang.String)null;}
public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
public  void setMetaClass(groovy.lang.MetaClass mc) { }
public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
public  java.lang.Object getProperty(java.lang.String property) { return null;}
public  void setProperty(java.lang.String property, java.lang.Object value) { }
public abstract  boolean process(java.io.File f);
public static  java.lang.String exec(java.lang.String string, java.io.File dir, long timeout, java.lang.String logCommand) { return (java.lang.String)null;}
}
