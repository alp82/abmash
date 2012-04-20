package com.abmash.core.query;

public class DirectionOptions {

	DirectionType type;
	DistanceType distanceType;
	int limit = 0;
	int limitPerTarget = 0;
	int minDistance = 0;
	int maxDistance = 0;
	boolean inBounds = false;
	boolean directionHasToMatchAllTargets = false;
	
	public DirectionOptions(DirectionType type) {
		this.type = type;
	}

	public DirectionType getType() {
		return type;
	}
	
	public DistanceType getDistanceType() {
		return distanceType;
	}

	public int getLimit() {
		return limit;
	}

	public int getLimitPerTarget() {
		return limitPerTarget;
	}

	public int getMinDistance() {
		return minDistance;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public boolean isInBounds() {
		return inBounds;
	}

	public boolean isDirectionHasToMatchAllTargets() {
		return directionHasToMatchAllTargets;
	}

	public DirectionOptions setType(DirectionType type) {
		this.type = type;
		return this;
	}
	
	public void setDistanceType(DistanceType distanceType) {
		this.distanceType = distanceType;
	}

	public DirectionOptions setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public DirectionOptions setLimitPerTarget(int limitPerTarget) {
		this.limitPerTarget = limitPerTarget;
		return this;
	}

	public DirectionOptions setMinDistance(int minDistance) {
		this.minDistance = minDistance;
		return this;
	}

	public DirectionOptions setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
		return this;
	}

	public DirectionOptions setInBounds(boolean inBounds) {
		this.inBounds = inBounds;
		return this;
	}

	public DirectionOptions setDirectionHasToMatchAllTargets(boolean directionHasToMatchAllTargets) {
		this.directionHasToMatchAllTargets = directionHasToMatchAllTargets;
		return this;
	}
	
	public String buildCommandSelector() {
		String options = "{ " +
			(type != null ? "directionType:'" + type.toString() + "'," : "") +
			(distanceType != null ? "distanceType:'" + distanceType.toString() + "'," : "") +
			(limit > 0 ? "limit: " + String.valueOf(limit) + "," : "") +
			(limitPerTarget > 0 ? "limitPerTarget:" + String.valueOf(limitPerTarget) + "," : "") +
			(minDistance > 0 ? "minDistance:" + String.valueOf(minDistance) + "," : "") +
			(maxDistance > 0 ? "maxDistance:" + String.valueOf(maxDistance) + "," : "") +
			(inBounds ? "inBounds:true," : "") +
			(directionHasToMatchAllTargets ? "directionHasToMatchAllTargets:true," : "") +
		" }";
		return options;
	}
	
}
