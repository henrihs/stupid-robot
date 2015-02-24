package edu.wsu.robot;

import org.junit.Test;

import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ObservableSensorFactory;
import edu.wsu.sensors.SensorHandler;
import static org.mockito.Mockito.*;

public class RobotState_InitSensorsTest {

	@Test
	public void doWork_InitsWhatWeNeed(){
		Robot fakeRobot = mock(Robot.class);
		SensorHandler fakeHandler = mock(SensorHandler.class);
		ObservableSensorFactory factory = new ObservableSensorFactory(fakeRobot, fakeHandler);
		ObservableSensorFactory fakeFactory = spy(factory); 
		RobotState_InitSensors state = new RobotState_InitSensors(fakeHandler);
		
		state.doWork(fakeRobot, fakeFactory);
		
		verify(fakeFactory, times(8)).create(isA(ESensor.class));
	}

}
