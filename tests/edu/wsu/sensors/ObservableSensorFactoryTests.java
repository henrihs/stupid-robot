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
		int sensors = 3;
		ObservableSensorFactory factory = new ObservableSensorFactory(fakeRobot, fakeHandler, sensors);
		
		factory.createDistAndLightSensors(ESensor.BACKL);
		factory.createDistAndLightSensors(ESensor.ANGLEL);
		factory.createDistAndLightSensors(ESensor.FRONTL);
		Thread.sleep(50);
		verify(fakeHandler, times(sensors)).update(any(DistanceSensor.class), any());
		verify(fakeHandler, times(sensors)).update(any(LightSensor.class), any());
		when(fakeRobot.getDistanceValue(anyInt())).thenReturn(1300);
		Thread.sleep(50);
		verify(fakeHandler, times(sensors*2)).update(any(DistanceSensor.class), any());
		verify(fakeHandler, times(sensors*2)).update(any(LightSensor.class), any());
	}

}
