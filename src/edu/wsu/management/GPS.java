package edu.wsu.management;

import java.util.Stack;

import edu.wsu.modelling.ECellContent;
import edu.wsu.modelling.EnvModel;
import edu.wsu.modelling.IndexPair;

public class GPS {
	
	private EnvModel envModel;
	private PathFinder pathFinder;
	private Stack<IndexPair> path;
		
	public GPS() {
		
		envModel = new EnvModel(60);
		createOuterWalls();
		createRow(5, 0, 25);
		createCol(25, 5, 40);
		createRow(20, 45, 59);
		envModel.initRobotPresence();
		System.out.println(envModel);
		
		pathFinder = new PathFinder(envModel);
	}
	
	public void setEnvModel(EnvModel envModel) {
		pathFinder = new PathFinder(envModel);
	}
	
	public Order pathTo(IndexPair destination) {
		path = pathFinder.pathTo(destination);
		Order order = new Order(path);
		System.out.println(order);
		return order;
	}
	
	private void createOuterWalls() {
		for (int i = 0; i < 60; i++) {
			envModel.setCell(i, 0, ECellContent.OBSTACLE);
			envModel.setCell(i, 59, ECellContent.OBSTACLE);
			envModel.setCell(0, i, ECellContent.OBSTACLE);
			envModel.setCell(59, i, ECellContent.OBSTACLE);
		}
	}
	
	private void createRow(int row, int fromY, int toY) {
		for (int y = fromY; y <= toY; y++) {
			envModel.setCell(row, y, ECellContent.OBSTACLE);
		}
	}
	
	private void createCol(int col, int fromX, int toX) {
		for (int x = fromX; x < toX; x++) {
			envModel.setCell(x, col, ECellContent.OBSTACLE);
		}
	}
}
