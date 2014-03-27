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
}
