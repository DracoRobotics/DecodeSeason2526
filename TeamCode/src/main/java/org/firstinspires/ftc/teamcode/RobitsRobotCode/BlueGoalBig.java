package org.firstinspires.ftc.teamcode.RobitsRobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class BlueGoalBig extends LinearOpMode {

    DcMotor CatapultMotor;

    DcMotor leftSide;
    DcMotor rightSide;

    Servo indexer;

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
        indexer.setPosition(0);
        CatapultMotor.setPower(1);


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
        indexer.setPosition(0.6);

        leftSide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightSide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                waitForStart();

        DriveBackward();
        sleep(525);
        StopDriving();
        sleep(400);
        CatapultShoot();
        CatapultShoot();
        CatapultShoot();
        DriveBackward();
        sleep(2200);
        StopDriving();



















//        TurnLeft();
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
//        sleep(2250);
//        StopDriving();
//        sleep(2150);
//        StopDriving();
//        sleep(2000);
//        DriveForward();
//        sleep(2000);
//        StopDriving();
//        sleep(1000);
//        TurnRight();
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
//        TurnLeft();
//        sleep(200);
//        StopDriving();
//        DriveBackward();
//        sleep(1400);
//        StopDriving();
//        sleep(1000);
//
 
    }
}
