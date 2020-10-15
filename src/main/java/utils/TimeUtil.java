package utils;

public class TimeUtil {
  private long timeInMs;
  public void tick() {
    timeInMs = System.currentTimeMillis();
  }

  public long getElapsedTime() {
    return System.currentTimeMillis() - timeInMs;
  }

  public void logTime(String s, boolean debugEnabled) {
    if (debugEnabled) {
      System.out.println(s + getElapsedTime() + " ms");
    }
  }
}
