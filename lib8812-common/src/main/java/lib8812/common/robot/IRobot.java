package lib8812.common.robot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import lib8812.common.robot.hardwarewrappers.VirtualCRServo;
import lib8812.common.robot.hardwarewrappers.VirtualMotor;
import lib8812.common.robot.hardwarewrappers.VirtualServo;

/** @noinspection unchecked*/
public abstract class IRobot {
    public class LockedUsageAction implements Action
    {
        Action inner;
        public Object[] resources;
        boolean started = false;

        public LockedUsageAction(Action inner, Object... resources) {
            this.resources = resources;
            this.inner = inner;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (started) {
                boolean runAgain =  inner.run(telemetryPacket);

                if (!runAgain) {
                    release(resources);

                    return false;
                }
            }

            if (!use(resources)) return false; // resources are locked, don't try again, just quit

            started = true;
            return run(telemetryPacket);
        }
    }

    public ArrayList<Object> lockedResources = new ArrayList<>();

    public void releaseAllDevices() {
        lockedResources.clear();
    }

    public void useAndRelease(Object resource, Runnable action) {
        if (!resourceAvailable(resource)) return;

        // since all the "locks" are single-threaded, we don't actually have to use/release the resource, we just need to confirm that it's available

        action.run();
    }

    public boolean resourceAvailable(Object resource) {
		return !lockedResources.contains(resource);
	}

    public boolean resourcesAvailable(Object... resources) {
        for (Object resource : resources)
        {
            if (lockedResources.contains(resource)) return false;
        }

        return true;
    }

    public boolean use(Object... resources) {
        for (Object resource : resources)
        {
            if (lockedResources.contains(resource)) return false;
        }

        Collections.addAll(lockedResources, resources);

        return true;
    }

    public void release(Object... resources) {
        for (Object resource : resources)
        {
            lockedResources.remove(resource);
        }
    }

    public void init(HardwareMap hardwareMap) {
        Class<? extends IRobot> cls = this.getClass();
        Field[] publicFields = cls.getFields();

        for (Field fld : publicFields) {
            Class<?> type = fld.getType();

            if (Modifier.isStatic(fld.getModifiers())) continue;

            if (!HardwareDevice.class.isAssignableFrom(type)) continue; // Field must be a hardware device


            try {
                fld.set(
                        this,
                        loadDevice(hardwareMap, type, fld.getName())
                );
            } catch (IllegalAccessException e) {
                throw new RuntimeException("unable to initialize hardware device", e);
            }
        }

        postInit(hardwareMap);
    }

    protected void postInit(HardwareMap hardwareMap) { }

    protected static <THardwareDevice> THardwareDevice loadDevice(HardwareMap hardwareMap, Class<THardwareDevice> cls, String name) {
        if (cls.equals(VirtualMotor.class)) return (THardwareDevice) new VirtualMotor();
        if (cls.equals(VirtualServo.class)) return (THardwareDevice) new VirtualServo();
        if (cls.equals(VirtualCRServo.class)) return (THardwareDevice) new VirtualCRServo();

        return hardwareMap.get(cls, name);
    }
}
