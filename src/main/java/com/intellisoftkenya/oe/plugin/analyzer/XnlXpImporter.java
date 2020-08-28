package com.intellisoftkenya.oe.plugin.analyzer;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.common.services.PluginAnalyzerService;
import org.openelisglobal.plugin.AnalyzerImporterPlugin;

public class XnlXpImporter implements AnalyzerImporterPlugin {

    private static final String XNL_XP_IMPORTER = "XNL/XP-Importer";

    @Override
    public boolean connect() {
        PluginAnalyzerService.getInstance().addAnalyzerDatabaseParts(XNL_XP_IMPORTER, "Plugin for Sysmex XN-L and XP analyzer", loadTestMapping());
        PluginAnalyzerService.getInstance().registerAnalyzer(this);
        return true;
    }

    /**
     * Load test name mappings from the test_mapping file.
     * @return a map of test names from analyzer to those in OpenELIS
     */
    private List<PluginAnalyzerService.TestMapping> loadTestMapping() {
        try {
            URI testMap = getClass().getResource("/test_maping").toURI();
            return Files.readAllLines(Paths.get(testMap)).stream()
                .map(l -> {
                    String[] map = l.split(" +");
                    return new PluginAnalyzerService.TestMapping(map[0], map[1]);
                }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("An error occured while reading the test_mapping file", e);
        }
    }

    @Override
    public boolean isTargetAnalyzer(List<String> lines) {
        if (lines.size() <= 1) {
            return false;
        }
        return isSupported(lines.get(1)); //skip the headers and pick the first result
    }

    /**
     * Checks if the results being imported is from any of the supported analyzers. Supported analyzers
     * are configured in the supported file.
     * @param result result picked from the file to be processed
     * @return true if the results are from a supported analyzer
     */
    private boolean isSupported(String result) {
        boolean supported = false;
        try {
            URL supportedFile = getClass().getResource("/supported");
            List<String> supportedInstruments = Files.readAllLines(Paths.get(supportedFile.toURI()));
            supported = supportedInstruments.stream().anyMatch(inst -> result.contains(inst));
        } catch (Exception e) {
            throw new RuntimeException("An error occured while reading the supported file.", e);
        }
        return supported;
    }

    @Override
    public AnalyzerLineInserter getAnalyzerLineInserter() {
        try {
            URI testMap = getClass().getResource("/test_maping").toURI();
            Map<String,String> unitsMap = new HashMap<>();
            Files.readAllLines(Paths.get(testMap)).forEach(l -> {
                String[] map = l.split(" +");
                unitsMap.put(map[0], map[2]);
            });
            return new XnlXpLineInserter(XNL_XP_IMPORTER, unitsMap);
        } catch (Exception e) {
            throw new RuntimeException("An error occured while reading the test_mapping file", e);
        }
    }

}
