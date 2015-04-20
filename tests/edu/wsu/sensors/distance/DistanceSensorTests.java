package edu.wsu.sensors.distance;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ISensorState;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DistanceSensorTests {
	
	public DistanceSensor distanceSensor;
	private Robot fakeRobot;
	private ESensor sensor;

	@Before
	public void initialize(){
		fakeRobot = mock(Robot.class);
		sensor = ESensor.ANGLEL;
		distanceSensor = new DistanceSensor(fakeRobot, sensor);
	}
	
	@Test
	public void run_invokesStateObject(){
		ISensorState fakeState = mock(ISensorState.class);
		distanceSensor.setState(fakeState);

		distanceSensor.run();
		
		verify(fakeState, times(1)).doWork(fakeRobot, sensor);
	}
	
	@Test
	public void run_doesNotChangeState_doesNotNotifyObservers(){
		DistanceState_Clear fakeState = mock(DistanceState_Clear.class);
		distanceSensor.setState(fakeState);
		DistanceSensor spySensor = spy(distanceSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof DistanceState_Clear);
		verify(spySensor, never()).notifyObservers(any());
	}
	
	@Test
	public void run_changesState_notifiesObservers(){
		DistanceState_Clear fakeState = mock(DistanceState_Clear.class);
		when(fakeState.doWork(fakeRobot, sensor)).thenReturn(new DistanceState_Obstacle());
		distanceSensor.setState(fakeState);
		DistanceSensor spySensor = spy(distanceSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof DistanceState_Obstacle);
		verify(spySensor, times(1)).notifyObservers(any());
	}

}
