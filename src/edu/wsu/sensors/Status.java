package edu.wsu.sensors;

import java.util.HashMap;

public class Status {
	
	private HashMap<ISensorStates, Integer> states;
	
	public Status(ISensorStates state) {
		this.states = new HashMap<ISensorStates, Integer>();
		addOrCreateState(state);
	}
	
	public void addOrCreateState(ISensorStates state) {
		if (!states.containsKey(state))
			states.put(state, 1);
		else
			states.put(state, states.get(state) + 1);
	}
	
	public ISensorStates getMostFrequentState() {
		ISensorStates state = null;
		int frequency = 0;
		for (ISensorStates s: states.keySet()) {
			if (state == null || states.get(s) > frequency) {
				state = s;
				frequency = states.get(s);
			}
		}
		return state;
	}
}
