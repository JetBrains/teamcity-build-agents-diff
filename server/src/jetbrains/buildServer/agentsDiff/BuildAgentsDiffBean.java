/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.agentsDiff;

import jetbrains.buildServer.BuildAgent;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.agentTypes.AgentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffBean {
  @Nullable
  private final AgentDescription myAgentA;
  @Nullable
  private final AgentDescription myAgentB;
  @NotNull
  private final Collection<BuildAgentsDiffEntry> myEntries;
  @NotNull
  private final CompatibleConfigurationsDiff myConfigurations;

  public BuildAgentsDiffBean(@Nullable AgentDescription agentA,
                             @Nullable AgentDescription agentB,
                             @Nullable Collection<BuildAgentsDiffEntry> entries,
                             @Nullable CompatibleConfigurationsDiff configurations) {
    myAgentA = agentA;
    myAgentB = agentB;
    myEntries = entries != null ? entries : Collections.<BuildAgentsDiffEntry>emptyList();
    myConfigurations = configurations != null ? configurations : CompatibleConfigurationsDiff.empty();
  }

  @NotNull
  public Collection<BuildAgentsDiffEntry> getEntries(){
    return myEntries;
  }

  @Nullable
  public AgentDescription getDescriptionA() {
    return myAgentA;
  }

  @Nullable
  public AgentDescription getDescriptionB() {
    return myAgentB;
  }

  @Nullable
  public SBuildAgent getAgentA() {
    return myAgentA instanceof SBuildAgent ? (SBuildAgent) myAgentA : null;
  }

  @Nullable
  public SBuildAgent getAgentB() {
    return myAgentB instanceof SBuildAgent ? (SBuildAgent) myAgentB : null;
  }

  @Nullable
  public String getIdA() {
    if (myAgentA == null) return null;
    return String.valueOf(myAgentA instanceof BuildAgent ? ((BuildAgent) myAgentA).getId() : (((AgentType) myAgentA)).getAgentTypeId());
  }

  @Nullable
  public String getIdB() {
    if (myAgentB == null) return null;
    return String.valueOf(myAgentB instanceof BuildAgent ? ((BuildAgent) myAgentB).getId() : (((AgentType) myAgentB)).getAgentTypeId());
  }

  @NotNull
  public CompatibleConfigurationsDiff getConfigurations() {
    return myConfigurations;
  }

  public static BuildAgentsDiffBean empty() {
    return new BuildAgentsDiffBean(null, null, null, null);
  }
}
