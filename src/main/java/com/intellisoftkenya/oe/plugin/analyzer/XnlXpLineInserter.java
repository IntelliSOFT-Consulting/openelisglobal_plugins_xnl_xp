package com.intellisoftkenya.oe.plugin.analyzer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
import org.openelisglobal.analyzerimport.util.AnalyzerTestNameCache;
import org.openelisglobal.analyzerimport.util.MappedTestName;
import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;
import org.openelisglobal.common.util.DateUtil;

public class XnlXpLineInserter extends AnalyzerLineInserter {

    //ASSUMPTION: Time uses a 24-hour representation
    private static final String DATE_PATTERN = "dd MMMMM yyyy H:mm:ss";
    private final String DELIMITER = ",";
    private final String CONTROL_ACCESSION_PREFIX = "QC";
    private final int TEST_START_INDEX = 4; //test in the CSV appear from the 5th column onwards.

    private final int SAMPLE_NUMBER = 2;
    private final int RECEPTION_DATE = 3;

    private final String analyzerName;
    private final Map<String, String> unitsMap;

    /**
     * Creates an xnl-xp line inserter.
     * @param importerName name of the registered importer
     * @param unitsMap a map of test name to units
     */
    public XnlXpLineInserter(String analyzerName, Map<String, String> unitsMap) {
        this.analyzerName = analyzerName;
        this.unitsMap = unitsMap;
    }

    @Override
    public boolean insert(List<String> lines, String currentUserId) {
        Map<Integer, String> indexToTest = new HashMap<>();
        String[] headers = lines.get(0).split(DELIMITER);
        for (int i = TEST_START_INDEX; i < headers.length; ++i) {
            indexToTest.put(i, headers[i]);
        }

        AnalyzerReaderUtil readerUtil = new AnalyzerReaderUtil();
        List<AnalyzerResults> parsedResults = new ArrayList<>();
        for (int r = 1; r < lines.size(); ++r) {
            String[] fields = lines.get(r).split(DELIMITER);
            String sampleNumber = fields[SAMPLE_NUMBER];
            Timestamp receptionDate = DateUtil.convertStringDateToTimestampWithPatternNoLocale(fields[RECEPTION_DATE], DATE_PATTERN);
            for (int i = TEST_START_INDEX; i < fields.length; ++i) {
                if (indexToTest.containsKey(i)) {
                    String testName = indexToTest.get(i);
                    MappedTestName mappedTestName = AnalyzerTestNameCache.getInstance().getMappedTest(analyzerName, testName);
                    if (mappedTestName == null) {
                        mappedTestName = AnalyzerTestNameCache.getInstance().getEmptyMappedTestName(analyzerName, testName);
                    }
                    AnalyzerResults testResult = new AnalyzerResults();
                    testResult.setAnalyzerId(mappedTestName.getAnalyzerId());
                    testResult.setAccessionNumber(sampleNumber);
                    if (sampleNumber != null && sampleNumber.startsWith(CONTROL_ACCESSION_PREFIX)) {
                        testResult.setIsControl(true);
                    }
                    testResult.setResult(fields[i]);
                    testResult.setUnits(unitsMap.get(testName));
                    testResult.setCompleteDate(receptionDate);

                    parsedResults.add(testResult);

                    AnalyzerResults resultFromDB = readerUtil.createAnalyzerResultFromDB(testResult);
                    if( resultFromDB != null){
                        parsedResults.add(resultFromDB);
                    }
                }
            }
        }

        return persistImport(currentUserId, parsedResults);
    }

    @Override
    public String getError() {
        return "XN-L/XP importer unable to write to database";
    }
}