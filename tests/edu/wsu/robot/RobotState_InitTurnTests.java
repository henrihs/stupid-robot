package edu.wsu.robot;

import org.junit.Before;
import org.junit.Test;

import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Turn;
import edu.wsu.sensors.ESensor;
import static common.PropertyReader.*;
import static org.mockito.Mockito.*;

public class RobotState_InitTurnTests {
	
	private RobotState_InitTurn state;
	private Robot fakeRobot;
	
	@Before
	public void initialize(){
		state = new RobotState_InitTurn();
		fakeRobot = mock(Robot.class);
		when(fakeRobot.getRightWheelPosition()).thenReturn((long)0);
		when(fakeRobot.getLeftWheelPosition()).thenReturn((long)0);
	}

	@Test
	public void doWork_mustTurnAround_setsWheelEndsToThreeTimesTurnAround(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(-getTurnAround()*3);
		verify(fakeRobot).setLeftWheelEnd(getTurnAround()*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}
	
	@Test
	public void doWork_mustTurnRight_setsWheelEndsToThreeTimesRightTurn(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(0);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(-getRightTurn()*3);
		verify(fakeRobot).setLeftWheelEnd(getRightTurn()*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}
	
	@Test
	public void doWork_mustTurnLeft_setsWheelEndsToThreeTimesLeftTurn(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		state.doWork(fakeRobot);
		
		verify(fakeRobot).setRightWheelEnd(-getLeftTurn()*3);
		verify(fakeRobot).setLeftWheelEnd(getLeftTurn()*3);
		verify(fakeRobot).setState(any(RobotState_Turn.class));
	}	
	
	@Test
	public void doWork_mustTurnEitherWay_setsWheelEndsToThreeTimesEitherTurn(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(0);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		state.doWork(fakeRobot);
		
		if (state.wasLastTurnPositive()){
			verify(fakeRobot).setRightWheelEnd(-getLeftTurn()*3);
			verify(fakeRobot).setLeftWheelEnd(getLeftTurn()*3);
		}
		else {
			verify(fakeRobot).setRightWheelEnd(-getLeftTurn()*3);
			verify(fakeRobot).setLeftWheelEnd(getLeftTurn()*3);
		}
		
		verify(fakeRobot).setState(any(RobotState_Turn.class));		
	}

}
