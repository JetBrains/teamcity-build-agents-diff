

package jetbrains.buildServer.agentsDiff;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Evgeniy.Koshkin.
 */
class BuildAgentsDiffUtils {
  @NotNull
  static String getDiffPermalink(@NotNull String agentAId, @Nullable String agentBId) {
    String diffPermalink = "/agents.html?tab=diff#agentA=" + agentAId;
    if (agentBId != null) {
      diffPermalink += "&agentB=" + agentBId;
    }
    return diffPermalink;
  }
}