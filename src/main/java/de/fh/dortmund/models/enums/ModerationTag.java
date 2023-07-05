package de.fh.dortmund.models.enums;

public enum ModerationTag {
    OFFENSIVE(0),
    SPAM(1),
    DUPLICATE(2),
    ADVERTISEMENT(3);

    private final int value;

    private ModerationTag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ModerationTag fromValue(int value) {
        for (ModerationTag sorte : ModerationTag.values()) {
            if (sorte.value == value) {
                return sorte;
            }
        }
        throw new IllegalArgumentException("Unknown ModerationTag: " + value);
    }

}
