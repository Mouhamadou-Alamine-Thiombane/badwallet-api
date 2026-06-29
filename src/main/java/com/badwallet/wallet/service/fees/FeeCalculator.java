package com.badwallet.wallet.service.fees;

import java.math.BigDecimal;

/**
 * TEMPLATE METHOD PATTERN : l'algorithme global de calcul des frais
 * (pourcentage plafonné) est figé ici ; seules les sous-classes
 * définissent le taux et le plafond applicables.
 */
public abstract class FeeCalculator {

    public final BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = calculateFeePercentage(amount);
        BigDecimal maxFee = getMaxFee();
        return fee.min(maxFee);
    }

    protected abstract BigDecimal calculateFeePercentage(BigDecimal amount);

    protected abstract BigDecimal getMaxFee();
}
