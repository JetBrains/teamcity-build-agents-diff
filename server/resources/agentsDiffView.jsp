<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="diff" type="jetbrains.buildServer.agentsDiff.BuildAgentsDiffBean" scope="request"/>
<jsp:useBean id="diffPermalink" type="java.lang.String" scope="request"/>

<c:choose>
  <c:when test="${diff.agentA == null || diff.agentB == null}">
    <span>Please choose build agents to compare.</span>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${not empty diff.entries}">
        <c:set var="diffPermalinkFullUrl"><c:url value="${diffPermalink}"/></c:set>
        <a style="float: right" href="${diffPermalinkFullUrl}">Permalink</a>
        <table id="agentsDiffTable" class="diffTable">
          <th>Agent Parameter</th>
          <c:set var="agentATypeIdString">${diff.agentA.agentTypeId}</c:set>
          <c:set var="agentBTypeIdString">${diff.agentB.agentTypeId}</c:set>
          <th><bs:agentDetailsLink agentName="${diff.agentA.name}" agentTypeId="${agentATypeIdString}"/></th>
          <th><bs:agentDetailsLink agentName="${diff.agentB.name}" agentTypeId="${agentBTypeIdString}"/></th>
          <c:forEach items="${diff.entries}" var="entry">
            <c:set var="propertyName" value="${entry.propertyName}"/>
            <c:set var="propertyValueA" value="${entry.propertyValueA}"/>
            <c:set var="propertyValueB" value="${entry.propertyValueB}"/>
            <c:set var="valuesDiffer" value="${entry.valuesDiffer}"/>
            <c:choose>
              <c:when test="${valuesDiffer}">
                <tr class="diffRow">
                  <td>${propertyName}</td>
                  <td class="propA">
                    <div class="propValueContainer">${propertyValueA}</div>
                  </td>
                  <td class="propB">
                    <div class="propValueContainer">${propertyValueB}</div>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <tr class="diffRow">
                  <td bgcolor="#e6ffe6">${propertyName}</td>
                  <td>
                    <div class="propValueContainer">${propertyValueA}</div>
                  </td>
                  <td>
                    <div class="propValueContainer">${propertyValueB}</div>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </table>
        <script type="application/javascript">
          BS.AgentsDiff.colorize();
        </script>
      </c:when>
      <c:otherwise>
        Build agents are identical.
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>