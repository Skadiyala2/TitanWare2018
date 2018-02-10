package org.usfirst.frc.team1683.autonomous;

import java.util.Arrays;

import org.usfirst.frc.team1683.autonomous.Autonomous.State;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.DriverStation;

public class SingleTarget extends Autonomous {
	private PathPoint[] points;
	private Path path;
	private Target target;
	private TargetChooser chooser;

	public SingleTarget(DriveTrain drive) {
		super(drive);
		presentState = State.INIT_CASE;
	}
	
	public void setChooser(TargetChooser chooser) {
		this.chooser = chooser;
	}
	
	public void init() {
		target = chooser.getCorrectTarget();
		SmartDashboard.sendData("target", target.toString());
		// default paths assume everything is on left, so multiply by -1 if
		// otherwise
		boolean isLeft = target.isStartMiddle()
				? DriverStation.getInstance().getGameSpecificMessage().charAt(target.getSwitchScale()) == 'L'
				: chooser.getPosition() == 'L';
		if (target == Target.MIDDLE_SWITCH) {
			// TODO
		} else if (target == Target.MIDDLE_SCALE) {
			// TODO
		} else if (target == Target.CLOSE_SWITCH) {
			points = DrivePathPoints.LeftSwitchLeft;
		} else if (target == Target.CLOSE_SCALE) {
			points = DrivePathPoints.LeftScaleLeft;
		} else if (target == Target.FAR_SWITCH) {
			points = DrivePathPoints.LeftSwitchRight;
		} else if (target == Target.FAR_SCALE) {
			points = DrivePathPoints.LeftScaleRight;
		}
		if (!isLeft) {
			for (int i = 0; i < points.length; ++i) {
				points[i] = points[i].flipX();
			}
		}
		path = new Path(tankDrive, points, 0.4, 0.3);
		SmartDashboard.sendData("path points", Arrays.toString(points));
	}

	public void run() {
		SmartDashboard.sendData("Auto state", presentState.toString());
		switch (presentState) {
		case INIT_CASE:
			init();
			nextState = State.RUN_PATH;
			break;
		case RUN_PATH:
			if (!path.isDone()) {
				path.run();
			} else {
				tankDrive.stop();
				nextState = State.LIFT_ELEVATOR;
			}
			break;
		case LIFT_ELEVATOR:
			break;
		default:
			break;
		}
		presentState = nextState;
	}
}
