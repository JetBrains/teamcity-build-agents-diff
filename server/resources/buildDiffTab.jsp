<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="allBuilds" type="java.util.List" scope="request"/>
<jsp:useBean id="allBuildTypes" type="java.util.List" scope="request"/>

<c:set var="ajaxUrl"><c:url value="/builds/diffView.html?buildId=${param.buildId}&buildTypeId=${param.buildTypeId}"/></c:set>

<c:choose>
  <c:when test="${empty allBuilds}">
    <span>Nothing to compare.</span>
  </c:when>
  <c:otherwise>
    <div class="actionBar">
      <label>Compare with build type:</label>

      <forms:select name="buildTypeBSelection" id="buildTypeBSelection" style="width: 20em" enableFilter="true" onchange="return BS.AgentsDiff.updateDiff('${ajaxUrl}');">
        <c:choose>
          <c:when test="${empty param.buildTypeIdB}">
            <forms:option value="${param.buildTypeId}">${param.buildTypeId}</forms:option>
          </c:when>
          <c:otherwise>
            <forms:option value="${param.buildTypeIdB}">${param.buildTypeIdB}</forms:option>
          </c:otherwise>
        </c:choose>

        <c:forEach items="${allBuildTypes}" var="agentA">
          <c:set var="agentAId" value="${agentA}"/>

          <forms:option value="${agentA}">
            <c:out value="${agentA}"/>
          </forms:option>
        </c:forEach>
      </forms:select>

      <label>build:</label>
      <forms:select name="buildBSelection" id="buildBSelection" style="width: 20em" enableFilter="true" onchange="return BS.AgentsDiff.updateDiff('${ajaxUrl}');">
        <forms:option value="">-- Select build --</forms:option>
        <c:forEach items="${allBuilds}" var="agentA">
          <c:set var="agentAId" value="${agentA}"/>

          <forms:option value="${agentA.buildNumber}">
            <c:out value="${agentA.buildNumber}"/>
          </forms:option>
        </c:forEach>
      </forms:select>
    </div>

    <div id="agentsDiffView"></div>

    <script type="application/javascript">
      BS.AgentsDiff.chooseAgents();
      BS.AgentsDiff.updateDiff('${ajaxUrl}');

    </script>

  </c:otherwise>
</c:choose>