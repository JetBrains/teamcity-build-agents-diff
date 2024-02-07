

package jetbrains.buildServer.agentsDiff;

import org.jetbrains.annotations.NotNull;

/**
 * @author Evgeniy.Koshkin
 */
public enum BuildAgentsDiffEntryType {
  PARAMETER_NAME("parameter_diff"),
  PARAMETER_VALUE("value_diff");

  private String myCssClass;

  BuildAgentsDiffEntryType(String cssClass) {
    myCssClass = cssClass;
  }

  @NotNull
  public String getCssClass() {
    return myCssClass;
  }
}