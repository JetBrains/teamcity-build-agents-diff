/*
 * Copyright 2000-2019 JetBrains s.r.o.
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