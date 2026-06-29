package com.badwallet.wallet.service.fees;

import com.badwallet.shared.utils.Constants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Frais de retrait : 1% du montant, plafonnés à 5000 CFA. */
@Component
public class WithdrawalFeeCalculator extends FeeCalculator {

    @Override
    protected BigDecimal calculateFeePercentage(BigDecimal amount) {
        return amount.multiply(Constants.WITHDRAW_FEE_RATE);
    }

    @Override
    protected BigDecimal getMaxFee() {
        return Constants.WITHDRAW_MAX_FEE;
    }
}
