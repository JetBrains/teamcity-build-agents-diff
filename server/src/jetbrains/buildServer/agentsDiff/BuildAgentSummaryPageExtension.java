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

import jetbrains.buildServer.controllers.agent.AgentDetailsFormBase;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.agentTypes.SAgentType;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentSummaryPageExtension extends SimplePageExtension {

  public BuildAgentSummaryPageExtension(@NotNull PagePlaces pagePlaces,
                                        @NotNull PluginDescriptor pluginDescriptor) {
    super(pagePlaces, PlaceId.AGENT_SUMMARY, "compareWith", pluginDescriptor.getPluginResourcesPath("compareWith.jsp"));
    addCssFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.css"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.js"));
    register();
  }

  @Override
  public final boolean isAvailable(@NotNull final HttpServletRequest request) {
    AgentDetailsFormBase form = findForm(request);
    if (form == null) return false;
    AgentDescription agent = form.getAgent();
    return agent instanceof SBuildAgent
            || agent instanceof SAgentType && ((SAgentType) agent).getRealAgent() == null;
  }

  @Override
  public final void fillModel(@NotNull final Map<String, Object> model, @NotNull final HttpServletRequest request) {
    AgentDescription agent = getAgent(request);
    if (agent != null) {
      String id;
      if (agent instanceof SBuildAgent) {
        id = String.valueOf(((SBuildAgent) agent).getId());
      } else if (agent instanceof SAgentType) {
        id = BuildAgentsDiffViewController.TYPE_PREFIX + ((SAgentType) agent).getAgentTypeId();
      } else return;
      model.put("diffUrl", BuildAgentsDiffUtils.getDiffPermalink(id, null));
    }
  }

  private AgentDescription getAgent(final HttpServletRequest request) {
    AgentDetailsFormBase form = findForm(request);
    return form != null ? form.getAgent() : null;
  }

  @Nullable
  private AgentDetailsFormBase findForm(@NotNull HttpServletRequest request) {
    return (AgentDetailsFormBase) request.getAttribute("agentDetails");
  }
}
