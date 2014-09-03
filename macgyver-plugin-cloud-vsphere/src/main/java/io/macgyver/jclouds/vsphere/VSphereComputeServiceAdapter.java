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
package io.macgyver.jclouds.vsphere;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Iterables.filter;

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
public class VSphereComputeServiceAdapter implements ComputeServiceAdapter<VirtualMachine, Hardware, Image, Datacenter> {
   private final VSphereServerManager client;

   @Inject
   public VSphereComputeServiceAdapter(VSphereServerManager client) {
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
	   throw new UnsupportedOperationException();    
   }
   
   @Override
   public Iterable<VirtualMachine> listNodes() {

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
	   throw new UnsupportedOperationException();    
   }

   @Override
   public VirtualMachine getNode(String id) {
      return client.getServer(id);
   }

   @Override
   public void destroyNode(String id) {
      client.destroyServer(Integer.parseInt(id));
   }

   @Override
   public void rebootNode(String id) {
      throw new UnsupportedOperationException();    
   }

   @Override
   public void resumeNode(String id) {
      client.startServer(Integer.parseInt(id));  throw new UnsupportedOperationException();      
   }

   @Override
   public void suspendNode(String id) {
	   throw new UnsupportedOperationException();       
   }
}
