package edu.wsu.robot;

import org.junit.Before;
import org.junit.Test;

import edu.wsu.robot.IRobotStates;
import edu.wsu.robot.Robot;
import edu.wsu.robot.RobotState_Drive;
import edu.wsu.robot.RobotState_InitSensors;
import edu.wsu.robot.RobotState_InitTurn;
import edu.wsu.robot.RobotState_Stop;
import edu.wsu.robot.RobotState_Turn;
import edu.wsu.sensors.SensorHandler;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RobotTests {
	
	private Robot robot;
	private SensorHandler fakeHandler;

	@Before
	public void initialize(){
		fakeHandler = mock(SensorHandler.class);
		robot = new Robot(fakeHandler);
	}
	
	@Test
	public void verifyInitialization(){
		verify(fakeHandler, times(1)).addObserver(robot);
		assertTrue(robot.getState() instanceof RobotState_InitSensors);
	}
	
	@Test
	public void update_isInTurnState_doesNotChangeState(){
		RobotState_Turn turnState = new RobotState_Turn();
		RobotState_Drive driveState = new RobotState_Drive();
		robot.setNextState(turnState);
		
		robot.update(fakeHandler, driveState);
		
		assertSame(turnState, robot.getState());
	}
	
	@Test
	public void update_isInInitTurnState_doesNotChangeState(){
		RobotState_InitTurn initTurnState = new RobotState_InitTurn();
		RobotState_Drive driveState = new RobotState_Drive();
		robot.setNextState(initTurnState);
		
		robot.update(fakeHandler, driveState);
		
		assertSame(initTurnState, robot.getState());
	}
	
	@Test
	public void update_isInDriveState_ChangesToInitTurnState(){
		RobotState_Drive driveState = new RobotState_Drive();
		RobotState_InitTurn initTurnState = new RobotState_InitTurn();
		robot.setNextState(driveState);
		
		robot.update(fakeHandler, initTurnState);
		
		assertSame(initTurnState, robot.getState());
	}
	
	@Test
	public void update_isInDriveState_ChangesToStopState(){
		RobotState_Drive driveState = new RobotState_Drive();
		RobotState_Stop stopState = new RobotState_Stop();
		robot.setNextState(driveState);
		
		robot.update(fakeHandler, stopState);
		
		assertSame(stopState, robot.getState());
	}

	@Test
	public void update_isInStopState_ChangesToDriveState(){
		RobotState_Stop stopState = new RobotState_Stop();
		RobotState_Drive driveState = new RobotState_Drive();
		robot.setNextState(stopState);
		
		robot.update(fakeHandler, driveState);
		
		assertSame(driveState, robot.getState());
	}
	
	 @Test
	 public void doWork_invokesStateObject() throws Exception{
		 IRobotStates fakeState = mock(IRobotStates.class);
		 robot.setNextState(fakeState);
		 
		 robot.doWork();
		 
		 verify(fakeState, times(1)).doWork(robot);
	 }
}
