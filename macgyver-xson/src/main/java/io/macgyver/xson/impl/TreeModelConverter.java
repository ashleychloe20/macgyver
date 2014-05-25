package io.macgyver.xson.impl;

public  interface TreeModelConverter {
	public <X> X convertObject(Object input, Class<X> output);

}