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

import jetbrains.buildServer.agent.Constants;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.BuildAgentEx;
import jetbrains.buildServer.serverSide.BuildAgentManagerEx;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.filters.Filter;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffViewController extends BaseFormXmlController {

  @NotNull private final PluginDescriptor myPluginDescriptor;
  @NotNull private final BuildAgentManagerEx myBuildAgentManager;

  public BuildAgentsDiffViewController(@NotNull SBuildServer server,
                                       @NotNull PluginDescriptor pluginDescriptor,
                                       @NotNull WebControllerManager webControllerManager,
                                       @NotNull BuildAgentManagerEx buildAgentManager) {
    super(server);
    myPluginDescriptor = pluginDescriptor;
    myBuildAgentManager = buildAgentManager;
    webControllerManager.registerController("/agents/diffView.html**", this);
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    int agentAId = Integer.parseInt(request.getParameter("agentA"));
    int agentBId = Integer.parseInt(request.getParameter("agentB"));
    final BuildAgentEx agentA = myBuildAgentManager.findAgentById(agentAId, true);
    final BuildAgentEx agentB = myBuildAgentManager.findAgentById(agentBId, true);
    if(agentA == null || agentB == null) return null;
    final ModelAndView view = new ModelAndView(myPluginDescriptor.getPluginResourcesPath("/agentsDiffView.jsp"));
    final Map<String,Object> model = view.getModel();
    dumpSystemPropDiff(model, agentA, agentB);
    dumpEnvVarDiff(model, agentA, agentB);
    dumpConfigParamDiff(model, agentA, agentB);
    model.put("agentA", agentA.getName());
    model.put("agentB", agentB.getName());
    return view;
  }

  private void dumpSystemPropDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAConfigParams = CollectionsUtil.filterMapByKeys(agentA.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.SYSTEM_PREFIX);
      }
    });
    final Map<String, String> agentBConfigParams = CollectionsUtil.filterMapByKeys(agentB.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.SYSTEM_PREFIX);
      }
    });
    model.put("agentAConfigParams", agentAConfigParams);
    model.put("agentBConfigParams", agentBConfigParams);
  }

  private void dumpEnvVarDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAEnvVars = CollectionsUtil.filterMapByKeys(agentA.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.ENV_PREFIX);
      }
    });
    final Map<String, String> agentBEnvVars = CollectionsUtil.filterMapByKeys(agentB.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.ENV_PREFIX);
      }
    });
    model.put("agentAEnvVars", agentAEnvVars);
    model.put("agentBEnvVars", agentBEnvVars);
  }

  private void dumpConfigParamDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAConfigParams = agentA.getConfigurationParameters();
    final Map<String, String> agentBConfigParams = agentB.getConfigurationParameters();
    model.put("agentAConfigParams", agentAConfigParams);
    model.put("agentBConfigParams", agentBConfigParams);
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
  }
}
