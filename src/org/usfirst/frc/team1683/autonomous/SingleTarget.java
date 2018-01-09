package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;

public class SingleTarget extends Autonomous{
	private PathPoint[] points;
	private Path path;
	
	public SingleTarget(DriveTrain drive, Target target) {
		super(drive);
		if(target == Target.CLOSEST_SWITCH) {
			//TODO
		}
		else if (target == Target.SCALE) {
			//TODO
		}
		else if (target == Target.OPPONENET_SWITCH) {
			//TODO
		}
		else {
			//TODO
		}
		path = new Path(tankDrive, points, 0.3, 0.3);
	}	
	public void run() {
		if (!path.isDone()) {
			path.run();
		} else {
			tankDrive.stop();
		}
	}
}