package org.firstinspires.ftc.teamcode.RobitsRobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="RobitsRobot", group="Linear Opmode")

public class RobitsRobot extends OpMode {

//DcMotor IntakeMotor;

    DcMotor CatapultMotor;

    DcMotor leftSide;
    DcMotor rightSide;

    Servo indexer;

    @Override
    public void init() {
//
//        IntakeMotor = hardwareMap.dcMotor.get("IntakeMotor");

        CatapultMotor = hardwareMap.dcMotor.get("CatapultMotor");

        leftSide = hardwareMap.get(DcMotor.class, "leftSide");
        rightSide = hardwareMap.get(DcMotor.class, "rightSide");
        rightSide.setDirection(DcMotorSimple.Direction.REVERSE);

        indexer = hardwareMap.get(Servo.class, "indexer");
    }



    @Override
    public void loop() {

        //Intake Code
//        if (gamepad1.a == true) {
//            IntakeMotor.setPower(1);
//
//        }
//
//        else if (gamepad1.a == false){
//            IntakeMotor.setPower(0);
//
//        }

        //Catapult Load
        if (gamepad1.y == true) {
            CatapultMotor.setPower(1);
            indexer.setPosition(0);


        }

        //Unload
        if (gamepad1.x == true) {
            CatapultMotor.setPower(-1);
        } else if (gamepad1.y == false) {
            indexer.setPosition(0.5);
            CatapultMotor.setPower(0);

        }


        //Drivetrain Code
        double rightPower = gamepad1.left_stick_y;
        double leftPower = gamepad1.right_stick_y;

        leftSide.setPower(leftPower);
        rightSide.setPower(rightPower);


    }

    private void pause(int milliseconds) {
        CatapultMotor.setPower(0);

    }


}

