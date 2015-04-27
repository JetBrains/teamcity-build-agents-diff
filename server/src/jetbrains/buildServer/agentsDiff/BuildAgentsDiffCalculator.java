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
import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.agent.AgentDetailsFormBase;
import jetbrains.buildServer.controllers.agent.AgentDetailsFormFactory;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.agentTypes.SAgentType;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.Converter;
import jetbrains.buildServer.util.filters.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffCalculator {
  private static final Logger LOG = Logger.getInstance(BuildAgentsDiffCalculator.class.getName());

  @NotNull
  public BuildAgentsDiffBean calculateDiff(@NotNull final AgentDescription agentA, @NotNull final AgentDescription agentB, AgentDetailsFormFactory factory) {
    final Map<String, String> configParamsA = agentA.getAvailableParameters();
    final Map<String, String> configParamsB = agentB.getAvailableParameters();

    final List<BuildAgentsDiffEntry> entries = new LinkedList<BuildAgentsDiffEntry>();

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

    final CompatibleConfigurationsDiff configurationsDiff = getCompatibleConfigurationsDiff(agentA, agentB, factory);

    Collections.sort(entries, new Comparator<BuildAgentsDiffEntry>() {
      public int compare(BuildAgentsDiffEntry o1, BuildAgentsDiffEntry o2) {
        return o1.getPropertyName().compareToIgnoreCase(o2.getPropertyName());
      }
    });
    return new BuildAgentsDiffBean(agentA, agentB, entries, configurationsDiff);
  }

  @Nullable
  private CompatibleConfigurationsDiff getCompatibleConfigurationsDiff(AgentDescription agentA, AgentDescription agentB, AgentDetailsFormFactory factory) {
    final AgentDetailsFormBase formA = getAgentDetailsForm(agentA, factory);
    final AgentDetailsFormBase formB = getAgentDetailsForm(agentB, factory);
    if (formA == null || formB == null) return null;
    final List<AgentCompatibility> compatibilitiesA = formA.getActiveCompatibilities().getCompatibilities();
    final List<AgentCompatibility> compatibilitiesB = formB.getActiveCompatibilities().getCompatibilities();

    final List<SBuildType> buildTypesA = getCompatibleBuildTypes(compatibilitiesA);
    final List<SBuildType> buildTypesB = getCompatibleBuildTypes(compatibilitiesB);

    final Set<SBuildType> btOnlyA = new HashSet<SBuildType>(buildTypesA);
    btOnlyA.removeAll(buildTypesB);
    final Set<SBuildType> btOnlyB = new HashSet<SBuildType>(buildTypesB);
    btOnlyB.removeAll(buildTypesA);

    final Set<SBuildType> btMissingA = btOnlyB;
    final Set<SBuildType> btMissingB = btOnlyA;

    final Map<SProject, List<AgentCompatibility>> mapOnlyA = getCompatibleByProject(true, CollectionsUtil.filterCollection(compatibilitiesA, new Filter<AgentCompatibility>() {
      public boolean accept(@NotNull AgentCompatibility data) {
        return btOnlyA.contains(data.getBuildType());
      }
    }));
    final Map<SProject, List<AgentCompatibility>> mapOnlyB = getCompatibleByProject(true, CollectionsUtil.filterCollection(compatibilitiesB, new Filter<AgentCompatibility>() {
      public boolean accept(@NotNull AgentCompatibility data) {
        return btOnlyB.contains(data.getBuildType());
      }
    }));

    final Map<SProject, List<AgentCompatibility>> mapMissingA = getCompatibleByProject(false, CollectionsUtil.filterCollection(compatibilitiesA, new Filter<AgentCompatibility>() {
      public boolean accept(@NotNull AgentCompatibility data) {
        return btMissingA.contains(data.getBuildType());
      }
    }));
    final Map<SProject, List<AgentCompatibility>> mapMissingB = getCompatibleByProject(false, CollectionsUtil.filterCollection(compatibilitiesB, new Filter<AgentCompatibility>() {
      public boolean accept(@NotNull AgentCompatibility data) {
        return btMissingB.contains(data.getBuildType());
      }
    }));

    return new CompatibleConfigurationsDiff(mapOnlyA, mapOnlyB, mapMissingA, mapMissingB, formA, formB);
  }

  private AgentDetailsFormBase getAgentDetailsForm(AgentDescription agent, AgentDetailsFormFactory factory) {
    if (agent instanceof SBuildAgent) {
      return factory.createAgentDetailsForm((SBuildAgent) agent);
    } else if (agent instanceof SAgentType) {
      return factory.createAgentTypeDetailsForm((SAgentType) agent);
    }
    LOG.warn("Unsupported subtype '" + agent.getClass().getName() + "' of AgentDescription provided: " + agent.toString());
    return null;
  }

  private List<SBuildType> getCompatibleBuildTypes(List<AgentCompatibility> compatibilitiesA) {
    return CollectionsUtil.convertCollection(CollectionsUtil.filterCollection(compatibilitiesA, new Filter<AgentCompatibility>() {
      public boolean accept(@NotNull AgentCompatibility data) {
        return data.isActive() && data.isCompatible(); // isActive will filter by agent pool compatibility
      }
    }), new Converter<SBuildType, AgentCompatibility>() {
      public SBuildType createFrom(@NotNull AgentCompatibility source) {
        return source.getBuildType();
      }
    });
  }

  @NotNull
  private Map<SProject, List<AgentCompatibility>> getCompatibleByProject(boolean compatible, List<AgentCompatibility> myCompatibilities) {
    Map<SProject, List<AgentCompatibility>> result = new TreeMap<SProject, List<AgentCompatibility>>(new ProjectComparator());
    for (AgentCompatibility compatibility : myCompatibilities) {
      if (compatibility.isCompatible() != compatible) {
        continue;
      }

      SProject project = compatibility.getBuildType().getProject();
      List<AgentCompatibility> compatibilities = result.get(project);
      if (compatibilities == null) {
        compatibilities = new ArrayList<AgentCompatibility>();
        result.put(project, compatibilities);
      }
      compatibilities.add(compatibility);
    }
    return result;
  }
}
