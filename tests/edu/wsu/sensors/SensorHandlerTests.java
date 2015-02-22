package edu.wsu.sensors;

import java.util.Observable;
import java.util.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Turn;


public class SensorHandlerTests {
	
	private SensorHandler handler;
	private Robot fakeRobot;
	private ESensor frontSensor;

	@Before
	public void initialize(){
		handler = SensorHandler.getInstance();
//		fakeRobot = Mockito.mock(Robot.class);
		fakeRobot = mock(Robot.class);
		handler.setRobot(fakeRobot);
		frontSensor = ESensor.FRONTR;
	}
	
	@After
	public void cleanup(){
		handler.setRobot(null);
		fakeRobot = null;
	}
	
	@Test
	public void getInstance_firedTwice_onlyHasOneInstance(){
		SensorHandler handler2 = SensorHandler.getInstance();
		
		assertSame(handler, handler2);
	}
	
	@Test
	public void getNextState_isClearInFront_returnsDriveState(){
		IRobotStates nextState = handler.getNextState(frontSensor, new SensorState_Clear());
		
		assertTrue(nextState instanceof RobotState_Drive);
	}
	
	@Test
	public void getNextState_isBlockedInFront_returnsInitTurnState(){
		IRobotStates nextState = handler.getNextState(frontSensor, new SensorState_Obstacle());
		
		assertTrue(nextState instanceof RobotState_InitTurn);
	}
	
	@Test
	public void getNextState_isBlockedOnSide_returnsDriveState(){
		ESensor sensor = ESensor.RIGHT;
		
		IRobotStates nextState = handler.getNextState(sensor, new SensorState_Obstacle());
		
		assertTrue(nextState instanceof RobotState_Drive);
	}
	
	@Test
	public void update_robotInInitTurnState_doesNotNotifyObservers(){
		IRobotStates fakeRobotState = new RobotState_InitTurn();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, never()).notifyObservers(any());
	}
	
	@Test
	public void update_robotInTurnState_doesNotNotifyObservers(){
		IRobotStates fakeRobotState = new RobotState_Turn();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, never()).notifyObservers(any());
	}
	
	@Test
	public void update_robotInDriveState_notifiesObservers(){
		IRobotStates fakeRobotState = new RobotState_Drive();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, times(1)).notifyObservers(any());
	}
	
	@Test
	public void calculateTurn_mustTurnAround_returns180(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		int turnToMake = handler.calculateTurn();
		
		assertEquals(180, turnToMake);
	}
	
	@Test
	public void calculateTurn_mustTurnRight_returns90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(0);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(1300);
		
		int turnToMake = handler.calculateTurn();
		
		assertEquals(90, turnToMake);
	}
	
	@Test
	public void calculateTurn_mustTurnLeft_returnsMinus90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		int turnToMake = handler.calculateTurn();
		
		assertEquals(-90, turnToMake);
	}	
	
	@Test
	public void calculateTurn_mustTurnEitherWay_returnsAbs90(){
		when(fakeRobot.getDistanceValue(ESensor.RIGHT.val())).thenReturn(1300);
		when(fakeRobot.getDistanceValue(ESensor.LEFT.val())).thenReturn(0);
		
		int turnToMake = handler.calculateTurn();
		
		assertEquals(90, Math.abs(turnToMake));
	}
}
