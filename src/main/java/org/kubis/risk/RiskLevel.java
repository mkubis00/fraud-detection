package org.kubis.risk;

/***
 * Risk level is calculated from the value of risk points. More points means higher risk.
 */
public enum RiskLevel {
    VERY_LOW(20),
    LOW(40),
    MEDIUM(60),
    HIGH(99),
    VERY_HIGH(100);

    private final int score;

    RiskLevel(int score) {
        this.score = score;
    }

    /***
     * Risk point are summed up and then based of the result risk level is defined.
     */
    public static RiskLevel fromPoints(int points) {
        for (RiskLevel level : values()) {
            if (points <= level.score) {
                return level;
            }
        }
        return VERY_HIGH;
    }
}
