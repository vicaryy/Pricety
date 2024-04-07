package com.vicary.pricety.service.chart;

public class ChartGeneratorFactory {
    private static final String fileDestination = "/Users/vicary/desktop/scraper/chart";

    public static ProductChartGenerator getDefaultChartGenerator() {
        return ProductChartGenerator.builder()
                .fileDestination(fileDestination)
                .dimension(new ProductChartGenerator.Dimension(1200, 600))
                .build();
    }
}
