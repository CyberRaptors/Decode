package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lib8812.common.robot.hardwarewrappers.VirtualCRServo;
import lib8812.common.robot.hardwarewrappers.VirtualMotor;
import lib8812.common.robot.hardwarewrappers.VirtualServo;

/** @noinspection unchecked*/
public abstract class IRobot {
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
