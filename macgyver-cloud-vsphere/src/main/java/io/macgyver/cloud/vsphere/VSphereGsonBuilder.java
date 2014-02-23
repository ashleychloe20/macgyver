package io.macgyver.cloud.vsphere;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.VirtualMachine;

public class VSphereGsonBuilder {

	public static GsonBuilder createBuilder() {
		return new GsonBuilder().registerTypeAdapter(VirtualMachine.class,
				new VirtualMachineSerializer());

	}

	static class VirtualMachineSerializer implements
			JsonSerializer<VirtualMachine> {

		@Override
		public JsonElement serialize(VirtualMachine src, Type typeOfSrc,
				JsonSerializationContext context) {

			JsonObject obj = new JsonObject();

			VirtualMachineConfigInfo ci = src.getConfig();
			
			obj.add("config", context.serialize(ci));
			obj.add("guest",context.serialize(src.getGuest()));
			obj.addProperty("canonicalId", ci.getUuid());
			
			return obj;

		}
	}

}
