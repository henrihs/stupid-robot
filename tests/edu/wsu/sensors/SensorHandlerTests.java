package edu.wsu.sensors;

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
		handler = new SensorHandler();
		fakeRobot = mock(Robot.class);
		frontSensor = ESensor.FRONTR;
	}
	
	@After
	public void cleanup(){
		fakeRobot = null;
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
	public void update_robotInInitTurnState_notifiesObservers(){
		IRobotStates fakeRobotState = new RobotState_InitTurn();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, times(1)).notifyObservers(any());
	}
	
	@Test
	public void update_robotInTurnState_notifiesObservers(){
		IRobotStates fakeRobotState = new RobotState_Turn();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, times(1)).notifyObservers(any());
	}
	
	@Test
	public void update_robotInDriveState_notifiesObservers(){
		IRobotStates fakeRobotState = new RobotState_Drive();
		when(fakeRobot.getState()).thenReturn(fakeRobotState);
		SensorHandler spyHandler = spy(handler);
		
		spyHandler.update(new ObservableSensor(fakeRobot, frontSensor), new SensorState_Clear());

		verify(spyHandler, times(1)).notifyObservers(any());
	}
}
