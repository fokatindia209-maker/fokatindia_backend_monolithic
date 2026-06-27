package com.fokatindia.dto.dashboard.subvendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubVendorMonthlyBookingStat {
    private String month;
    private Long count;
}
