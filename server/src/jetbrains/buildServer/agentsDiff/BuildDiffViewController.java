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
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildDiffViewController extends BaseFormXmlController {

  private static final String AGENTS_DIFF_VIEW_HTML = "/builds/diffView.html**";

  @NotNull private final PluginDescriptor myPluginDescriptor;
  @NotNull private final BuildHistory myBuildHistory;
  @NotNull private final BuildDiffCalculator myDiffCalculator = new BuildDiffCalculator();

  public BuildDiffViewController(@NotNull SBuildServer server,
                                       @NotNull PluginDescriptor pluginDescriptor,
                                       @NotNull WebControllerManager webControllerManager,
                                       @NotNull BuildHistoryEx buildHistory) {
    super(server);
    myBuildHistory = buildHistory;
    myPluginDescriptor = pluginDescriptor;
    webControllerManager.registerController(AGENTS_DIFF_VIEW_HTML, this);
  }

  @Override
  protected ModelAndView doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
    final ModelAndView view = new ModelAndView(myPluginDescriptor.getPluginResourcesPath("buildDiffView.jsp"));
    BuildDiffBean diff = BuildDiffBean.empty();

    final String buildNumberA = request.getParameter("buildId");
    final String buildNumberB = request.getParameter("buildIdB");
    final String externalBuildTypeA = request.getParameter("buildTypeId");
    final String externalBuildTypeB = request.getParameter("buildTypeIdB");
    if(!buildNumberA.isEmpty() && !buildNumberB.isEmpty()){

      java.util.List<SFinishedBuild> x = myBuildHistory.getEntries(true);

      SFinishedBuild buildA = findBuild(buildNumberA, externalBuildTypeA, x);
      SFinishedBuild buildB = findBuild(buildNumberB, externalBuildTypeB, x);

      if(buildA != null && buildB != null) {
        diff = myDiffCalculator.calculateDiff(buildA, buildB);
      }
    }

    view.getModel().put("diff", diff);
    return view;
  }

  private SFinishedBuild findBuild(String buildAIdString, String buildAIdTypeString, List<SFinishedBuild> x) {
    for (SFinishedBuild build : x) {
      if (buildAIdString.equals(build.getBuildNumber()))
      {
        if (buildAIdTypeString.equals(build.getBuildTypeExternalId()))
        {
          return build;
        }
      }
    }
    return null;
  }

  @Override
  protected void doPost(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Element xmlResponse) {
  }
}
