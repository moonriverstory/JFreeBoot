package com.ice.chart.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PriceHistoryBO {
    private String supplier;
    private BigDecimal price;
    private LocalDate changeDate;
}
