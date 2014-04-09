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
    BS.ajaxUpdater($('agentsDiffView'), url + "?agentA=" + agentAId + "&agentB=" + agentBId, {
      method: 'get',
      evalScripts: true
    });
    return false;
  },

  colorize: function() {
    var dmp = new diff_match_patch();
    $j("tr.diffRow").each(function() {
      var propACell = $j(this).children(".propA").first();
      var propBCell = $j(this).children(".propB").first();
      if(propACell && propBCell){
        var textA = propACell.text();
        var textB = propBCell.text();
        var diffABHtml = BS.AgentsDiff.diff2Html(dmp.diff_main(textA, textB));
        var diffBAHtml = BS.AgentsDiff.diff2Html(dmp.diff_main(textB, textA));
        propACell.html(diffABHtml);
        propBCell.html(diffBAHtml);
      }
    });
  },

  diff2Html: function(diffs) {
    var html = [];
    var pattern_amp = /&/g;
    var pattern_lt = /</g;
    var pattern_gt = />/g;
    var pattern_para = /\n/g;
    for (var x = 0; x < diffs.length; x++) {
      var op = diffs[x][0];    // Operation (insert, delete, equal)
      var data = diffs[x][1];  // Text of change.
      var text = data.replace(pattern_amp, '&amp;').replace(pattern_lt, '&lt;')
              .replace(pattern_gt, '&gt;').replace(pattern_para, '&para;<br>');
      switch (op) {
        case DIFF_INSERT:
          html[x] = '<span style="background:#9DCBFA;">' + text + '</span>';
          break;
        case DIFF_EQUAL:
          html[x] = '<span>' + text + '</span>';
          break;
      }
    }
    return html.join('');
  }
};