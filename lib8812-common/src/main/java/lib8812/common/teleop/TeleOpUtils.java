package lib8812.common.teleop;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TeleOpUtils {
    public static final double DEFAULT_FINE_TUNE_THRESH = 0.2;
    public static final double DEFAULT_LINEAR_INPUT_SCALE = 2.5;
    public static final double DEFAULT_FINE_AND_FAST_CONTROL_THRESH = 0.7;

    final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static boolean isApproximatelyEqual(double val1, double val2, double tolerance) {
        return Math.abs(val1-val2) <= tolerance;
    }

    public static boolean isBetweenInclusive(double val, double low, double high)
    {
        return val >= low && val <= high;
    }

    public static double fineTuneInput(double input, double thresh)
    {
        if (isBetweenInclusive(input, 1-thresh, 1)) return 1;
        if (isBetweenInclusive(input, 0-thresh, 0+thresh)) return 0;
        if (isBetweenInclusive(input, -1, -1+thresh)) return -1;

        return input;
    }

    public static double quadraticallyScaleInput(double input, double linearScale) {
        return powerScaleInput(input, 2, linearScale);
    }

    public static double quadraticallyScaleInput(double input) {
        return quadraticallyScaleInput(input, DEFAULT_LINEAR_INPUT_SCALE);
    }

    public static double powerScaleInput(double input, double power, double linearScale) {
        double sign = Math.signum(input);

        double linearlyScaled = Math.abs(input*linearScale);

        double powerScaled = Math.pow(linearlyScaled, power);

        if (Math.signum(powerScaled) != sign) return -powerScaled;

        return powerScaled;
    }

    public static double powerScaleInput(double input, double power) {
        return powerScaleInput(input, power, DEFAULT_LINEAR_INPUT_SCALE);
    }

    public static double fineAndFastControl(double input, double thresh) {
        if (input > thresh) return input;

        // quadratically scale small inputs
        return Math.signum(input)*(input*input);
    }

    public static double fineAndFastControl(double input) {
        return fineAndFastControl(input, DEFAULT_FINE_AND_FAST_CONTROL_THRESH);
    }

    public static double fineTuneInput(double input) { return fineTuneInput(input, DEFAULT_FINE_TUNE_THRESH); }

    public static void setTimeout(Runnable runnable, long delay) {
        scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}
