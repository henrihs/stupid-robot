package edu.wsu.sensors;

import java.util.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_Drive;


public class SensorHandlerTests {
	
	private SensorHandler handler;
	private Robot fakeRobot;

	@Before
	public void initialize(){
		handler = SensorHandler.getInstance();
//		fakeRobot = Mockito.mock(Robot.class);
		fakeRobot = new Robot();
	}
	
	@After
	public void cleanup(){
		handler.setRobot(null);
		fakeRobot = null;
	}
	
	@Test
	public void getInstance_firedTwice_onlyHasOneInstance(){
		SensorHandler handler2 = SensorHandler.getInstance();
		
		assertEquals(handler, handler2);
	}
	
	@Test
	public void getNextState_isClearInFront_returnsDriveState(){
		ESensor sensor = ESensor.FRONTL;
		ISensorStates sensorState = new SensorState_Clear();
		
		IRobotStates nextState = handler.getNextState(sensor, sensorState);
		
		assertTrue(nextState instanceof RobotState_Drive);
		
//		Observer fakeObserver = mock(Observer.class);
//		handler.addObserver(fakeObserver);
//		ObservableSensor fakeSensor = mock(ObservableSensor.class);
//		when(fakeSensor.getSensor()).thenReturn(ES)
//		ISensorStates fakeSensorState = mock(ISensorStates.class);
//		
//		handler.update(fakeSensor, fakeSensorState);
		
//		verify(fakeObserver, times(1)).update(handler, isA(IRobotStates.class));
	}

}
