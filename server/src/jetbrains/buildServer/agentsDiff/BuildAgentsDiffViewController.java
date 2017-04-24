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
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.BuildAgentManagerEx;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.agentTypes.AgentTypeManager;
import jetbrains.buildServer.util.StringUtil;
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
  public static final String TYPE_PREFIX = "type-";

  @NotNull private final PluginDescriptor myPluginDescriptor;
  @NotNull private final BuildAgentManagerEx myBuildAgentManager;
  @NotNull
  private final AgentTypeManager myAgentTypeManager;
  @NotNull private final BuildAgentsDiffCalculator myDiffCalculator = new BuildAgentsDiffCalculator();

  public BuildAgentsDiffViewController(@NotNull SBuildServer server,
                                       @NotNull PluginDescriptor pluginDescriptor,
                                       @NotNull WebControllerManager webControllerManager,
                                       @NotNull AgentTypeManager agentTypeManager,
                                       @NotNull BuildAgentManagerEx buildAgentManager) {
    super(server);
    myPluginDescriptor = pluginDescriptor;
    myAgentTypeManager = agentTypeManager;
    myBuildAgentManager = buildAgentManager;
    webControllerManager.registerController(AGENTS_DIFF_VIEW_HTML, this);
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    final ModelAndView view = new ModelAndView(myPluginDescriptor.getPluginResourcesPath("agentsDiffView.jsp"));
    BuildAgentsDiffBean diff = BuildAgentsDiffBean.empty();
    String diffPermalink = "";

    final String agentAId = request.getParameter("agentA");
    final String agentBId = request.getParameter("agentB");

    final AgentDescription agentA = getAgentDescription(agentAId);
    final AgentDescription agentB = getAgentDescription(agentBId);
    if (agentA != null && agentB != null) {
      diff = myDiffCalculator.calculateDiff(agentA, agentB);
      diffPermalink = BuildAgentsDiffUtils.getDiffPermalink(agentAId, agentBId);
    }

    view.getModel().put("diff", diff);
    view.getModel().put("diffPermalink", diffPermalink);
    return view;
  }

  private AgentDescription getAgentDescription(String input) {
    if (StringUtil.isEmptyOrSpaces(input)) return null;
    try {
      if (input.startsWith(TYPE_PREFIX)) {
        int id = Integer.parseInt(input.substring(TYPE_PREFIX.length()));
        return myAgentTypeManager.findAgentTypeById(id);
      }
      int id = Integer.parseInt(input);
      return myBuildAgentManager.findAgentById(id, true);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
  }
}
