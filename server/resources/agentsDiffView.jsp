<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="agent" tagdir="/WEB-INF/tags/agent" %>
<jsp:useBean id="diff" type="jetbrains.buildServer.agentsDiff.BuildAgentsDiffBean" scope="request"/>
<jsp:useBean id="diffPermalink" type="java.lang.String" scope="request"/>

<c:choose>
  <c:when test="${diff.descriptionA == null || diff.descriptionB == null}">
    <span>Please choose build agents to compare.</span>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${not empty diff.entries}">
        <c:set var="diffPermalinkFullUrl"><c:url value="${diffPermalink}"/></c:set>
        <a style="float: right" href="${diffPermalinkFullUrl}">Permalink</a>
        <table id="agentsDiffTable" class="diffTable">
          <th>Agent Parameter</th>
          <th>
            <c:choose>
              <c:when test="${diff.agentA != null}"> <bs:agentDetailsLink agent="${diff.agentA}"/> </c:when>
              <c:otherwise> <bs:agentDetailsLink agentTypeId="${diff.idA}"/> </c:otherwise>
            </c:choose>
          </th>
          <th>
            <c:choose>
              <c:when test="${diff.agentB != null}"> <bs:agentDetailsLink agent="${diff.agentB}"/> </c:when>
              <c:otherwise> <bs:agentDetailsLink agentTypeId="${diff.idB}"/> </c:otherwise>
            </c:choose>
          </th>
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
        Build agents parameters are identical.
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${diff.configurations.different}">
        <p>Incompatible build configurations:</p>
        <table id="agentsConfigurationsDiffTable" class="configurations">
        <tr>
          <th>
            <c:choose>
              <c:when test="${diff.agentA != null}"> <bs:agentDetailsLink agent="${diff.agentA}"/> </c:when>
              <c:otherwise> <bs:agentDetailsLink agentTypeId="${diff.idA}"/> </c:otherwise>
            </c:choose>
          </th>
          <th>
            <c:choose>
              <c:when test="${diff.agentB != null}"> <bs:agentDetailsLink agent="${diff.agentB}"/> </c:when>
              <c:otherwise> <bs:agentDetailsLink agentTypeId="${diff.idB}"/> </c:otherwise>
            </c:choose>
          </th>
        </tr>
        <tr>
          <td>
            <c:set scope="request" var="agentDetails" value="${diff.configurations.formA}"/>
            <c:forEach items="${diff.configurations.missingA}" var="entry2">
              <agent:projectCompatibleEntries project="${entry2.key}"
                                              compatibilities="${entry2.value}"
                                              compatible="true"/>
            </c:forEach>
          </td>
          <td>
            <c:set var="agentDetails" value="${diff.configurations.formB}"/>
            <c:forEach items="${diff.configurations.missingB}" var="entry2">
              <agent:projectCompatibleEntries project="${entry2.key}"
                                              compatibilities="${entry2.value}"
                                              compatible="false"/>
            </c:forEach>
          </td>
        </tr>
      </c:when>
      <c:otherwise>
        Build agents compatible configurations are identical.
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>