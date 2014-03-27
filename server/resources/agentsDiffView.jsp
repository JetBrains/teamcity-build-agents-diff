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
      <tr class="diffRow">
        <td>${propertyName}</td>
        <td class="propA">${propertyValueA}</td>
        <td class="propB">${propertyValueB}</td>
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