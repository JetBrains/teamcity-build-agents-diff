
package jetbrains.buildServer.agentsDiff;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

public class BuildAgentsDiffCalculatorTest {
  @Test
  public void testSimplifyParameters() {
    Map<String, String> params = new HashMap<>();
    params.put("teamcity.agent.tools.dir", "/teamcity-agent/tools");
    params.put("teamcity.tool.maven", "/teamcity-agent/tools/maven");
    params.put("teamcity.tool.nuget", "/teamcity-agent/tools/nuget");
    params.put("teamcity.agent.home.dir", "/teamcity-agent");

    then(BuildAgentsDiffCalculator.simplifyParameters(params))
            .containsValue("%teamcity.agent.tools.dir%/maven")
            .containsValue("%teamcity.agent.tools.dir%/nuget")
            .containsValue("%teamcity.agent.home.dir%/tools")
            .containsValue("/teamcity-agent")
    ;
  }
}