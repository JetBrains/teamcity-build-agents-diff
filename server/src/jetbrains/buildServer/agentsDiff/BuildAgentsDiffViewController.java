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

import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.serverSide.BuildAgentEx;
import jetbrains.buildServer.serverSide.BuildAgentManagerEx;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffViewController extends BaseFormXmlController {

  private static final String AGENTS_DIFF_VIEW_HTML = "/agents/diffView.html**";

  @NotNull private final PluginDescriptor myPluginDescriptor;
  @NotNull private final BuildAgentManagerEx myBuildAgentManager;
  @NotNull private final BuildAgentsDiffCalculator myDiffCalculator = new BuildAgentsDiffCalculator();

  public BuildAgentsDiffViewController(@NotNull SBuildServer server,
                                       @NotNull PluginDescriptor pluginDescriptor,
                                       @NotNull WebControllerManager webControllerManager,
                                       @NotNull BuildAgentManagerEx buildAgentManager) {
    super(server);
    myPluginDescriptor = pluginDescriptor;
    myBuildAgentManager = buildAgentManager;
    webControllerManager.registerController(AGENTS_DIFF_VIEW_HTML, this);
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    final ModelAndView view = new ModelAndView(myPluginDescriptor.getPluginResourcesPath("agentsDiffView.jsp"));
    BuildAgentsDiffBean diff = BuildAgentsDiffBean.empty();

    final String agentAIdString = request.getParameter("agentA");
    final String agentBIdString = request.getParameter("agentB");
    if(!agentAIdString.isEmpty() && !agentBIdString.isEmpty()){
      int agentAId = Integer.parseInt(agentAIdString);
      int agentBId = Integer.parseInt(agentBIdString);
      final BuildAgentEx agentA = myBuildAgentManager.findAgentById(agentAId, true);
      final BuildAgentEx agentB = myBuildAgentManager.findAgentById(agentBId, true);
      if(agentA != null && agentB != null) {
        diff = myDiffCalculator.calculateDiff(agentA, agentB);
      }
    }

    view.getModel().put("diff", diff);
    return view;
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
  }
}
