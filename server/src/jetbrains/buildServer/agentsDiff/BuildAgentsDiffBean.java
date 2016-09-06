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

import jetbrains.buildServer.serverSide.BuildAgentEx;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffBean {
  private final BuildAgentEx myAgentA;
  private final BuildAgentEx myAgentB;
  private final Collection<BuildAgentsDiffEntry> myEntries;

  public BuildAgentsDiffBean(BuildAgentEx agentA, BuildAgentEx agentB, Collection<BuildAgentsDiffEntry> entries) {
    myAgentA = agentA;
    myAgentB = agentB;
    myEntries = entries;
  }

  public Collection<BuildAgentsDiffEntry> getEntries(){
    return myEntries;
  }

  public BuildAgentEx getAgentA() {
    return myAgentA;
  }

  public BuildAgentEx getAgentB() {
    return myAgentB;
  }

  public static BuildAgentsDiffBean empty() {
    return new BuildAgentsDiffBean(null, null, Collections.EMPTY_LIST);
  }
}
