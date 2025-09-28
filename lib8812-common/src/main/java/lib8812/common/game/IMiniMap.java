package lib8812.common.game;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class IMiniMap<TFieldComponentEnum> {
    protected TFieldComponentEnum[][] map;
    protected final LinearOpMode opMode;

    public IMiniMap(LinearOpMode opMode) {
        this.opMode = opMode;
    }
    abstract public void update(TFieldComponentEnum[][] newMap, int counter);
    abstract public void printToTelemetry();
}
