<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="agentAName" type="java.lang.String" scope="request"/>
<jsp:useBean id="agentBName" type="java.lang.String" scope="request"/>
<jsp:useBean id="diff" type="jetbrains.buildServer.agentsDiff.BuildAgentsDiffBean" scope="request"/>

<c:choose>
  <c:when test="${not empty diff.entries}">
    <table id="agentsDiffTable" class="diffTable">
      <th>Agent Parameter</th>
      <th>${agentAName}</th>
      <th>${agentBName}</th>
    <c:forEach items="${diff.entries}" var="entry">
      <c:set var="propertyName" value="${entry.propertyName}"/>
      <c:set var="propertyValueA" value="${entry.propertyValueA}"/>
      <c:set var="propertyValueB" value="${entry.propertyValueB}"/>
      <c:set var="valuesDiffer" value="${entry.valuesDiffer}"/>
      <tr class="diffRow">
        <td>${propertyName}</td>
        <c:choose>
          <c:when test="${valuesDiffer}">
            <td class="propA">${propertyValueA}</td>
            <td class="propB">${propertyValueB}</td>
          </c:when>
          <c:otherwise>
            <td>${propertyValueA}</td>
            <td>${propertyValueB}</td>
          </c:otherwise>
        </c:choose>
      </tr>
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