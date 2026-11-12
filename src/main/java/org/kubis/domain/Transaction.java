package org.kubis.domain;

import java.math.BigDecimal;

public record Transaction(
        String bin,
        BigDecimal amount,
        String userLocation
) {}
