package com.badwallet.shared.utils;

import java.math.BigDecimal;

public final class Constants {

    private Constants() {}

    public static final BigDecimal WITHDRAW_FEE_RATE = new BigDecimal("0.01");   // 1%
    public static final BigDecimal WITHDRAW_MAX_FEE = new BigDecimal("5000");    // CFA

    public static final BigDecimal TRANSFER_FEE_RATE = new BigDecimal("0.005");  // 0.5%
    public static final BigDecimal TRANSFER_MAX_FEE = new BigDecimal("1000");    // CFA

    public static final String DEFAULT_CURRENCY = "XOF";
}
