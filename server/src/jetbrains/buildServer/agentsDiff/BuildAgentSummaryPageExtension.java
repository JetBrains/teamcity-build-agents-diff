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

import jetbrains.buildServer.serverSide.BuildAgentManager;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import jetbrains.buildServer.web.openapi.agent.AgentFinderUtil;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentSummaryPageExtension extends SimplePageExtension {
  @NotNull private BuildAgentManager myAgentManager;

  public BuildAgentSummaryPageExtension(@NotNull PagePlaces pagePlaces,
                                        @NotNull PluginDescriptor pluginDescriptor,
                                        @NotNull BuildAgentManager agentManager) {
    super(pagePlaces, PlaceId.AGENT_SUMMARY, "compareWith", pluginDescriptor.getPluginResourcesPath("compareWith.jsp"));
    myAgentManager = agentManager;
    addCssFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.css"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.js"));
    register();
  }

  @Override
  public final boolean isAvailable(@NotNull final HttpServletRequest request) {
    return getAgent(request) != null;
  }

  @Override
  public final void fillModel(@NotNull final Map<String, Object> model, @NotNull final HttpServletRequest request) {
    SBuildAgent agent = getAgent(request);
    if (agent != null) {
      model.put("diffUrl", "agents.html?tab=diff#agentA=" + agent.getId());
    }
  }

  private SBuildAgent getAgent(final HttpServletRequest request) {
    return AgentFinderUtil.findAgent(request, myAgentManager);
  }
}
