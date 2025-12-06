//package org.firstinspires.ftc.teamcode.tuning;
//
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.Trajectory;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//
//
//
//public class trajectoryPractice extends LinearOpMode {
//
//
//    @Override
//
//        public void runOpMode() {
//        Pose2d myPose = new Pose2d(10, -5, Math.toRadians(90));
//        Vector2d myVector = new Vector2d(10, -5);
//
//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//
//        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
//                .strafeRight(10)
//                .forward(5)
//                .build();
//
//        waitForStart();
//
//        if(isStopRequested()) return;
//
//        drive.followTrajectory(myTrajectory);
//
//
//        }
//
//    }
//
