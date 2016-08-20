<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="diff" type="jetbrains.buildServer.agentsDiff.BuildAgentsDiffBean" scope="request"/>

    <c:choose>
      <c:when test="${not empty diff.entries}">
        <table id="agentsDiffTable" class="diffTable">
          <th>Build Parameter</th>
          <th>${diff.agentA}</th>
          <th>${diff.agentB}</th>
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
        Builds are identical.
      </c:otherwise>
    </c:choose>
