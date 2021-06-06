import com.ice.Application;
import com.ice.chart.JFreeChartDemo;
import com.ice.chart.pojo.PriceHistoryBO;
import org.jfree.data.time.TimeSeriesCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@PropertySource(value = "classpath:application.properties", encoding = "utf-8")
public class TestChart {
    @Autowired
    JFreeChartDemo jfreeChartDemo;

    @Test
    public void chart(){

        Map<String, List<PriceHistoryBO>> historyMap = jfreeChartDemo.initMap();
        jfreeChartDemo.sout();
        LocalDate startDate = LocalDate.of(2021, 4, 1);
        LocalDate endDate = LocalDate.of(2022, 3, 31);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (Map.Entry<String, List<PriceHistoryBO>> historyEntry : historyMap.entrySet()) {
            dataset.addSeries(jfreeChartDemo.generateXYDataSet(historyEntry.getKey(), historyEntry.getValue(), startDate));
        }
        jfreeChartDemo.createLineChart(dataset, "BPA价格变化", "财年", "价格（为0代表没有BPA）", "demo.png", "D:/com.ice.chart/");
    }
}
