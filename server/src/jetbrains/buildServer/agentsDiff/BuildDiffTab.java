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

import jetbrains.buildServer.agentServer.Server;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.serverSide.impl.audit.finders.BuildTypeFinder;
import jetbrains.buildServer.util.ItemProcessor;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimpleCustomTab;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 *
 * Choose a build to compare with and then progress to this page:
 * /agents/diffView.html?buildId=4&buildTypeId=Test_ABuildType&buildIdB=4
 */
public class BuildDiffTab extends SimpleCustomTab {

  private final BuildHistoryEx myBuildHistory;

  public BuildDiffTab(@NotNull PagePlaces pagePlaces,
                            @NotNull PluginDescriptor pluginDescriptor,
                            @NotNull BuildHistoryEx buildHistory) {

    super(pagePlaces, PlaceId.BUILD_RESULTS_TAB, "diff", pluginDescriptor.getPluginResourcesPath("buildDiffTab.jsp"), "Diff");
    myBuildHistory = buildHistory;
    addCssFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.css"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.js"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("libs/diff_match_patch.js"));
    register();
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);

    String buildTypeExternalId = request.getParameter("buildTypeId");
    String buildTypeExternalIdB = request.getParameter("buildTypeIdB");

    if (buildTypeExternalIdB == null)
    {
      buildTypeExternalIdB = buildTypeExternalId;
    }

    java.util.List builds = myBuildHistory.getEntries(true);

    java.util.List buildTypes = new java.util.ArrayList();

    //Todo: this should be a one liner...
    for (Object build: builds) {
      if (!buildTypes.contains(((SBuild)build).getBuildTypeExternalId()))
      {
        buildTypes.add(((SBuild) build).getBuildTypeExternalId());
      }
    }

    java.util.List availableBuilds = new java.util.ArrayList();

    String all = request.getParameter("all");
    if (all != null && !all.toLowerCase().equals("false")) {
      availableBuilds = builds;
    } else {
      for (Object build : builds) {
        if (buildTypeExternalIdB.equals(((SBuild) build).getBuildTypeExternalId())) {
          availableBuilds.add(build);
        }
      }
    }

    model.put("allBuilds", availableBuilds);
    model.put("allBuildTypes", buildTypes);
  }
}