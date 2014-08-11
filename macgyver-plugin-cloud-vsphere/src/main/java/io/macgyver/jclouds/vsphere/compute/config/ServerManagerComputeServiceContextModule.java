/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.jclouds.vsphere.compute.config;

import io.macgyver.jclouds.vsphere.Datacenter;
import io.macgyver.jclouds.vsphere.Hardware;
import io.macgyver.jclouds.vsphere.Image;
import io.macgyver.jclouds.vsphere.Server;
import io.macgyver.jclouds.vsphere.compute.functions.DatacenterToLocation;
import io.macgyver.jclouds.vsphere.compute.functions.ServerManagerHardwareToHardware;
import io.macgyver.jclouds.vsphere.compute.functions.ServerManagerImageToImage;
import io.macgyver.jclouds.vsphere.compute.functions.ServerToNodeMetadata;
import io.macgyver.jclouds.vsphere.compute.strategy.ServerManagerComputeServiceAdapter;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

public class ServerManagerComputeServiceContextModule extends
         ComputeServiceAdapterContextModule<Server, Hardware, Image, Datacenter> {

   @Override
   protected void configure() {
      super.configure();
      bind(new TypeLiteral<ComputeServiceAdapter<Server, Hardware, Image, Datacenter>>() {
      }).to(ServerManagerComputeServiceAdapter.class);
      bind(new TypeLiteral<Function<Server, NodeMetadata>>() {
      }).to(ServerToNodeMetadata.class);
      bind(new TypeLiteral<Function<Image, org.jclouds.compute.domain.Image>>() {
      }).to(ServerManagerImageToImage.class);
      bind(new TypeLiteral<Function<Hardware, org.jclouds.compute.domain.Hardware>>() {
      }).to(ServerManagerHardwareToHardware.class);
      bind(new TypeLiteral<Function<Datacenter, Location>>() {
      }).to(DatacenterToLocation.class);
      // to have the compute service adapter override default locations
      install(new LocationsFromComputeServiceAdapterModule<Server, Hardware, Image, Datacenter>(){});
   }
}
