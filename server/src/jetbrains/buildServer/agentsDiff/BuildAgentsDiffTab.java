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
import jetbrains.buildServer.serverSide.BuildAgentEx;
import jetbrains.buildServer.serverSide.BuildAgentManagerEx;
import jetbrains.buildServer.serverSide.agentTypes.AgentType;
import jetbrains.buildServer.serverSide.agentTypes.AgentTypeStorage;
import jetbrains.buildServer.serverSide.agentTypes.SAgentType;
import jetbrains.buildServer.util.Converter;
import jetbrains.buildServer.util.filters.Filter;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimpleCustomTab;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static jetbrains.buildServer.util.CollectionsUtil.*;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffTab extends SimpleCustomTab {

  private final BuildAgentManagerEx myBuildAgentManager;
  private final AgentTypeStorage myAgentTypeStorage;

  public BuildAgentsDiffTab(@NotNull PagePlaces pagePlaces,
                            @NotNull PluginDescriptor pluginDescriptor,
                            @NotNull AgentTypeStorage agentTypeStorage,
                            @NotNull BuildAgentManagerEx buildAgentManager) {
    super(pagePlaces, PlaceId.AGENTS_TAB, "diff", pluginDescriptor.getPluginResourcesPath("agentsDiffTab.jsp"), "Diff");
    myBuildAgentManager = buildAgentManager;
    myAgentTypeStorage = agentTypeStorage;
    addCssFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.css"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.js"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("libs/diff_match_patch.js"));
    register();
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);
    List<BuildAgentEx> authorized = myBuildAgentManager.getAllAgents(false);
    model.put("allAgents", authorized);

    final Set<Integer> authorizedIds = authorized.stream().map(BuildAgent::getId).collect(Collectors.toSet());
    Collection<BuildAgentEx> unauthorized = myBuildAgentManager.getAllAgents(true)
            .stream()
            .filter(agent -> !authorizedIds.contains(agent.getId()))
            .collect(Collectors.toList());
    model.put("unauthorizedAgents", unauthorized);

    List<AgentType> types = filterCollection(filterNulls(convertCollection(myAgentTypeStorage.getAgentTypeIds(), new Converter<AgentType, Integer>() {
      @Override
      public AgentType createFrom(@NotNull Integer id) {
        return myAgentTypeStorage.findAgentTypeById(id);
      }
    })), new Filter<AgentType>() {
      @Override
      public boolean accept(@NotNull AgentType data) {
        return data instanceof SAgentType && ((SAgentType) data).getRealAgent() == null;

      }
    });
    model.put("cloudAgentTypes", types);
  }
}
