package io.macgyver.metrics;

import org.joda.time.DateTime;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class BasicTSV implements TSV {

	Number val;
	DateTime dateTime;
	
	

	public BasicTSV(DateTime dateTime, Number n) {
		Preconditions.checkNotNull(dateTime, "timestamp should not be null");
		Preconditions.checkNotNull(n,"value should not be null");
		this.val = n;
		this.dateTime = dateTime;
	}
	@Override
	public Number getValue() {
		return val;
	}

	@Override
	public DateTime getDateTime() {
		return dateTime;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("ts", getDateTime()).add("val", getValue()).toString();
	}
}
