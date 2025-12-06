package org.firstinspires.ftc.teamcode.RobitsRobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class RedBigTriangleRobitsRobotAutonomus extends LinearOpMode {

    DcMotor CatapultMotor;

    Servo indexer;

    DcMotor leftSide;
    DcMotor rightSide;


    public void DriveForward (){
        leftSide.setPower(1);
        rightSide.setPower(1);
    }

    public void StopDriving(){
        leftSide.setPower(0);
        rightSide.setPower(0);
    }

    public void DriveBackward(){
        leftSide.setPower(-1);
        rightSide.setPower(-1);
    }

    public void TurnLeft(){
        leftSide.setPower(-1);
        rightSide.setPower(1);
    }

    public void TurnRight(){
        leftSide.setPower(1);
        rightSide.setPower(-1);
    }

    public void CatapultLoad() {
        CatapultMotor.setPower(1);
        indexer.setPosition(0);
    }

    public void CatapultUnload(){
        CatapultMotor.setPower(-1);
        indexer.setPosition(0.5);

    }
    public void CatapultShoot(){
        CatapultLoad();
        sleep(800);
        CatapultStop();
        sleep(500);
        CatapultUnload();
        sleep(700);
        CatapultStop();
        sleep(500);
    }


    public void CatapultStop() {
        CatapultMotor.setPower(0);
    }
    @Override
    public void runOpMode() throws InterruptedException {

        CatapultMotor = hardwareMap.dcMotor.get("CatapultMotor");

        leftSide = hardwareMap.get(DcMotor.class, "leftSide");
        rightSide = hardwareMap.get(DcMotor.class, "rightSide");
        leftSide.setDirection(DcMotorSimple.Direction.REVERSE);

        indexer = hardwareMap.get(Servo.class, "indexer");


        leftSide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        DriveForward();
        sleep(150);
        StopDriving();
        CatapultShoot();
        CatapultShoot();
        CatapultShoot();
        TurnRight();
        sleep(250);
        DriveBackward();
        sleep(2000);
        StopDriving();


//        TurnRight();
//        sleep(50);
//        StopDriving();
//        DriveBackward();
//        sleep(2250);
//        StopDriving();
//        sleep(1000);
//        DriveForward();
//        sleep(2250);
//        StopDriving();
//        sleep(100);
//        CatapultShoot();
//        CatapultShoot();
//        CatapultShoot();
//        DriveBackward();
//        sleep(2150);
//        StopDriving();
//        sleep(2000);
//        DriveForward();
//        sleep(2250);
//        StopDriving();
//        sleep(1000);
//        TurnLeft();
//        sleep(220);
//        StopDriving();
//        DriveBackward();
//        sleep(100);
//        StopDriving();
//        sleep( 100);
//        CatapultShoot();
//        CatapultShoot();
//        CatapultShoot();
//        DriveBackward();
//        sleep(200);
//        StopDriving();
//        TurnRight();
//        sleep(300);
//        StopDriving();
//        DriveBackward();
//        sleep(1500);
//        StopDriving();
//        sleep(1000);


    }
}
