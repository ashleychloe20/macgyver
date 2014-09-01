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
package io.macgyver.jclouds.vsphere.compute.strategy;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Iterables.filter;
import io.macgyver.jclouds.vsphere.Datacenter;
import io.macgyver.jclouds.vsphere.Hardware;
import io.macgyver.jclouds.vsphere.Image;
import io.macgyver.jclouds.vsphere.VSphereServerManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.Template;
import org.jclouds.domain.LoginCredentials;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * defines the connection between the {@link VSphereServerManager} implementation and the jclouds
 * {@link ComputeService}
 */
@Singleton
public class ServerManagerComputeServiceAdapter implements ComputeServiceAdapter<VirtualMachine, Hardware, Image, Datacenter> {
   private final VSphereServerManager client;

   @Inject
   public ServerManagerComputeServiceAdapter(VSphereServerManager client) {
      this.client = checkNotNull(client, "client");
      
   }


   
   @Override
   public NodeAndInitialCredentials<VirtualMachine>  createNodeWithGroupEncodedIntoName(String tag, String name, Template template) {
      // create the backend object using parameters from the template.
      VirtualMachine from = client.createServerInDC(template.getLocation().getId(), name,
            Integer.parseInt(template.getImage().getProviderId()),
            Integer.parseInt(template.getHardware().getProviderId()));
      return new NodeAndInitialCredentials<VirtualMachine>(from, from.getMOR().getVal() + "", LoginCredentials.builder().user("none")
            .password("none").build());
   }

   @Override
   public Iterable<Hardware> listHardwareProfiles() {
      return client.listHardware();
   }

   @Override
   public Iterable<Image> listImages() {
	  
      return client.listImages();
   }
   
   @Override
   public Image getImage(String id) {
      int imageId = Integer.parseInt(id);
      return client.getImage(imageId);
   }
   
   @Override
   public Iterable<VirtualMachine> listNodes() {
	   System.out.println("listNodes: "+this);
	   
      return client.listServers();
   }

   @Override
   public Iterable<VirtualMachine> listNodesByIds(final Iterable<String> ids) {
      return filter(listNodes(), new Predicate<VirtualMachine>() {

            @Override
            public boolean apply(VirtualMachine server) {
               return contains(ids, server.getMOR().getVal());
            }
         });
   }
   
   @Override
   public Iterable<Datacenter> listLocations() {
      return ImmutableSet.of(new Datacenter(1, "SFO"));
   }

   @Override
   public VirtualMachine getNode(String id) {
      int serverId = Integer.parseInt(id);
      return client.getServer(serverId);
   }

   @Override
   public void destroyNode(String id) {
      client.destroyServer(Integer.parseInt(id));
   }

   @Override
   public void rebootNode(String id) {
      client.rebootServer(Integer.parseInt(id));      
   }

   @Override
   public void resumeNode(String id) {
      client.startServer(Integer.parseInt(id));      
      
   }

   @Override
   public void suspendNode(String id) {
      client.stopServer(Integer.parseInt(id));      
   }
}
