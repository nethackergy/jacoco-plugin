package hudson.plugins.emma;

import hudson.util.IOException2;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kohsuke Kawaguchi
 */
public final class CoverageReport extends AggregatedReport<CoverageReport,PackageReport> {
    public CoverageReport(InputStream xmlReport) throws IOException {
        try {
            createDigester().parse(xmlReport);
        } catch (SAXException e) {
            throw new IOException2("Failed to parse XML",e);
        }
    }
    public CoverageReport(File xmlReport) throws IOException {
        try {
            createDigester().parse(xmlReport);
        } catch (SAXException e) {
            throw new IOException2("Failed to parse "+xmlReport,e);
        }
    }

    private Digester createDigester() {
        Digester digester = new Digester();
        digester.setClassLoader(getClass().getClassLoader());

        digester.push(this);

        digester.addObjectCreate( "*/package", PackageReport.class);
        digester.addSetNext(      "*/package","add");
        digester.addSetProperties("*/package");
        digester.addObjectCreate( "*/srcfile", SourceFileReport.class);
        digester.addSetNext(      "*/srcfile","add");
        digester.addSetProperties("*/srcfile");
        digester.addObjectCreate( "*/class", ClassReport.class);
        digester.addSetNext(      "*/class","add");
        digester.addSetProperties("*/class");
        digester.addObjectCreate( "*/method", MethodReport.class);
        digester.addSetNext(      "*/method","add");
        digester.addSetProperties("*/method");

        digester.addObjectCreate("*/coverage", CoverageElement.class);
        digester.addSetProperties("*/coverage");
        digester.addSetNext(      "*/coverage","addCoverage");

        //digester.addObjectCreate("*/testcase",TestCase.class);
        //digester.addSetNext("*/testsuite","add");
        //digester.addSetNext("*/test","add");
        //if(owner.considerTestAsTestObject())
        //    digester.addCallMethod("*/test", "setconsiderTestAsTestObject");
        //digester.addSetNext("*/testcase","add");
        //
        //// common properties applicable to more than one TestObjects.
        //digester.addBeanPropertySetter("*/id");
        //digester.addBeanPropertySetter("*/name");
        //digester.addBeanPropertySetter("*/description");
        //digester.addSetProperties("*/status","value","statusString");  // set attributes. in particular @revision
        //digester.addBeanPropertySetter("*/status","statusMessage");
        return digester;
    }
}