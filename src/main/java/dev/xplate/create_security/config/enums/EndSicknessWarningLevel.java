package dev.xplate.create_security.config.enums;

public enum EndSicknessWarningLevel {
    NORMAL(3),
    EVERY_OTHER(2),
    LAST(1),
    NONE(0);

    private final int level;

    EndSicknessWarningLevel(int level) {
        this.level = level;
    }

    public boolean isLevelAtLeast(EndSicknessWarningLevel check) {
        return level <= check.level;
    }
}
