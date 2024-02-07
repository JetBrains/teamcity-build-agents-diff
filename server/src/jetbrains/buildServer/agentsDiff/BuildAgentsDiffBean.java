

package jetbrains.buildServer.agentsDiff;

import jetbrains.buildServer.BuildAgent;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.SBuildAgent;
import jetbrains.buildServer.serverSide.agentTypes.AgentType;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffBean {
  private final AgentDescription myAgentA;
  private final AgentDescription myAgentB;
  private final Collection<BuildAgentsDiffEntry> myEntries;

  public BuildAgentsDiffBean(AgentDescription agentA,
                             AgentDescription agentB,
                             Collection<BuildAgentsDiffEntry> entries) {
    myAgentA = agentA;
    myAgentB = agentB;
    myEntries = entries;
  }

  public Collection<BuildAgentsDiffEntry> getEntries(){
    return myEntries;
  }

  public AgentDescription getDescriptionA() {
    return myAgentA;
  }

  public AgentDescription getDescriptionB() {
    return myAgentB;
  }

  public SBuildAgent getAgentA() {
    return myAgentA instanceof SBuildAgent ? (SBuildAgent) myAgentA : null;
  }

  public SBuildAgent getAgentB() {
    return myAgentB instanceof SBuildAgent ? (SBuildAgent) myAgentB : null;
  }

  public String getIdA() {
    return String.valueOf(myAgentA instanceof BuildAgent ? ((BuildAgent) myAgentA).getId() : (((AgentType) myAgentA)).getAgentTypeId());
  }

  public String getIdB() {
    return String.valueOf(myAgentB instanceof BuildAgent ? ((BuildAgent) myAgentB).getId() : (((AgentType) myAgentB)).getAgentTypeId());
  }

  public static BuildAgentsDiffBean empty() {
    return new BuildAgentsDiffBean(null, null, Collections.emptyList());
  }
}