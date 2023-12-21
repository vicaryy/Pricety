package com.vicary.zalandoscraper.service.chart;

import com.vicary.zalandoscraper.exception.ChartGeneratorException;
import com.vicary.zalandoscraper.model.Product;
import com.vicary.zalandoscraper.service.dto.ProductHistoryDTO;
import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.None;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProductChartGenerator {
    private List<Double> x = new ArrayList<>();
    private List<Double> y = new ArrayList<>();
//    private List<Double> xSoldOut = new ArrayList<>();
//    private List<Double> ySoldOut = new ArrayList<>();
    List<List<Double>> xS = new ArrayList<>();
    List<List<Double>> yS = new ArrayList<>();

    @Setter
    @Getter
    private String fileDestination;

    @Setter
    @Getter
    private Dimension dimension;


    public File asPng(Product p, List<ProductHistoryDTO> DTOs) {
        if (fileDestination.isBlank())
            throw new ChartGeneratorException("", "File destination cannot be empty when generating to Png");
        checkDTOsValidation(p, DTOs);


        XYChart chart = new XYChartBuilder()
                .title(p.getName() + " - " + p.getVariant() + " - " + p.getServiceName())
                .width(dimension.width())
                .height(dimension.height())
                .xAxisTitle("Time")
                .yAxisTitle("Price - " + p.getCurrency())
                .build();
        setAxis(DTOs);
        chart.addSeries("Price", x, y).setMarker(new None()).setLineWidth(4);
        for (int k = 0; k < xS.size(); k++) {
            chart.addSeries("Sold Out" + k, xS.get(k), yS.get(k)).setMarker(new None()).setLineWidth(4).setLineColor(Color.RED);
        }
        System.out.println(xS);
        System.out.println(yS);
        int i = 1;
        for (ProductHistoryDTO dto : DTOs) {
            System.out.println("        Product nr: " + i);
            System.out.println(dto);
            i++;
        }
//        chart.addSeries("Sold Out", xSoldOut, ySoldOut);
//        chart.addSeries("Sold Out", xSoldOut, ySoldOut).setMarker(new None()).setLineWidth(8);
        new SwingWrapper(chart).displayChart();
        try {
            Thread.sleep(20000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    void setAxis(List<ProductHistoryDTO> DTOs) {
        System.out.println(DTOs.get(21));
        DTOs.get(21).setPrice(9);
//        List<List<Double>> xS = new ArrayList<>();
//        List<List<Double>> yS = new ArrayList<>();


        for (int i = 0; i < DTOs.size(); i++) {
            var dto = DTOs.get(i);
            if (dto.getPrice() == 0 && i == 0) {
                for (int k = i + 1; k < DTOs.size(); k++) {
                    var dtoK = DTOs.get(k);
                    if (dtoK.getPrice() != 0) {
                        double nextPrice = dtoK.getPrice();
                        List<Double> xx = new ArrayList<>();
                        List<Double> yy = new ArrayList<>();
                        for (int l = i; l < k; l++) {

                            xx.add((double) l);
                            yy.add(nextPrice);
//                            xSoldOut.add((double) l);
//                            ySoldOut.add(nextPrice);
                            x.add((double) l);
                            y.add(nextPrice);
                        }
                        xx.add((double) k + 1);
                        yy.add(nextPrice);

                        xS.add(xx);
                        yS.add(yy);
                        i = k;
                        x.add((double) i);
                        y.add(nextPrice);
                        break;
                    }
                }
            } else if (dto.getPrice() == 0) {
                double nextPrice = DTOs.get(i - 1).getPrice();
                List<Double> xx = new ArrayList<>();
                List<Double> yy = new ArrayList<>();
                for (int k = i; k < DTOs.size(); k++) {
                    if (DTOs.get(k).getPrice() == 0) {
                        xx.add((double) k);
                        yy.add(nextPrice);
//                        xSoldOut.add((double) k);
//                        ySoldOut.add(nextPrice);
                        x.add((double) k);
                        y.add(nextPrice);
                    } else {
                        xx.add((double) k - 1);
                        yy.add(nextPrice);
                        xS.add(xx);
                        yS.add(yy);
                        x.add((double) k);
                        y.add(nextPrice);
                        i = k - 1;
                        break;
                    }
                }
            } else {
                x.add((double) i);
                y.add(dto.getPrice());
            }
        }
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

    public File asJpg() {
        if (fileDestination.isBlank())
            throw new ChartGeneratorException("", "File destination cannot be empty when generating to Png");
        return null;
    }

    public File asPngHighResolution() {
        if (fileDestination.isBlank())
            throw new ChartGeneratorException("", "File destination cannot be empty when generating to Png");
        return null;
    }

    public File asJpgHighResolution() {
        if (fileDestination.isBlank())
            throw new ChartGeneratorException("", "File destination cannot be empty when generating to Png");
        return null;
    }

    public void display() {

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

//    List<Double> getXSoldOut() {
//        return xSoldOut;
//    }
//
//    List<Double> getYSoldOut() {
//        return ySoldOut;
//    }

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

