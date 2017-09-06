package com.esplugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Collections;
import java.util.Map;

public class QjAnalyzerPlugin extends Plugin implements AnalysisPlugin {
    final static Logger logger = LogManager.getLogger(QjAnalyzerPlugin.class);
    static final String FILTER_NAME = "qj-analyzer-filter";

    public QjAnalyzerPlugin() {
        super();
        logger.info("Create The Plugin");
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap(FILTER_NAME, QjAnalyzerTokenFilterFactory::new);
    }

}


