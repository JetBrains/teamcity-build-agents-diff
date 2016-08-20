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
 * http://127.0.0.1:8111/agents/diffView.html?buildId=4&buildTypeId=Test_ABuildType&buildIdB=4
 */
public class BuildAgentsDiffTab extends SimpleCustomTab {

  private final BuildsManager myBuildManager;
  private final BuildHistoryEx myBuildHistory;

  public BuildAgentsDiffTab(@NotNull PagePlaces pagePlaces,
                            @NotNull PluginDescriptor pluginDescriptor,
                            @NotNull BuildsManager buildManager,
                            @NotNull BuildHistoryEx buildHistory) {
    //          Proect -> Build_conf_tab ->

    super(pagePlaces, PlaceId.BUILD_RESULTS_TAB, "diff", pluginDescriptor.getPluginResourcesPath("agentsDiffTab.jsp"), "Diff");
    myBuildManager = buildManager;
    myBuildHistory = buildHistory;
   // buildFinder
    addCssFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.css"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("agentsDiff.js"));
    addJsFile(pluginDescriptor.getPluginResourcesPath("libs/diff_match_patch.js"));
    register();
  }

  @Override
  public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
    super.fillModel(model, request);

    //BuildQueryOptions options = new BuildQueryOptions();



    String buildNumber = request.getParameter("buildId");
    String buildTypeExternalId = request.getParameter("buildTypeId");

    //myBuildManager.processBuilds(options, x.Builds);
    java.util.List builds = new ArrayList();
    builds = myBuildHistory.getEntries(true);

    //BuildFinder finder = new BuildFinder();
    BuildQueryOptions options = new BuildQueryOptions();
    options.setIncludeRunning(true);
    options.setMatchAllBranches(true);
    //options



/*    myBuildManager.processBuilds(options, finder);

    SBuild test_aBuildType = myBuildManager.findBuildInstanceByBuildNumber("Test_ABuildType", "1");
    builds.add(test_aBuildType);
    builds.add("testyme");*/
    System.out.println("Hello BOB");
    ///System.out.println(test_aBuildType);

    //logger.warn("buildid a is ." + buildAIdString + ".");

   // builds.addAll(finder.buildList);
   // builds.add(
//    myBuildManager.findBuildInstanceById(  ).name
    model.put("allBuilds", builds);
  }
}
/*
class BuildFinder implements ItemProcessor<SBuild> {
  public java.util.List<SBuild> buildList = new java.util.List<SBuild>();

  public boolean processItem(SBuild item) {

    buildList.add(item);

    return true;
  }
}*/