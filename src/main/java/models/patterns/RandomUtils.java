package models.patterns;

import java.util.Random;

public class RandomUtils {
  private static Random rng = new Random();
  public static int randomInt(int lower, int upper) {
    return lower + rng.nextInt(upper - lower);
  }

  public static double randomDouble() {
    return rng.nextDouble();
  }
}
