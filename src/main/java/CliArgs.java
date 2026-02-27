public class CliArgs {

    public final String pattern;  // random | glider | pulsar | gospel-gun
    public final double density;  // 0.0 – 1.0
    public final int fps;         // 1 – 120
    public final String size;     // small | medium | large | fullscreen
    public final int cols;
    public final int rows;
    public final String colors;   // classic | matrix | fire | ice

    private CliArgs(String pattern, double density, int fps, String size, int cols, int rows, String colors) {
        this.pattern = pattern;
        this.density = density;
        this.fps = fps;
        this.size = size;
        this.cols = cols;
        this.rows = rows;
        this.colors = colors;
    }

    public static CliArgs parse(String[] args) {
        String pattern = "random";
        double density = 0.2;
        int fps = 30;
        String size = "fullscreen";
        int cols = -1;
        int rows = -1;
        String colors = "classic";

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp();
                System.exit(0);
            }
            if (i + 1 >= args.length) {
                System.err.println("Missing value for flag: " + arg);
                System.exit(1);
            }
            String value = args[++i];
            switch (arg) {
                case "--pattern": case "-p":
                    pattern = validatePattern(value); break;
                case "--density": case "-d":
                    density = validateDensity(value); break;
                case "--fps": case "-f":
                    fps = validateFps(value); break;
                case "--size": case "-s":
                    size = validateSize(value); break;
                case "--cols":
                    cols = parsePositiveInt(value, "--cols"); break;
                case "--rows":
                    rows = parsePositiveInt(value, "--rows"); break;
                case "--colors": case "-c":
                    colors = validateColors(value); break;
                default:
                    System.err.println("Unknown flag: " + arg);
                    printHelp();
                    System.exit(1);
            }
        }

        int[] preset = sizePreset(size);
        if (cols == -1) cols = preset[0];
        if (rows == -1) rows = preset[1];

        return new CliArgs(pattern, density, fps, size, cols, rows, colors);
    }

    private static String validatePattern(String v) {
        switch (v) {
            case "random": case "glider": case "pulsar": case "gospel-gun": return v;
            default:
                System.err.println("Invalid --pattern: " + v + ". Must be: random, glider, pulsar, gospel-gun");
                System.exit(1); return null;
        }
    }

    private static double validateDensity(String v) {
        try {
            double d = Double.parseDouble(v);
            if (d < 0.0 || d > 1.0) throw new NumberFormatException();
            return d;
        } catch (NumberFormatException e) {
            System.err.println("Invalid --density: " + v + ". Must be 0.0 – 1.0");
            System.exit(1); return 0;
        }
    }

    private static int validateFps(String v) {
        try {
            int f = Integer.parseInt(v);
            if (f < 1 || f > 120) throw new NumberFormatException();
            return f;
        } catch (NumberFormatException e) {
            System.err.println("Invalid --fps: " + v + ". Must be 1 – 120");
            System.exit(1); return 0;
        }
    }

    private static String validateSize(String v) {
        switch (v) {
            case "small": case "medium": case "large": case "fullscreen": return v;
            default:
                System.err.println("Invalid --size: " + v + ". Must be: small, medium, large, fullscreen");
                System.exit(1); return null;
        }
    }

    private static String validateColors(String v) {
        switch (v) {
            case "classic": case "matrix": case "fire": case "ice": return v;
            default:
                System.err.println("Invalid --colors: " + v + ". Must be: classic, matrix, fire, ice");
                System.exit(1); return null;
        }
    }

    private static int parsePositiveInt(String v, String flag) {
        try {
            int n = Integer.parseInt(v);
            if (n < 1) throw new NumberFormatException();
            return n;
        } catch (NumberFormatException e) {
            System.err.println("Invalid " + flag + ": " + v + ". Must be a positive integer");
            System.exit(1); return 0;
        }
    }

    private static int[] sizePreset(String size) {
        switch (size) {
            case "small":      return new int[]{96,  54};
            case "medium":     return new int[]{320, 180};
            case "large":      return new int[]{640, 360};
            default:           return new int[]{960, 540};
        }
    }

    public static void printHelp() {
        System.out.println("Conway's Game of Life");
        System.out.println();
        System.out.println("Usage: Main [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -p, --pattern   <random|glider|pulsar|gospel-gun>  Starting pattern (default: random)");
        System.out.println("  -d, --density   <0.0-1.0>                          Fraction of alive cells (default: 0.2)");
        System.out.println("  -f, --fps       <1-120>                            Frames per second (default: 30)");
        System.out.println("  -s, --size      <small|medium|large|fullscreen>    Grid size preset (default: fullscreen)");
        System.out.println("      --cols      <int>                              Raw column count (overrides --size)");
        System.out.println("      --rows      <int>                              Raw row count (overrides --size)");
        System.out.println("  -c, --colors    <classic|matrix|fire|ice>          Colour theme (default: classic)");
        System.out.println("  -h, --help                                         Show this help");
        System.out.println();
        System.out.println("Size presets (cols x rows):");
        System.out.println("  small       96 x 54    (~20px cells at 1920x1080)");
        System.out.println("  medium     320 x 180   (~6px cells at 1920x1080)");
        System.out.println("  large      640 x 360   (~3px cells at 1920x1080)");
        System.out.println("  fullscreen 960 x 540   (~2px cells at 1920x1080)");
        System.out.println();
        System.out.println("Colour themes:");
        System.out.println("  classic    White, light yellow, light red");
        System.out.println("  matrix     Bright green, medium green, dark green");
        System.out.println("  fire       Red, orange, yellow");
        System.out.println("  ice        Cyan, white, light blue");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  Main");
        System.out.println("  Main --pattern glider --size medium --fps 60");
        System.out.println("  Main -p random -d 0.5 -f 10 -c fire");
        System.out.println("  Main --pattern pulsar --size small -c matrix");
    }
}
