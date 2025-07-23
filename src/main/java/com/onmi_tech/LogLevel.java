package com.onmi_tech;

/**
 * Log level enumeration for defining logging severity.
 */
public enum LogLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the numeric level of the log severity.
     * @return the level value
     */
    public int getLevel() {
        return level;
    }
}
