<%@ include file="/include-internal.jsp"%>

<jsp:useBean id="allAgents" type="java.util.List" scope="request"/>
<jsp:useBean id="cloudAgentTypes" type="java.util.List" scope="request"/>
<jsp:useBean id="unauthorizedAgents" type="java.util.Collection" scope="request"/>
<c:set var="ajaxUrl"><c:url value="/agents/diffView.html"/></c:set>

<c:choose>
  <c:when test="${empty allAgents && empty cloudAgentTypes && empty unauthorizedAgents}">
    <span>Nothing to compare.</span>
  </c:when>
  <c:otherwise>
    <div class="actionBar">
      <label>Compare build agent</label>
      <forms:select name="agentASelection" id="agentASelection" style="width: 20em" enableFilter="true" onchange="return BS.AgentsDiff.updateDiff('${ajaxUrl}');">
        <forms:option value="">-- Select agent --</forms:option>
        <c:forEach items="${allAgents}" var="agentA">
          <c:set var="agentAId" value="${agentA.id}"/>
          <forms:option value="${agentAId}"><c:out value="${agentA.name}"/> <bs:agentShortStatus agent="${agentA}" showUnavailable="${true}"/></forms:option>
        </c:forEach>
        <c:if test="${not empty cloudAgentTypes}">
          <forms:option value="">-- Cloud Agent Types --</forms:option>
          <c:forEach items="${cloudAgentTypes}" var="typeA">
            <c:set var="typeAId" value="${typeA.id}"/>
            <forms:option value="type-${typeAId}"><c:out value="${typeA.details.displayName}"/></forms:option>
          </c:forEach>
        </c:if>
        <c:if test="${not empty unauthorizedAgents}">
          <forms:option value="">-- Unauthorized Agents --</forms:option>
          <c:forEach items="${unauthorizedAgents}" var="agentA">
            <c:set var="agentAId" value="${agentA.id}"/>
            <forms:option value="${agentAId}"><c:out value="${agentA.name}"/> <bs:agentShortStatus agent="${agentA}" showUnavailable="${true}"/></forms:option>
          </c:forEach>
        </c:if>
      </forms:select>
      &nbsp;<label>with</label>
      <forms:select name="agentBSelection" id="agentBSelection" style="width: 20em" enableFilter="true" onchange="return BS.AgentsDiff.updateDiff('${ajaxUrl}');">
        <forms:option value="">-- Select agent --</forms:option>
        <c:forEach items="${allAgents}" var="agentB">
          <c:set var="agentBId" value="${agentB.id}"/>
          <forms:option value="${agentBId}"><c:out value="${agentB.name}"/> <bs:agentShortStatus agent="${agentB}" showUnavailable="${true}"/></forms:option>
        </c:forEach>
        <c:if test="${not empty cloudAgentTypes}">
          <forms:option value="">-- Cloud Agent Types --</forms:option>
          <c:forEach items="${cloudAgentTypes}" var="typeB">
            <c:set var="typeBId" value="${typeB.id}"/>
            <forms:option value="type-${typeBId}"><c:out value="${typeB.details.displayName}"/></forms:option>
          </c:forEach>
        </c:if>
        <c:if test="${not empty unauthorizedAgents}">
          <forms:option value="">-- Unauthorized Agents --</forms:option>
          <c:forEach items="${unauthorizedAgents}" var="agentB">
            <c:set var="agentBId" value="${agentB.id}"/>
            <forms:option value="${agentBId}"><c:out value="${agentB.name}"/> <bs:agentShortStatus agent="${agentB}" showUnavailable="${true}"/></forms:option>
          </c:forEach>
        </c:if>
      </forms:select>
    </div>

    <div id="agentsDiffView"></div>

    <script type="application/javascript">
      BS.AgentsDiff.chooseAgents();
      BS.AgentsDiff.updateDiff('${ajaxUrl}');
    </script>

  </c:otherwise>
</c:choose>