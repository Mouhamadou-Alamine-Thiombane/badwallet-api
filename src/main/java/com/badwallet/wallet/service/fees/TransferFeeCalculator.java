package com.badwallet.wallet.service.fees;

import com.badwallet.shared.utils.Constants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Frais de transfert : 0.5% du montant, plafonnés à 1000 CFA. */
@Component
public class TransferFeeCalculator extends FeeCalculator {

    @Override
    protected BigDecimal calculateFeePercentage(BigDecimal amount) {
        return amount.multiply(Constants.TRANSFER_FEE_RATE);
    }

    @Override
    protected BigDecimal getMaxFee() {
        return Constants.TRANSFER_MAX_FEE;
    }
}
