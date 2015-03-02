package edu.wsu.motormanagement;

import org.junit.Test;

import edu.wsu.motormanagement.Robot;
import edu.wsu.motormanagement.RobotState_Drive;
import static org.mockito.Mockito.*;

public class RobotState_DriveTests {

	@Test
	public void doWork_robotIsDrivingStraightForward(){
		RobotState_Drive state = new RobotState_Drive();
		Robot fakeRobot = mock(Robot.class);
				
		state.doWork(fakeRobot);
				
		verify(fakeRobot).drive();
	}

}
