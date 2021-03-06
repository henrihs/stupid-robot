package edu.wsu.robot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_Stop;

public class RobotState_StopTests {

	@Test
	public void doWork_robotHasStopped(){
		RobotState_Stop state = new RobotState_Stop();
		Robot fakeRobot = mock(Robot.class);
				
		state.doWork(fakeRobot);
				
		verify(fakeRobot).stop();
	}

}
