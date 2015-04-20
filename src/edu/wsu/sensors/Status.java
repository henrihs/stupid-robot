package edu.wsu.sensors;

import java.util.HashMap;

public class Status {
	
	private HashMap<ISensorState, Integer> states;
	
	public Status(ISensorState state) {
		this.states = new HashMap<ISensorState, Integer>();
		addOrCreateState(state);
	}
	
	public void addOrCreateState(ISensorState state) {
		if (!states.containsKey(state))
			states.put(state, 1);
		else
			states.put(state, states.get(state) + 1);
	}
	
	public ISensorState getMostFrequentState() {
		ISensorState state = null;
		int frequency = 0;
		for (ISensorState s: states.keySet()) {
			if (state == null || states.get(s) > frequency) {
				state = s;
				frequency = states.get(s);
			}
		}
		return state;
	}
}
