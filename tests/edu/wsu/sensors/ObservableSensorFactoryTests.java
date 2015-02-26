package edu.wsu.sensors;

import org.junit.Test;

import edu.wsu.robot.Robot;
import static org.mockito.Mockito.*;

public class ObservableSensorFactoryTests {
	
	@Test
	public void create_initializesSensors_sensorsUpdateSensorHandler() throws InterruptedException{
		SensorHandler fakeHandler = mock(SensorHandler.class);
		Robot fakeRobot = mock(Robot.class);
		when(fakeRobot.getDistanceValue(anyInt())).thenReturn(0);
		ObservableSensorFactory factory = new ObservableSensorFactory(fakeRobot, fakeHandler, 3);
		
		factory.create(ESensor.BACKL);
		factory.create(ESensor.ANGLEL);
		factory.create(ESensor.FRONTL);
		Thread.sleep(50);
		verify(fakeHandler, times(3)).update(any(ObservableSensor.class), any());
		when(fakeRobot.getDistanceValue(anyInt())).thenReturn(1300);
		Thread.sleep(50);
		verify(fakeHandler, times(6)).update(any(ObservableSensor.class), any());
	}

}
