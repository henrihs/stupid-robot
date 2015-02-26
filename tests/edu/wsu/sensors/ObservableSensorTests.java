package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObservableSensorTests {
	
	public ObservableSensor observableSensor;
	private Robot fakeRobot;
	private ESensor sensor;

	@Before
	public void initialize(){
		fakeRobot = mock(Robot.class);
		sensor = ESensor.ANGLEL;
		observableSensor = new ObservableSensor(fakeRobot, sensor);
	}
	
	@Test
	public void run_invokesStateObject(){
		ISensorStates fakeState = mock(ISensorStates.class);
		observableSensor.setState(fakeState);

		observableSensor.run();
		
		verify(fakeState, times(1)).doWork(fakeRobot, sensor);
	}
	
	@Test
	public void run_doesNotChangeState_doesNotNotifyObservers(){
		SensorState_Clear fakeState = mock(SensorState_Clear.class);
		observableSensor.setState(fakeState);
		ObservableSensor spySensor = spy(observableSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof SensorState_Clear);
		verify(spySensor, never()).notifyObservers(any());
	}
	
	@Test
	public void run_changesState_notifiesObservers(){
		SensorState_Clear fakeState = mock(SensorState_Clear.class);
		when(fakeState.doWork(fakeRobot, sensor)).thenReturn(new SensorState_Obstacle());
		observableSensor.setState(fakeState);
		ObservableSensor spySensor = spy(observableSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof SensorState_Obstacle);
		verify(spySensor, times(1)).notifyObservers(any());
	}

}
