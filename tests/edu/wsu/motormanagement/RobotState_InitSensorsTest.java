package edu.wsu.motormanagement;

import org.junit.Test;

import edu.wsu.motormanagement.Robot;
import edu.wsu.motormanagement.RobotState_InitSensors;
import edu.wsu.sensors.ESensor;
import edu.wsu.sensors.ObservableSensorFactory;
import edu.wsu.sensors.SensorHandler;
import static org.mockito.Mockito.*;

public class RobotState_InitSensorsTest {

	@Test
	public void doWork_InitsWhatWeNeed(){
		int numOfSensors = ESensor.values().length;
		Robot fakeRobot = mock(Robot.class);
		SensorHandler fakeHandler = mock(SensorHandler.class);
		ObservableSensorFactory factory = new ObservableSensorFactory(fakeRobot, fakeHandler, numOfSensors);
		ObservableSensorFactory fakeFactory = spy(factory); 
		RobotState_InitSensors state = new RobotState_InitSensors(fakeHandler);
		
		state.doWork(fakeRobot, fakeFactory);
		
		verify(fakeFactory, times(numOfSensors)).createDistAndLightSensors(isA(ESensor.class));
	}

}
