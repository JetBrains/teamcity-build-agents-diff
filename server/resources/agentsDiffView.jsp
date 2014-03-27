<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="diff" type="jetbrains.buildServer.agentsDiff.BuildAgentsDiffBean" scope="request"/>

<c:choose>
  <c:when test="${not empty diff.entries}">
    <table>
    <c:forEach items="${diff.entries}" var="entry">
      <c:set var="entryType" value="${entry.type}"/>
      <c:set var="propertyName" value="${entry.propertyName}"/>
      <c:set var="propertyValueA" value="${entry.propertyValueA}"/>
      <c:set var="propertyValueB" value="${entry.propertyValueB}"/>
      <tr class="${entryType.cssClass}">
        <td>${propertyName}</td>
        <td>${propertyValueA}</td>
        <td>${propertyValueB}</td>
      </tr>
    </c:forEach>
    </table>
  </c:when>
  <c:otherwise>
    Build agents are identical.
  </c:otherwise>
</c:choose>