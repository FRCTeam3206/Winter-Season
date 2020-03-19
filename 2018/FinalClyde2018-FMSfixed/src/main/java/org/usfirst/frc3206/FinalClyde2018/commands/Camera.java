package org.usfirst.frc3206.FinalClyde2018.commands;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Camera extends Command {
CameraServer Camera1;
CameraServer Camera2;
	public Camera() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		//requires(Robot.CameraSub);
		
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	new Thread(() -> {
            
    		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture();
            camera1.setResolution(160, 120);
            camera1.setFPS(75);
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Camera1", 160, 120);
                   
            Mat source = new Mat();
            Mat output = new Mat();
          
        while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();
    	
    	
    	
		new Thread(() -> {
		            
		    		UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture();
		            camera2.setResolution(160, 120);
		            camera2.setFPS(75);
		            CvSink cvSink = CameraServer.getInstance().getVideo();
		            CvSource outputStream = CameraServer.getInstance().putVideo("Camera2", 160, 120);
		                   
		            Mat source = new Mat();
		            Mat output = new Mat();
		          
		        while(!Thread.interrupted()) {
		                cvSink.grabFrame(source);
		                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
		                outputStream.putFrame(output);
		            }
		        }).start();
		        
		    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    
    	//CameraServer.getInstance().startAutomaticCapture();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
