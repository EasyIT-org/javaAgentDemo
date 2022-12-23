package org.easyit.demo.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is copy from sofa-common-tools
 */
public final class Profiler {


    private static final ThreadLocal<Profiler.Entry> entryStack = new ThreadLocal<>();

    public Profiler() {
    }

    public static void start() {
        start((String) null);
    }

    public static void start(String message) {
        entryStack.set(new Profiler.Entry(message, null, null));
    }

    public static void start(Profiler.Message message) {
        entryStack.set(new Profiler.Entry(message, null, null));
    }

    public static void reset() {
        entryStack.set(null);
    }

    public static void enter(String message) {
        Profiler.Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        }

    }

    public static void enter(Profiler.Message message) {
        Profiler.Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        }

    }

    public static void release() {
        Profiler.Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.release();
        }

    }

    public static long getDuration() {
        Profiler.Entry entry = entryStack.get();
        return entry != null ? entry.getDuration() : -1L;
    }

    public static String dump() {
        return dump("", "");
    }

    public static String dump(String prefix) {
        return dump(prefix, prefix);
    }

    public static String dump(String prefix1, String prefix2) {
        Profiler.Entry entry = entryStack.get();
        return entry != null ? entry.toString(prefix1, prefix2) : "";
    }

    public static Profiler.Entry getEntry() {
        return entryStack.get();
    }

    public static Profiler.Entry getCurrentEntry() {
        Profiler.Entry subEntry = entryStack.get();
        Profiler.Entry entry = null;
        if (subEntry != null) {
            do {
                entry = subEntry;
                subEntry = subEntry.getUnreleasedEntry();
            } while (subEntry != null);
        }

        return entry;
    }

    public enum MessageLevel {
        NO_MESSAGE,
        BRIEF_MESSAGE,
        DETAILED_MESSAGE;

    }

    public interface Message {
        Profiler.MessageLevel getMessageLevel(Profiler.Entry var1);

        String getBriefMessage();

        String getDetailedMessage();
    }

    public static final class Entry {
        private final List<Entry> subEntries;
        private final Object message;
        private final Profiler.Entry parentEntry;
        private final Profiler.Entry firstEntry;
        private final long baseTime;
        private final long startTime;
        private long endTime;

        private Entry(Object message, Profiler.Entry parentEntry, Profiler.Entry firstEntry) {
            this.subEntries = new ArrayList(4);
            this.message = message;
            this.startTime = System.currentTimeMillis();
            this.parentEntry = parentEntry;
            this.firstEntry = firstEntry != null ? firstEntry : this;
            this.baseTime = firstEntry == null ? 0L : firstEntry.startTime;
        }

        public String getMessage() {
            String messageString = null;
            if (this.message instanceof String) {
                messageString = (String) this.message;
            } else if (this.message instanceof Profiler.Message) {
                Profiler.Message messageObject = (Profiler.Message) this.message;
                Profiler.MessageLevel level = Profiler.MessageLevel.BRIEF_MESSAGE;
                if (this.isReleased()) {
                    level = messageObject.getMessageLevel(this);
                }

                if (level == Profiler.MessageLevel.DETAILED_MESSAGE) {
                    messageString = messageObject.getDetailedMessage();
                } else {
                    messageString = messageObject.getBriefMessage();
                }
            }

            return messageString;
        }

        public long getStartTime() {
            return this.baseTime > 0L ? this.startTime - this.baseTime : 0L;
        }

        public long getEndTime() {
            return this.endTime < this.baseTime ? -1L : this.endTime - this.baseTime;
        }

        public long getDuration() {
            return this.endTime < this.startTime ? -1L : this.endTime - this.startTime;
        }

        public long getDurationOfSelf() {
            long duration = this.getDuration();
            if (duration < 0L) {
                return -1L;
            } else if (this.subEntries.isEmpty()) {
                return duration;
            } else {
                for (int i = 0; i < this.subEntries.size(); ++i) {
                    Profiler.Entry subEntry = this.subEntries.get(i);
                    duration -= subEntry.getDuration();
                }

                return duration < 0L ? -1L : duration;
            }
        }

        public double getPecentage() {
            double parentDuration = 0.0D;
            double duration = (double) this.getDuration();
            if (this.parentEntry != null && this.parentEntry.isReleased()) {
                parentDuration = (double) this.parentEntry.getDuration();
            }

            return duration > 0.0D && parentDuration > 0.0D ? duration / parentDuration : 0.0D;
        }

        public double getPecentageOfAll() {
            double firstDuration = 0.0D;
            double duration = (double) this.getDuration();
            if (this.firstEntry != null && this.firstEntry.isReleased()) {
                firstDuration = (double) this.firstEntry.getDuration();
            }

            return duration > 0.0D && firstDuration > 0.0D ? duration / firstDuration : 0.0D;
        }

        public List getSubEntries() {
            return Collections.unmodifiableList(this.subEntries);
        }

        private void release() {
            this.endTime = System.currentTimeMillis();
        }

        private boolean isReleased() {
            return this.endTime > 0L;
        }

        private void enterSubEntry(Object message) {
            Profiler.Entry subEntry = new Profiler.Entry(message, this, this.firstEntry);
            this.subEntries.add(subEntry);
        }

        private Profiler.Entry getUnreleasedEntry() {
            Profiler.Entry subEntry = null;
            if (!this.subEntries.isEmpty()) {
                subEntry = this.subEntries.get(this.subEntries.size() - 1);
                if (subEntry.isReleased()) {
                    subEntry = null;
                }
            }

            return subEntry;
        }

        public String toString() {
            return this.toString("", "");
        }

        private String toString(String prefix1, String prefix2) {
            StringBuffer buffer = new StringBuffer();
            this.toString(buffer, prefix1, prefix2);
            return buffer.toString();
        }

        private void toString(StringBuffer buffer, String prefix1, String prefix2) {
            buffer.append(prefix1);
            String message = this.getMessage();
            long startTime = this.getStartTime();
            long duration = this.getDuration();
            long durationOfSelf = this.getDurationOfSelf();
            double percent = this.getPecentage();
            double percentOfAll = this.getPecentageOfAll();
            Object[] params = new Object[]{message, new Long(startTime), new Long(duration),
                    new Long(durationOfSelf), new Double(percent), new Double(percentOfAll)};
            StringBuffer pattern = new StringBuffer("{1,number} ");
            if (this.isReleased()) {
                pattern.append("[{2,number}ms");
                if (durationOfSelf > 0L && durationOfSelf != duration) {
                    pattern.append(" ({3,number}ms)");
                }

                if (percent > 0.0D) {
                    pattern.append(", {4,number,##%}");
                }

                if (percentOfAll > 0.0D) {
                    pattern.append(", {5,number,##%}");
                }

                pattern.append("]");
            } else {
                pattern.append("[UNRELEASED]");
            }

            if (message != null) {
                pattern.append(" - {0}");
            }

            buffer.append(MessageFormat.format(pattern.toString(), params));

            for (int i = 0; i < this.subEntries.size(); ++i) {
                Profiler.Entry subEntry = this.subEntries.get(i);
                buffer.append('\n');
                if (i == this.subEntries.size() - 1) {
                    subEntry.toString(buffer, prefix2 + "`---", prefix2 + "    ");
                } else if (i == 0) {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   ");
                } else {
                    subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   ");
                }
            }

        }
    }
}

