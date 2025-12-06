package org.firstinspires.ftc.teamcode.RobitsRobotCode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "AprilTag + Telemetry", group = "Sensor")
public class RobitsLimelight extends OpMode {
    private Limelight3A limelight;
    private IMU imu;

    private DcMotor turretMotor;
    private boolean headingLockEnabled = false;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        imu = hardwareMap.get(IMU.class, "imu");
        turretMotor = hardwareMap.get(DcMotor.class, "turret");
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(orientation));
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double currentHeading = orientation.getYaw();
        limelight.updateRobotOrientation(currentHeading);

        if (gamepad1.a) headingLockEnabled = true;
        if (gamepad1.b) headingLockEnabled = false;

        LLStatus status = limelight.getStatus();
        telemetry.addData("LL Name", status.getName());
        telemetry.addData("LL Temp", "%.1fC", status.getTemp());
        telemetry.addData("LL CPU", "%.1f%%", status.getCpu());
        telemetry.addData("LL FPS", "%d", (int) status.getFps());
        telemetry.addData("Pipeline", "Index: %d, Type: %s", status.getPipelineIndex(), status.getPipelineType());

        LLResult result = limelight.getLatestResult();
        if (result != null) {
            telemetry.addData("LL Latency", result.getCaptureLatency() + result.getTargetingLatency());
            telemetry.addData("Parse Latency", result.getParseLatency());
            telemetry.addData("PythonOutput", java.util.Arrays.toString(result.getPythonOutput()));

            if (result.isValid()) {
                double tx = result.getTx();
                telemetry.addData("tx", tx);
                telemetry.addData("ty", result.getTy());
                telemetry.addData("Botpose", result.getBotpose().toString());

                if (headingLockEnabled) {
                    double kP = 0.02;
                    double turnPower = kP * tx;

                    if (Math.abs(tx) > 1.0) {
                        if (Math.abs(turnPower) < 0.05) {
                            turnPower = Math.copySign(0.05, turnPower);
                        }
                        turretMotor.setPower(turnPower);
                    } else {
                        turretMotor.setPower(0);
                    }
                }


                for (LLResultTypes.BarcodeResult br : result.getBarcodeResults()) ;  {
                   // telemetry.addData("Barcode", "Data: %s", cr.getData());
                }
                for (LLResultTypes.ClassifierResult cr : result.getClassifierResults()) {
                    telemetry.addData("Classifier", "Class: %s, Confidence: %.2f", cr.getClassName(), cr.getConfidence());
                }
                for (LLResultTypes.DetectorResult dr : result.getDetectorResults()) {
                    telemetry.addData("Detector", "Class: %s, Area: %.2f", dr.getClassName(), dr.getTargetArea());
                }
                for (LLResultTypes.FiducialResult fr : result.getFiducialResults()) {
                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                            fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
                }
                for (LLResultTypes.ColorResult cr : result.getColorResults()) {
                    telemetry.addData("Color", "X: %.2f, Y: %.2f", cr.getTargetXDegrees(), cr.getTargetYDegrees());
                }

                telemetry.update();

            }
        }
    }
    }