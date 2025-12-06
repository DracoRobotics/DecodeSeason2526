package org.firstinspires.ftc.teamcode;




import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.MecanumDrive;
//import org.firstinspires.ftc.teamcode.tuning.TuningOpModes;


@Autonomous
public class BANEAutonRR  extends LinearOpMode {




    DcMotor intake;


    Servo indexer;


    DcMotor flywheelRight;
    DcMotor flywheelLeft;




    public class IntakeBall implements InstantFunction {




        @Override
        public void run() {
            intake.setPower(1);
            sleep(500);
            intake.setPower(0);
        }
    }




    public class ShootBall implements InstantFunction{




        @Override
        public void run() {
            flywheelRight.setPower(.60);
            flywheelLeft.setPower(.60);
            sleep(500);
            intake.setPower(1);
            sleep(500);
            flywheelRight.setPower(0);
            flywheelLeft.setPower(0);
            intake.setPower(0);
        }
    }




    @Override
    public void runOpMode() throws InterruptedException {




        intake = hardwareMap.get(DcMotor.class, "intake");




        flywheelRight = hardwareMap.get(DcMotor.class, "flywheelRight");
        flywheelLeft = hardwareMap.get(DcMotor.class, "flywheelLeft");


        flywheelRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        Pose2d beginPose = new Pose2d(new Vector2d(0, 0), Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder Auto1 = drive.actionBuilder(beginPose)
                .splineTo(new Vector2d(35, -34), Math.toRadians(270))
                .stopAndAdd(new IntakeBall())
                .lineToY(-40)
                .lineToY(-35)
                .splineToConstantHeading(new Vector2d(-10, -10), Math.toRadians(180))
                .turnTo(Math.toRadians(230))
                .stopAndAdd(new ShootBall())
                .splineTo(new Vector2d(14, -34), Math.toRadians(270))
                .stopAndAdd(new IntakeBall())
                // .lineToY(-52)
                .lineToY(-40)
                .lineToY(-35)
                .splineToConstantHeading(new Vector2d(-10, -10), Math.toRadians(180))
                .turnTo(Math.toRadians(230))
                .stopAndAdd(new ShootBall())
                .splineTo(new Vector2d(-11, -34), Math.toRadians(270))
                // .lineToY(-52)
                .lineToY(-40)
                .lineToY(-35)
                .splineToConstantHeading(new Vector2d(-10, -10), Math.toRadians(180))
                .turnTo(Math.toRadians(230))
                .stopAndAdd(new ShootBall())
                .splineTo(new Vector2d(38, -33), Math.toRadians(270));



        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(Auto1.build());

    }



}
