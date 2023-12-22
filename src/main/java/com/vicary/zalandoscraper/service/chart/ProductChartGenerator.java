package com.vicary.zalandoscraper.service.chart;

import com.vicary.zalandoscraper.exception.ChartGeneratorException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import com.vicary.zalandoscraper.utils.PrettyTime;
import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.None;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductChartGenerator {
    private XYChart chart;
    private List<Double> x;
    private List<Double> y;
    private List<List<Double>> xSoldOut;
    private List<List<Double>> ySoldOut;

    @Setter
    @Getter
    private String fileDestination;

    @Setter
    @Getter
    private Dimension dimension;


    public File asPng(Product p, List<ProductHistoryDTO> DTOs) {
        return getFile(p, DTOs, BitmapEncoder.BitmapFormat.PNG, 0);
    }


    public File asJpg(Product p, List<ProductHistoryDTO> DTOs) {
        return getFile(p, DTOs, BitmapEncoder.BitmapFormat.JPG, 0);
    }

    public File asPngHighResolution(Product p, List<ProductHistoryDTO> DTOs) {
        return getFile(p, DTOs, BitmapEncoder.BitmapFormat.PNG, 300);
    }

    public File asJpgHighResolution(Product p, List<ProductHistoryDTO> DTOs) {
            return getFile(p, DTOs, BitmapEncoder.BitmapFormat.JPG, 300);
    }

    public void display(Product p, List<ProductHistoryDTO> DTOs) {
        checkDTOsValidation(p, DTOs);
        setAxis(DTOs);
        setXYChart(p, DTOs.get(0).getUpdateTime());

        if (!fileDestination.endsWith("/"))
            fileDestination = fileDestination + "/";
        new SwingWrapper(chart).displayChart();
    }

    private File getFile(Product p, List<ProductHistoryDTO> DTOs, BitmapEncoder.BitmapFormat bitmapFormat, int dpi) {
        if (fileDestination == null || fileDestination.isBlank())
            throw new ChartGeneratorException("", "File destination cannot be empty when generating to Png");
        checkDTOsValidation(p, DTOs);
        setAxis(DTOs);
        setXYChart(p, DTOs.get(0).getUpdateTime());

        if (!fileDestination.endsWith("/"))
            fileDestination = fileDestination + "/";
        String fileName = fileDestination + p.getProductId() + "-" + System.currentTimeMillis();
        try {
            if (dpi == 0)
                BitmapEncoder.saveBitmap(chart, fileName, bitmapFormat);
            else
                BitmapEncoder.saveBitmapWithDPI(chart, fileName, bitmapFormat, dpi);
        } catch (IOException e) {
            throw new ChartGeneratorException("", e.getMessage());
        }
        return new File(fileName);
    }

    private void checkDTOsValidation(Product p, List<ProductHistoryDTO> DTOs) {
        if (DTOs.isEmpty()) {
            throw new ChartGeneratorException(
                    "This product don't have any price history.",
                    "User '%s' try to get product price history but product history is empty, productId '%s'".formatted(p.getUser().getUserId(), p.getProductId()));
        }
        if (DTOs.stream().allMatch(dto -> dto.getPrice() == 0)) {
            throw new ChartGeneratorException(
                    "This product don't have any price history, was always sold out.",
                    "User '%s' try to get product price history but product was always sold out, productId '%s'".formatted(p.getUser().getUserId(), p.getProductId()));
        }
    }

    void setAxis(List<ProductHistoryDTO> DTOs) {
        x = new ArrayList<>();
        y = new ArrayList<>();
        xSoldOut = new ArrayList<>();
        ySoldOut = new ArrayList<>();
        for (int i = 0; i < DTOs.size(); i++) {
            boolean isSoldOutAtBegin = false;
            boolean isSoldOut = false;
            var dto = DTOs.get(i);
            if (dto.getPrice() == 0 && i == 0) {
                isSoldOutAtBegin = true;
                dto.setPrice(getNearestPriceWhenZeroAtBegin(DTOs));
            } else if (dto.getPrice() == 0) {
                isSoldOut = true;
                dto.setPrice(DTOs.get(i - 1).getPrice());
            }

            if (isSoldOutAtBegin) {
                List<Double> xx = new ArrayList<>();
                List<Double> yy = new ArrayList<>();
                xx.add((double) i);
                xx.add((double) i + 1);
                yy.add(dto.getPrice());
                yy.add(dto.getPrice());
                xSoldOut.add(xx);
                ySoldOut.add(yy);
            }
            if (isSoldOut) {
                List<Double> xx = new ArrayList<>();
                List<Double> yy = new ArrayList<>();
                xx.add((double) i);
                xx.add((double) i - 1);
                yy.add(dto.getPrice());
                yy.add(dto.getPrice());
                xSoldOut.add(xx);
                ySoldOut.add(yy);
            }

            x.add((double) i);
            y.add(dto.getPrice());
        }
    }

    private double getNearestPriceWhenZeroAtBegin(List<ProductHistoryDTO> DTOs) {
        for (ProductHistoryDTO dto : DTOs)
            if (dto.getPrice() != 0)
                return dto.getPrice();
        return 0;
    }

    private void setXYChart(Product p, LocalDateTime firstUpdate) {
        chart = new XYChartBuilder()
                .title(p.getName() + " - " + p.getVariant() + " - " + p.getServiceName())
                .width(dimension.width())
                .height(dimension.height())
                .xAxisTitle("Time")
                .yAxisTitle("Price - " + p.getCurrency())
                .build();
        chart.addSeries("On Sale", x, y).setMarker(new None()).setLineWidth(4);
        for (int i = 0; i < xSoldOut.size(); i++) {
            if (i == 0)
                chart.addSeries("Sold Out", xSoldOut.get(i), ySoldOut.get(i)).setMarker(new None()).setLineWidth(4).setLineColor(Color.RED);
            chart.addSeries("Sold Out" + i, xSoldOut.get(i), ySoldOut.get(i)).setMarker(new None()).setLineWidth(4).setLineColor(Color.RED).setShowInLegend(false);
        }

        Map<Double, Object> map = Map.of(
                0.0, PrettyTime.getDDmmYYYY(firstUpdate),
                (double) x.size() - 1, "Today");
        chart.setXAxisLabelOverrideMap(map);
    }


    void setX(List<Double> x) {
        this.x = x;
    }

    void setY(List<Double> y) {
        this.y = y;
    }

//    void setXSoldOut(List<Double> xSoldOut) {
//        this.xSoldOut = xSoldOut;
//    }
//
//    void setYSoldOut(List<Double> ySoldOut) {
//        this.ySoldOut = ySoldOut;
//    }

    List<Double> getX() {
        return x;
    }

    List<Double> getY() {
        return y;
    }

    List<List<Double>> getXSoldOut() {
        return xSoldOut;
    }

    List<List<Double>> getYSoldOut() {
        return ySoldOut;
    }

    public record Dimension(int width, int height) {
    }

    public static ProductChartGeneratorBuilder builder() {
        return new ProductChartGeneratorBuilder();
    }

    public static class ProductChartGeneratorBuilder {
        private Dimension dimension;
        private String fileDestination;

        ProductChartGeneratorBuilder() {
        }

        public ProductChartGeneratorBuilder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public ProductChartGeneratorBuilder fileDestination(String fileDestination) {
            this.fileDestination = fileDestination;
            return this;
        }

        public ProductChartGenerator build() {
            if (this.dimension == null) {
                this.dimension = new Dimension(500, 500);
            }
            System.setProperty("java.awt.headless", "false");
            return new ProductChartGenerator(this.dimension, this.fileDestination);
        }

        public String toString() {
            return "ProductChartGenerator.ProductChartGeneratorBuilder(dimension=" + this.dimension + ", fileDestination=" + this.fileDestination + ")";
        }
    }

    private ProductChartGenerator(Dimension dimension, String fileDestination) {
        this.dimension = dimension;
        this.fileDestination = fileDestination;
    }
}

