package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Turn extends Command {

	double GyroAngle;
	double GyroCoefficient = 1.0; 
	int DesiredAngle;
	String DesiredDirection;
    
	public Turn(String Direction, int Angle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	DesiredDirection = Direction;
    	DesiredAngle = Angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMap.imu.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	GyroAngle = Math.abs(GyroCoefficient * RobotMap.imu.getGyroAngleX());
		SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
		if (DesiredDirection == "left") {
			Robot.driveTrain.Rotate(-RobotMap.turnSpeed);
		}
		else {
			Robot.driveTrain.Rotate(RobotMap.turnSpeed);
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (GyroAngle >= DesiredAngle) {
    		return true;
    	} else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	RobotMap.imu.reset();
    	SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveTrain.stop();
    	RobotMap.imu.reset();
    	SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
    }
}