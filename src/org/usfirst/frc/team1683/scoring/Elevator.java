package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMain;

	private final double LIFT_SPEED_MAX_DOWN = 0.5;
	private final double LIFT_SPEED_MAX_UP = 1;
//	private final double LIFT_RATIO = 0.9;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;
	private InputFilter filter;
	
	private LinearEasing easing;

	// We start raised
	private final double START_DISTANCE = 0; // from when we start 55 in
												// extended TODO: findd actual
												// value
	private static final double MAX_DIST = 20;
	private double distance = START_DISTANCE;

	private boolean override;

	public Elevator(TalonSRX motorMain, LimitSwitch limitTop, LimitSwitch limitBottom) {
		filter = new InputFilter(0.8, 0);
		elevatorMain = motorMain;
		elevatorMain.getEncoder().reset();
//		elevatorFollow = motorFollow;

		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
		
		easing = new LinearEasing(MAX_DIST / 5, MAX_DIST / 5, 1);
	}

	public boolean spinUp() {
		if (limitTop.isPressed()) {
			stop();
//			elevatorFollow.stop();
			return true;
		} else {
			spin(1);
			return false;
		}
	}

	public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.stop();
//			elevatorFollow.stop();
			return true;
		} else {
			spin(-0.5);
			return false;
		}
	}

	public boolean spinTo(double d) {
		double distLeft = d - getHeight();
		if (Math.abs(distLeft) <= 2) {
			stop();
			return true;
		} else {
			spin(distLeft > 0 ? 1 : -0.5);
			return false;
		}
	}

	public void overrideLimit(boolean override) {
		this.override = override;
	}

	public void spin(double speed) {
		double easingSpeed = easing.getSpeed(getHeight(), MAX_DIST);
		SmartDashboard.sendData("Easing speed", easingSpeed);
		if (limitBottom.isPressed())
			elevatorMain.getEncoder().reset();
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0))) {
			elevatorMain.stop();
		} else if (Math.abs(speed) < 0.09) {
			stop();
		} else {
			double rawSpeed = Math.min(easingSpeed, Math.abs(speed)) * (speed > 0 ? LIFT_SPEED_MAX_UP : -LIFT_SPEED_MAX_DOWN);
			elevatorMain.set(rawSpeed);
//			elevatorMain.set(LIFT_RATIO * rawSpeed);
//			SmartDashboard.sendData("Elevator speed", LIFT_RATIO * rawSpeed);
			SmartDashboard.sendData("Elevator speed", rawSpeed);
		}
	}

	public void stop() {
		// double error = initEncValue - getHeight();
		// double correction = kP * error;
		SmartDashboard.sendData("StopRunning", Math.random() + ", " + (!limitBottom.isPressed()));
		if (!limitBottom.isPressed()) {
			SmartDashboard.sendData("IsRunningHold", Math.random());
			elevatorMain.set(0.1);//filter.filterInput(0.1));
//			elevatorFollow.set(filter.filterInput(0.1));
		} else {
			SmartDashboard.sendData("IsRunningHold", false);
			elevatorMain.set(0);
//			elevatorFollow.set(0);
		}
	}

	/*
	 * public boolean spinDown() { if (limitBottom.isPressed()) {
	 * elevatorMain.getEncoder().reset(); elevatorMain.brake(); distance =
	 * MAX_DISTANCE; // we have hit the bottom so reset return true; } else {
	 * double distLeft = MAX_DISTANCE -
	 * Math.abs(elevatorMain.getEncoder().getDistance());
	 * spin(-getLiftSpeed(distLeft)); return false; } }
	 */

	public TalonSRX getMotor() {
		return elevatorMain;
	}

	protected double getHeight() {
		return distance + elevatorMain.getEncoder().getDistance();
	}

}
