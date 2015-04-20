package edu.wsu.sensors;

import edu.wsu.robot.Robot;
import edu.wsu.sensors.distance.DistanceSensor;
import edu.wsu.sensors.distance.DistanceState_Clear;
import edu.wsu.sensors.distance.DistanceState_Obstacle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObservableSensorTests {
	
	public DistanceSensor observableSensor;
	private Robot fakeRobot;
	private ESensor sensor;

	@Before
	public void initialize(){
		fakeRobot = mock(Robot.class);
		sensor = ESensor.ANGLEL;
		observableSensor = new DistanceSensor(fakeRobot, sensor);
	}
	
	@Test
	public void run_invokesStateObject(){
		ISensorState fakeState = mock(ISensorState.class);
		observableSensor.setState(fakeState);

		observableSensor.run();
		
		verify(fakeState, times(1)).doWork(fakeRobot, sensor);
	}
	
	@Test
	public void run_doesNotChangeState_doesNotNotifyObservers(){
		DistanceState_Clear fakeState = mock(DistanceState_Clear.class);
		observableSensor.setState(fakeState);
		DistanceSensor spySensor = spy(observableSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof DistanceState_Clear);
		verify(spySensor, never()).notifyObservers(any());
	}
	
	@Test
	public void run_changesState_notifiesObservers(){
		DistanceState_Clear fakeState = mock(DistanceState_Clear.class);
		when(fakeState.doWork(fakeRobot, sensor)).thenReturn(new DistanceState_Obstacle());
		observableSensor.setState(fakeState);
		DistanceSensor spySensor = spy(observableSensor);

		spySensor.run();
		
		assertTrue(spySensor.getState() instanceof DistanceState_Obstacle);
		verify(spySensor, times(1)).notifyObservers(any());
	}

}
