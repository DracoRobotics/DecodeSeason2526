package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp
public class BANELimelightTest extends OpMode {

    // --- Motor and Sensor Definitions ---
    private Limelight3A limelight;
    private IMU imu;
    private DcMotor turret;
    // Drive motors are commented out, keeping them here for reference
    // private DcMotor leftFrontDrive = null; 
    // private DcMotor leftBackDrive = null; 
    // private DcMotor rightFrontDrive = null; 
    // private DcMotor rightBackDrive = null; 

    // --- Control State Variables ---
    private boolean headingLockEnabled = false;
    private boolean isSearching = false;
    private final int MIN_TURRET_ENCODER_LIMIT = turret.getCurrentPosition() - 620; // Example: Minimum safe encoder count
    private final int MAX_TURRET_ENCODER_LIMIT = turret.getCurrentPosition() + 670;  // Example: Maximum safe encoder count

    // --- PID Control Variables (NEW) ---
    private double integralSum = 0.0;
    private double lastError = 0.0;
    private long lastTime = 0; // Stores the last timestamp for calculating dt

    // PID Constants - These MUST BE TUNED for your specific robot!
    // Start with Kp, then Kd, then Ki.
    private final double Kp = 0.00018;     // Proportional Gain
    private final double Ki = 0;   // Integral Gain
    private final double Kd = 0.00; // Derivative Gain
    private final double searchPower = 0.15; // Power set for "Sweeping"

    // Tolerance/Safety Limits
    private final double AIM_TOLERANCE_DEGREES = 4.0; // Angle error (tx) to stop at
    private final double MAX_TURN_POWER = 0.1;        // Maximum motor power for turning
    private final double INTEGRAL_CLAMP = 100.0;      // Limits integral wind-up

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        imu = hardwareMap.get(IMU.class, "imu");

        // --- Turret Motor Setup ---
        turret = hardwareMap.get(DcMotor.class, "turret");
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // It's often helpful to set a direction, e.g., DcMotor.Direction.REVERSE
        // if the turret turns the wrong way for a positive 'tx'.
        // turret.setDirection(DcMotor.Direction.REVERSE); 

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu.initialize(new IMU.Parameters(orientation));

        // Initialize last time for delta time (dt) calculation
        lastTime = System.currentTimeMillis();
        telemetry.addData("Turret Position", turret.getCurrentPosition());

    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        telemetry.addData("Turret Position", turret.getCurrentPosition());

        // IMU update for Limelight (optional but good practice)
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double currentHeading = orientation.getYaw();
        limelight.updateRobotOrientation(currentHeading);

        // Control Lock State
        if (gamepad1.dpad_left) {
            headingLockEnabled = true;
        }
        if (gamepad1.dpad_right ) {
            headingLockEnabled = false;
        }

        // --- Limelight Vision Processing ---
        LLResult result = limelight.getLatestResult();

        if(turret.getCurrentPosition() <= MIN_TURRET_ENCODER_LIMIT){
            turret.setPower(0);
            telemetry.addData("Below Minimum", "YES");
        }
        if(turret.getCurrentPosition() >= MAX_TURRET_ENCODER_LIMIT){
            turret.setPower(0);
            telemetry.addData("Above Maximum", "YES");
        }
        if(turret.getCurrentPosition()<= MIN_TURRET_ENCODER_LIMIT && turret.getCurrentPosition() >= MAX_TURRET_ENCODER_LIMIT) {
            telemetry.addData("IN Range", "YES");
        }

        if (result != null && result.isValid()) {

            isSearching = false;

            // The horizontal angle error is the current error for the PID controller
            double currentError = result.getTx();
            telemetry.addData("tx", currentError);
            telemetry.addData("Target Found", "Yes");

            if (headingLockEnabled) {

                // --- 1. Calculate Delta Time (dt) ---
                long currentTime = System.currentTimeMillis();
                // dt is the time difference since the last loop iteration (in seconds)
                double dt = (currentTime - lastTime) / 1000.0;
                lastTime = currentTime;

                // --- 2. Check Deadband (Aim Tolerance) ---
                if (Math.abs(currentError) < AIM_TOLERANCE_DEGREES) {
                    turret.setPower(0);

                    // Reset PID terms when locked to prevent wind-up/jittering
                    integralSum = 0;
                    lastError = 0;
                    telemetry.addData("Auto Aim", "LOCKED");

                } else {

                    // --- 3. PID Calculations ---

                    // Integral Sum: Accumulate error over time
                    integralSum += currentError * dt;

                    // Integral Clamp: Limit the integral sum to prevent "wind-up"
                    integralSum = Math.min(integralSum, INTEGRAL_CLAMP / Ki);
                    integralSum = Math.max(integralSum, -INTEGRAL_CLAMP / Ki);

                    // Derivative: Rate of change of error
                    double derivative = (currentError - lastError) / dt;

                    // Total PID Output (Turn Power)
                    double turnPower = (Kp * currentError) +
                            (Ki * integralSum) +
                            (Kd * derivative);

                    // --- 4. Clamp Max Power and Apply ---
                    turnPower = Math.min(MAX_TURN_POWER, Math.max(-MAX_TURN_POWER, turnPower));

                    turret.setPower(turnPower);

                    // --- 5. Update lastError for next iteration ---
                    lastError = currentError;

                    telemetry.addData("Auto Aim", "TURNING");
                    telemetry.addData("Turret Power", turnPower);
                    telemetry.addData("Error (tx)", currentError);
                }


            } else {

                integralSum = 0;
                lastError = 0;


                // Target is not valid or not found
                if (headingLockEnabled) {
                    // If we were trying to lock but lost the target, enter search mode
                    telemetry.addData("Target Found", "No - Searching");

                    if (!isSearching) {
                        // Start the search: reverse direction of last movement
                        // This uses a simple toggle, you can make this smarter if needed
                        // A better approach might involve a dedicated "search direction" variable
                        turret.setPower(-searchPower); // Simple sweep
                        isSearching = true;
                    } else {
                        // Keep searching (motor already set above)
                        turret.setPower(-searchPower);
                    }

                } else {
                    // Auto-aim is manually disabled, turn everything off
                    isSearching = false; // ** ADDED CODE **
                    turret.setPower(0);
                    telemetry.addData("Target Found", "N/A (Disabled)");
                    telemetry.addData("Auto Aim", "DISABLED");
                }
            }


        // --- Telemetry Updates (Cleaned up) ---
        LLStatus status = limelight.getStatus();
        telemetry.addData("LL Name", status.getName());
        telemetry.addData("LL FPS", "%d", (int) status.getFps());
        telemetry.update();

        // Note: I removed the complex loop for other LLResultTypes for brevity
        // but you can put them back if you need those details.
    }
}

}