package com.intellisoftkenya.oe.plugin.analyzer;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XnlXpImporterTest {

    private XnlXpImporter analyzer;

    private final String headers = "Instrument Id,Sample No.,Reception Date,Time,Patient ID,WBC(10^3/uL),RBC(10^6/uL)\n";

    private final String xnResults = "XN-450^01101,,,11:39:15,,10.02,4.11\n";

    private final String xtResults = "XT-2000i^12520,,,11:39:15,,10.02,4.11\n";

    @Before
    public void setup() {
        analyzer = new XnlXpImporter();
    }

    @Test
    public void isTargetAnalyzer_shouldPassIfResultIsFromExpectedSource() {
        Assert.assertTrue(analyzer.isTargetAnalyzer(asList(headers, xnResults)));
    }

    @Test
    public void isTargetAnalyzer_shouldFailIfResultIsFromAnotherSource() {
        Assert.assertFalse(analyzer.isTargetAnalyzer(asList(headers, xtResults)));
    }

    @Test
    public void isTargetAnalyzer_shouldFailIfResultIsBlank() {
        Assert.assertFalse(analyzer.isTargetAnalyzer(emptyList()));
    }

    @Test
    public void isTargetAnalyzer_shouldFailIfResultOnlyHasHeaders() {
        Assert.assertFalse(analyzer.isTargetAnalyzer(singletonList(headers)));
    }
    
}