package edu.wsu.modelling;

import java.util.Observable;
import java.util.Observer;
import edu.wsu.sensors.SensorHandler;

public class Modeller implements Observer, TurnListener {
	
	private static int directionNumber;
	private final EnvModel envModel;
	private final RenderedModel rendModel;
	
	public enum EDirection {UP, RIGHT, DOWN, LEFT;}
	
	public Modeller(){
		this(new EnvModel(52));
	}

	public Modeller(EnvModel envModel) {
		this.envModel = envModel;
		envModel.initRobotPresence();
		rendModel = new RenderedModel(envModel);
		directionNumber = 1;
	}
	
	@Override
	public void onTurnInitialized(int angle) {
		changeDirection(angle);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorHandler))
			return;
		envModel.moveRobotPresence(getDirectionEnum());
		System.out.println(envModel);
	}
	
	public EDirection getDirectionEnum(){
		switch (directionNumber) {
		case 0:
			return EDirection.UP;
		case 1:
			return EDirection.RIGHT;
		case 2:
			return EDirection.DOWN;
		case 3:
			return EDirection.LEFT;
		default:
			return null;
		}
	}
	
	private void changeDirection(int angle){
		int direction = getDirection();
		direction += angle/90;
		direction %= 4;
		if (direction < 0)
			direction += 4;
		setDirection(direction);
	}
	
	private int getDirection(){
		return directionNumber;
	}
	
	private void setDirection(int direction){
		directionNumber = direction;
	}

}
