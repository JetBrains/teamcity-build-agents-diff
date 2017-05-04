/*
 * Copyright 2000-2015 JetBrains s.r.o.
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

import jetbrains.buildServer.controllers.agent.AgentDetailsFormBase;
import jetbrains.buildServer.serverSide.AgentCompatibility;
import jetbrains.buildServer.serverSide.SProject;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CompatibleConfigurationsDiff {

  private final Map<SProject, List<AgentCompatibility>> myOnlyA;
  private final Map<SProject, List<AgentCompatibility>> myOnlyB;
  private final Map<SProject, List<AgentCompatibility>> myMissingA;
  private final Map<SProject, List<AgentCompatibility>> myMissingB;
  private final AgentDetailsFormBase myFormA;
  private final AgentDetailsFormBase myFormB;

  public CompatibleConfigurationsDiff(Map<SProject, List<AgentCompatibility>> mapOnlyA, Map<SProject, List<AgentCompatibility>> mapOnlyB, Map<SProject, List<AgentCompatibility>> mapMissingA, Map<SProject, List<AgentCompatibility>> mapMissingB, AgentDetailsFormBase formA, AgentDetailsFormBase formB) {
    this.myOnlyA = mapOnlyA;
    this.myOnlyB = mapOnlyB;
    this.myMissingA = mapMissingA;
    this.myMissingB = mapMissingB;

    myFormA = formA;
    myFormB = formB;
  }

  public Map<SProject, List<AgentCompatibility>> getOnlyA() {
    return myOnlyA;
  }

  public Map<SProject, List<AgentCompatibility>> getOnlyB() {
    return myOnlyB;
  }

  public Map<SProject, List<AgentCompatibility>> getMissingA() {
    return myMissingA;
  }

  public Map<SProject, List<AgentCompatibility>> getMissingB() {
    return myMissingB;
  }

  public static CompatibleConfigurationsDiff empty() {
    final Map<SProject, List<AgentCompatibility>> empty = Collections.emptyMap();
    return new CompatibleConfigurationsDiff(empty, empty, empty, empty, null, null);
  }

  public boolean isDifferent() {
    return !myOnlyA.isEmpty() || !myOnlyB.isEmpty() || !myMissingA.isEmpty() || !myMissingB.isEmpty();
  }

  public AgentDetailsFormBase getFormA() {
    return myFormA;
  }

  public AgentDetailsFormBase getFormB() {
    return myFormB;
  }
}
