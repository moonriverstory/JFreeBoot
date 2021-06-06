package com.ice.chart;

import com.ice.chart.pojo.PriceHistoryBO;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component(value = "jfreeChartDemo")
@PropertySource(value = "classpath:application.properties", encoding = "utf-8")
public class JFreeChartDemo {

    @Value("${jfreechart.title}")
    private String title;

    public static void main(String[] args) {


        JFreeChartDemo demo = new JFreeChartDemo();
        demo.sout();

        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2022, 3, 31);

        Map<String, List<PriceHistoryBO>> historyMap = demo.initMap();

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (Map.Entry<String, List<PriceHistoryBO>> historyEntry : historyMap.entrySet()) {
            dataset.addSeries(demo.generateXYDataSet(historyEntry.getKey(), historyEntry.getValue(), startDate));
        }

        demo.createLineChart(dataset, "BPA价格变化", "财年", "价格（为0代表没有BPA）", "demo.png", "D:/com.ice.chart/");
    }

    public void sout() {
        System.out.println(title);
    }

    public Map<String, List<PriceHistoryBO>> initMap() {

        Map<String, List<PriceHistoryBO>> historyMap = new HashMap<>();

        List<PriceHistoryBO> fskHistoryList = new ArrayList<PriceHistoryBO>();
        PriceHistoryBO fskPriceHistory1 = new PriceHistoryBO();
        fskPriceHistory1.setPrice(new BigDecimal(366));
        fskPriceHistory1.setChangeDate(LocalDate.of(2021, 5, 1));
        fskHistoryList.add(fskPriceHistory1);
        PriceHistoryBO fskPriceHistory2 = new PriceHistoryBO();
        fskPriceHistory2.setPrice(new BigDecimal(386));
        fskPriceHistory2.setChangeDate(LocalDate.of(2021, 8, 1));
        fskHistoryList.add(fskPriceHistory2);
        PriceHistoryBO fskPriceHistory3 = new PriceHistoryBO();
        fskPriceHistory3.setPrice(new BigDecimal(336));
        fskPriceHistory3.setChangeDate(LocalDate.of(2022, 2, 10));
        fskHistoryList.add(fskPriceHistory3);
        historyMap.put("FSK", fskHistoryList);

        List<PriceHistoryBO> hzHistoryList = new ArrayList<PriceHistoryBO>();
        PriceHistoryBO hzPriceHistory1 = new PriceHistoryBO();
        hzPriceHistory1.setPrice(new BigDecimal(370));
        hzPriceHistory1.setChangeDate(LocalDate.of(2021, 4, 1));
        hzHistoryList.add(hzPriceHistory1);
        PriceHistoryBO hzPriceHistory2 = new PriceHistoryBO();
        hzPriceHistory2.setPrice(new BigDecimal(330));
        hzPriceHistory2.setChangeDate(LocalDate.of(2021, 6, 18));
        hzHistoryList.add(hzPriceHistory2);
        PriceHistoryBO hzPriceHistory3 = new PriceHistoryBO();
        hzPriceHistory3.setPrice(new BigDecimal(0));
        hzPriceHistory3.setChangeDate(LocalDate.of(2021, 9, 11));
        hzHistoryList.add(hzPriceHistory3);
        PriceHistoryBO hzPriceHistory4 = new PriceHistoryBO();
        hzPriceHistory4.setPrice(new BigDecimal(0));
        hzPriceHistory4.setChangeDate(LocalDate.of(2021, 11, 4));
        hzHistoryList.add(hzPriceHistory4);
        PriceHistoryBO hzPriceHistory5 = new PriceHistoryBO();
        hzPriceHistory5.setPrice(new BigDecimal(360));
        hzPriceHistory5.setChangeDate(LocalDate.of(2022, 2, 4));
        hzHistoryList.add(hzPriceHistory5);
        PriceHistoryBO hzPriceHistory6 = new PriceHistoryBO();
        hzPriceHistory6.setPrice(new BigDecimal(0));
        hzPriceHistory6.setChangeDate(LocalDate.of(2022, 3, 5));
        hzHistoryList.add(hzPriceHistory6);
        historyMap.put("HZ", hzHistoryList);

        List<PriceHistoryBO> xyHistoryList = new ArrayList<PriceHistoryBO>();
        PriceHistoryBO xyPriceHistory1 = new PriceHistoryBO();
        xyPriceHistory1.setPrice(new BigDecimal(330));
        xyPriceHistory1.setChangeDate(LocalDate.of(2021, 4, 1));
        xyHistoryList.add(xyPriceHistory1);
        PriceHistoryBO xyPriceHistory2 = new PriceHistoryBO();
        xyPriceHistory2.setPrice(new BigDecimal(330));
        xyPriceHistory2.setChangeDate(LocalDate.of(2022, 3, 31));
        xyHistoryList.add(xyPriceHistory2);
        historyMap.put("XY", xyHistoryList);
        return historyMap;
    }


    public TimeSeries generateXYDataSet(String supplier, List<PriceHistoryBO> historyList, LocalDate startDate) {
        TimeSeries series = new TimeSeries(supplier);
        LocalDate now = LocalDate.now();
        int daysOfYear = now.lengthOfYear();

        int historyIndex = 0;
        BigDecimal nextPrice = null;
        for (int i = 0; i < daysOfYear; i++) {
            LocalDate cursor = startDate.plusDays(i);
            //计算开头
            if (i == 0) {
                if (cursor.isEqual(startDate)) {
                    nextPrice = historyList.get(historyIndex).getPrice();
                    if (historyIndex < historyList.size() - 1) {
                        historyIndex++;
                    }
                } else {
                    nextPrice = new BigDecimal(0);
                }
            } else {
                if (cursor.isEqual(historyList.get(historyIndex).getChangeDate())) {
                    nextPrice = historyList.get(historyIndex).getPrice();
                    if (historyIndex < historyList.size() - 1) {
                        historyIndex++;
                    }
                }
            }
            series.add(new Day(cursor.getDayOfMonth(), cursor.getMonthValue(), cursor.getYear()), nextPrice);
        }
        return series;
    }

    public void createLineChart(XYDataset dataset, String title, String xLabel, String yLabel, String fileName, String path) {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        //应用主题样式
        ChartFactory.setChartTheme(standardChartTheme);


        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,  // title
                xLabel, // x-axis label
                yLabel, // y-axis label
                dataset);

        // XYPlot图表区域的设置对象,用来设置图表的一些显示属性
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setBackgroundPaint(Color.LIGHT_GRAY);        // 设置图表背景颜色
        xyPlot.setOrientation(PlotOrientation.VERTICAL);    // 图表横向显示还是纵向显示
        xyPlot.setDomainGridlinePaint(Color.BLUE);          // 设置横向网格线蓝色
        xyPlot.setDomainGridlinesVisible(true);             // 设置显示横向网格线
        xyPlot.setRangeGridlinePaint(Color.BLUE);           // 设置纵向网格线蓝色
        xyPlot.setRangeGridlinesVisible(true);              // 设置显示纵向网格线

        //创建路径
        com.ice.util.FileUtil.createDir(path);

        try {
            ChartUtilities.saveChartAsPNG(new File(path + fileName), chart, 1200, 500);
            log.info("create {} success!", fileName);
        } catch (IOException e) {
            log.error("create {} fail. Error: ", fileName, e);
        }

    }

}
