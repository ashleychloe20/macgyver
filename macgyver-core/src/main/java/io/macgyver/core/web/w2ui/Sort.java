package io.macgyver.core.web.w2ui;

public class Sort {

	public static enum Direction {
		ASCENDING("ASCENDING"),
		DESCENDING("DESCENDING");
		
		private final String direction;
		
		private Direction(String n) {
			this.direction = n;
		}
		public String toString() {
			return direction;
		}
		
		public static Direction fromString(String input) {
			if ("asc".equalsIgnoreCase(input)) {
				return ASCENDING;
			}
			else if ("desc".equalsIgnoreCase(input)) {
				return DESCENDING;
			}
			return Direction.valueOf(input);
		}
		 public boolean equalsName(String otherName){
		        return (otherName == null)? false:direction.equals(otherName);
		    }
	}
	
	String field;
	Direction direction = Direction.ASCENDING;
	
	
	public Sort(String field, Direction d) {
		this.field = field;
		this.direction = d;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
	
	
	
}
