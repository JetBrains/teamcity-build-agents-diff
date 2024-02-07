

package jetbrains.buildServer.agentsDiff;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.BuildAgentManagerEx;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.agentTypes.AgentTypeStorage;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffViewController extends BaseController {

  private static final String AGENTS_DIFF_VIEW_HTML = "/agents/diffView.html**";
  public static final String TYPE_PREFIX = "type-";

  @NotNull private final PluginDescriptor myPluginDescriptor;
  @NotNull private final BuildAgentManagerEx myBuildAgentManager;
  @NotNull
  private final AgentTypeStorage myAgentTypeStorage;
  @NotNull private final BuildAgentsDiffCalculator myDiffCalculator = new BuildAgentsDiffCalculator();

  public BuildAgentsDiffViewController(@NotNull SBuildServer server,
                                       @NotNull PluginDescriptor pluginDescriptor,
                                       @NotNull WebControllerManager webControllerManager,
                                       @NotNull AgentTypeStorage agentTypeStorage,
                                       @NotNull BuildAgentManagerEx buildAgentManager) {
    super(server);
    myPluginDescriptor = pluginDescriptor;
    myAgentTypeStorage = agentTypeStorage;
    myBuildAgentManager = buildAgentManager;
    webControllerManager.registerController(AGENTS_DIFF_VIEW_HTML, this);
  }

  @Nullable
  @Override
  protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
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
        return myAgentTypeStorage.findAgentTypeById(id);
      }
      int id = Integer.parseInt(input);
      return myBuildAgentManager.findAgentById(id, true);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}