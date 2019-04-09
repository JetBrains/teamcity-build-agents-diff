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

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.intellij.openapi.util.Pair;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffCalculator {

  private static final String AGENT_TOOLS_DIR = "teamcity.agent.tools.dir";
  private static final String AGENT_WORK_DIR = "teamcity.agent.work.dir";
  private static final String AGENT_HOME_DIR = "teamcity.agent.home.dir";

  @NotNull
  public BuildAgentsDiffBean calculateDiff(@NotNull final AgentDescription agentA, @NotNull final AgentDescription agentB) {
    final Map<String, String> configParamsA = simplifyParameters(agentA.getAvailableParameters());
    final Map<String, String> configParamsB = simplifyParameters(agentB.getAvailableParameters());

    final List<BuildAgentsDiffEntry> entries = new LinkedList<>();

    final MapDifference<String, String> mapDifference = Maps.difference(configParamsA, configParamsB);
    if(!mapDifference.areEqual()){
      final Map<String, MapDifference.ValueDifference<String>> stringValueDifferenceMap = mapDifference.entriesDiffering();
      for(String key : stringValueDifferenceMap.keySet()){
        final MapDifference.ValueDifference<String> stringValueDifference = stringValueDifferenceMap.get(key);
        entries.add(new BuildAgentsDiffEntry(BuildAgentsDiffEntryType.PARAMETER_VALUE, key, stringValueDifference.leftValue(), stringValueDifference.rightValue()));
      }

      Map<String, String> map = mapDifference.entriesOnlyOnLeft();
      for(String key : map.keySet()){
        entries.add(new BuildAgentsDiffEntry(BuildAgentsDiffEntryType.PARAMETER_NAME, key, map.get(key), null));
      }

      map = mapDifference.entriesOnlyOnRight();
      for(String key : map.keySet()){
        entries.add(new BuildAgentsDiffEntry(BuildAgentsDiffEntryType.PARAMETER_NAME, key, null, map.get(key)));
      }
    }
    entries.sort((o1, o2) -> o1.getPropertyName().compareToIgnoreCase(o2.getPropertyName()));
    return new BuildAgentsDiffBean(agentA, agentB, entries);
  }

  @NotNull
  static Map<String, String> simplifyParameters(@NotNull final Map<String, String> parameters) {
    final List<Pair<String, String>> replacements = getReplacements(parameters);
    if (replacements.isEmpty()) return parameters;

    final Map<String, String> result = new HashMap<>();
    entry:
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      for (Pair<String, String> pair : replacements) {
        if (!key.equals(pair.second) && value.contains(pair.first)) {
          result.put(key, value.replace(pair.first, escape(pair.second)));
          continue entry;
        }
      }
      result.put(key, value);
    }

    return result;
  }

  @NotNull
  private static List<Pair<String, String>> getReplacements(@NotNull Map<String, String> parameters) {
    ArrayList<Pair<String, String>> result = new ArrayList<>(3);
    String toolsDir = parameters.get(AGENT_TOOLS_DIR);
    String workDir = parameters.get(AGENT_WORK_DIR);
    String agentHomeDir = parameters.get(AGENT_HOME_DIR);

    if (!StringUtil.isEmptyOrSpaces(toolsDir)) {
      result.add(Pair.create(toolsDir, AGENT_TOOLS_DIR));
    }
    if (!StringUtil.isEmptyOrSpaces(workDir)) {
      result.add(Pair.create(workDir, AGENT_WORK_DIR));
    }
    if (!StringUtil.isEmptyOrSpaces(agentHomeDir)) {
      result.add(Pair.create(agentHomeDir, AGENT_HOME_DIR));
    }
    return result;
  }

  private static String escape(String s) {
    return "%" + s + "%";
  }
}
