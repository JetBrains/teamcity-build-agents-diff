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

/**
 * Created by Evgeniy.Koshkin on 18.03.14.
 */

BS.AgentsDiff = {
  updateDiff: function(url){
    var agentAId = $j('#agentASelection').val();
    var agentBId = $j('#agentBSelection').val();
    if(agentAId.length == 0 || agentBId.length == 0) return false;
    BS.ajaxUpdater($('agentsDiffView'), url + "?agentA=" + agentAId + "&agentB=" + agentBId, { method: 'get' });
    return false;
  }
};