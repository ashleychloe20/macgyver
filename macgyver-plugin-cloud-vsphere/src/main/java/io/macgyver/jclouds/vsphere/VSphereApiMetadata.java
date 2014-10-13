/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.net.URI;

import org.jclouds.apis.internal.BaseApiMetadata;
import org.jclouds.compute.ComputeServiceContext;

/**
 * Implementation of {@link ApiMetadata} for an example of library integration (ServerManager)
 */
public class VSphereApiMetadata extends BaseApiMetadata {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public VSphereApiMetadata() {
      super(new Builder());
   }

   protected VSphereApiMetadata(Builder builder) {
      super(builder);
   }

   public static class Builder extends BaseApiMetadata.Builder<Builder> {

      protected Builder() {
         id("vsphere")
         .name("vsphere")
         .identityName("Unused")
         .documentation(URI.create("http://www.jclouds.org/documentation/userguide/compute"))
         .view(ComputeServiceContext.class)
         .defaultModule(ServerManagerComputeServiceContextModule.class);
         
      }

      @Override
      public VSphereApiMetadata build() {
    	  
         return new VSphereApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
