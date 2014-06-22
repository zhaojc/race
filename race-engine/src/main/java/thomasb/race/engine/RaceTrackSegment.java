package thomasb.race.engine;

import static java.lang.Math.sqrt;
import static java.util.Objects.hash;

public final class RaceTrackSegment extends AbstractSegment implements TrackSegment {
	private final int maxSpeed;
	private final boolean terminating;
	private final boolean finish;

	public RaceTrackSegment(PointDouble start,
			PointDouble end,
			int maxSpeed,
			boolean isTerminating,
			boolean isFinish) {
		super(start, end);
		this.maxSpeed = maxSpeed;
		this.terminating = isTerminating;
		this.finish = isFinish;
	}

	static RaceTrackSegment from(TrackSegment segment) {
		if (segment instanceof RaceTrackSegment) {
			return (RaceTrackSegment) segment;
		}
		
		return new RaceTrackSegment(segment.getStart(),
				segment.getEnd(),
				segment.getMaxSpeed(),
				segment.isTerminating(),
				segment.isFinish());
	}
	
	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}


	@Override
	public boolean isFinish() {
		return finish;
	}

	
	@Override
	public boolean isTerminating() {
		return terminating;
	}
	
	double length() {
		VectorPoint diff = VectorPoint.from(getEnd()).diff(getStart());
		
		return sqrt(diff.dot(diff));
	}
	
	@Override
	public final int hashCode() {
		return hash(getStart(), getEnd(), maxSpeed, finish, terminating);
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof TrackSegment)) {
			return false;
		}
		
		TrackSegment other = (TrackSegment) obj;
		
		return getStart().equals(other.getStart()) &&
				getEnd().equals(other.getEnd()) &&
				maxSpeed == other.getMaxSpeed() &&
				finish == other.isFinish() &&
				terminating == other.isTerminating();
	}


	@Override
	public String toString() {
		return "RaceTrackSegment [maxSpeed=" + maxSpeed +
				", terminating=" + terminating +
				", finish=" + finish +
				", start=" + getStart() +
				", end=" + getEnd() + "]";
	}
}