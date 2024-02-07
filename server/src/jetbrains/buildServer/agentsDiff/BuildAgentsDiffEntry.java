

package jetbrains.buildServer.agentsDiff;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffEntry {
  private static final String VALUE_NOT_DEFINED_MARK = "---";

  private final BuildAgentsDiffEntryType myType;
  private final String myPropertyName;
  private final String myPropertyValueA;
  private final String myPropertyValueB;

  public BuildAgentsDiffEntry(BuildAgentsDiffEntryType type, String propertyName, String propertyValueA, String propertyValueB) {
    myType = type;
    myPropertyName = propertyName;
    myPropertyValueA = propertyValueA;
    myPropertyValueB = propertyValueB;
  }

  public BuildAgentsDiffEntryType getType() {
    return myType;
  }

  public String getPropertyName() {
    return myPropertyName;
  }

  public String getPropertyValueA() {
    return myPropertyValueA == null ? VALUE_NOT_DEFINED_MARK : myPropertyValueA;
  }

  public String getPropertyValueB() {
    return myPropertyValueB == null ? VALUE_NOT_DEFINED_MARK : myPropertyValueB;
  }

  public Boolean getValuesDiffer() {
    return myType == BuildAgentsDiffEntryType.PARAMETER_VALUE;
  }
}