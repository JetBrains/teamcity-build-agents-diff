<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="allBuilds" type="java.util.List" scope="request"/>
<c:set var="ajaxUrl"><c:url value="/agents/diffView.html"/></c:set>

<c:choose>
  <c:when test="${empty allBuilds}">
    <span>Nothing to compare.</span>
  </c:when>
  <c:otherwise>
    <div class="actionBar">
      <label>Compare with:</label>
      <forms:select name="agentASelection" id="agentASelection" style="width: 20em" enableFilter="true" onchange="return BS.AgentsDiff.updateDiff('${ajaxUrl}');">
        <forms:option value="">-- Select build --</forms:option>
        <c:forEach items="${allBuilds}" var="agentA">
          <c:set var="agentAId" value="${agentA.BuildId}"/>
          <th>${agentA}</th>
          <th>${agentA.BuildId}</th>
          <th>${agentA.getBuildType()}</th>

          <forms:option value="${agentAId}">
            <c:out value="${agentA.buildTypeName}"/>
            <!--bs:agentShortStatus agent="${agentA}" showUnavailable="${true}"/-->
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