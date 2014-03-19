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

import jetbrains.buildServer.agent.Constants;
import jetbrains.buildServer.controllers.changes.DiffViewUtil;
import jetbrains.buildServer.serverSide.BuildAgentEx;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.filters.Filter;
import jetbrains.buildServer.vcs.Modification;
import jetbrains.buildServer.vcs.VcsException;
import jetbrains.buildServer.vcs.VcsFileModification;
import jetbrains.buildServer.vcs.VcsModification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeniy.Koshkin
 */
public class BuildAgentsDiffUtil {

  public void fillModel(ModelAndView view, HttpServletRequest request, BuildAgentEx agentA, BuildAgentEx agentB) {
    Modification modification = new Foo();
    VcsFileModification change = new Boo();
    final Map<String, Object> model = DiffViewUtil.fillDiffModel(view, modification, change, request);
    model.put("contentInlined", true);
  }

  private class Foo implements VcsModification {
    @Nullable
    public String getUserName() {
      return "user name";
    }

    @NotNull
    public String getDescription() {
      return "description";
    }

    @NotNull
    public Date getVcsDate() {
      return new Date();
    }

    public long getId() {
      return -1;
    }

    public boolean isPersonal() {
      return false;
    }

    @NotNull
    public List<VcsFileModification> getChanges() {
      return Collections.emptyList();
    }

    @Nullable
    public VcsFileModification findChangeByPath(String fileName) {
      return null;
    }

    public int getChangeCount() {
      return 0;
    }

    @NotNull
    public String getVersion() {
      return "version";
    }

    public String getDisplayVersion() {
      return "version";
    }

    public String getVersionControlName() {
      return "version";
    }

    public int compareTo(VcsModification o) {
      return 0;
    }
  }

  private class Boo implements VcsFileModification {
    public byte[] getContentBefore() throws VcsException {
      return new byte[]{};
    }

    public byte[] getContentAfter() throws VcsException {
      return new byte[]{};
    }

    @Nullable
    public String getChangeTypeName() {
      return getType().getDescription().intern();
    }

    @NotNull
    public String getFileName() {
      return "file name";
    }

    @NotNull
    public String getRelativeFileName() {
      return "relative file name";
    }

    @NotNull
    public Type getType() {
      return Type.CHANGED;
    }

    @Nullable
    public String getBeforeChangeRevisionNumber() {
      return "";
    }

    @Nullable
    public String getAfterChangeRevisionNumber() {
      return "";
    }
  }

  private void dumpSystemPropDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAConfigParams = CollectionsUtil.filterMapByKeys(agentA.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.SYSTEM_PREFIX);
      }
    });
    final Map<String, String> agentBConfigParams = CollectionsUtil.filterMapByKeys(agentB.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.SYSTEM_PREFIX);
      }
    });
    model.put("agentAConfigParams", agentAConfigParams);
    model.put("agentBConfigParams", agentBConfigParams);
  }

  private void dumpEnvVarDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAEnvVars = CollectionsUtil.filterMapByKeys(agentA.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.ENV_PREFIX);
      }
    });
    final Map<String, String> agentBEnvVars = CollectionsUtil.filterMapByKeys(agentB.getBuildParameters(), new Filter<String>() {
      public boolean accept(@NotNull String data) {
        return data.startsWith(Constants.ENV_PREFIX);
      }
    });
    model.put("agentAEnvVars", agentAEnvVars);
    model.put("agentBEnvVars", agentBEnvVars);
  }

  private void dumpConfigParamDiff(Map<String, Object> model, BuildAgentEx agentA, BuildAgentEx agentB) {
    final Map<String, String> agentAConfigParams = agentA.getConfigurationParameters();
    final Map<String, String> agentBConfigParams = agentB.getConfigurationParameters();
    model.put("agentAConfigParams", agentAConfigParams);
    model.put("agentBConfigParams", agentBConfigParams);
  }
}
