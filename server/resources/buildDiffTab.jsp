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

      <forms:select name="buildTypeBSelection" id="buildTypeBSelection" style="width: 20em" enableFilter="true" onchange="return BS.BuildDiff.updateDiff('${ajaxUrl}');">
        <c:choose>
          <c:when test="${empty param.buildTypeIdB}">
            <forms:option value="${param.buildTypeId}">${param.buildTypeId}</forms:option>
          </c:when>
          <c:otherwise>
            <forms:option value="${param.buildTypeIdB}">${param.buildTypeIdB}</forms:option>
          </c:otherwise>
        </c:choose>

        <c:forEach items="${allBuildTypes}" var="buildA">
          <c:set var="buildAId" value="${buildA}"/>

          <forms:option value="${buildA}">
            <c:out value="${buildA}"/>
          </forms:option>
        </c:forEach>
      </forms:select>

      <label>build:</label>
      <forms:select name="buildBSelection" id="buildBSelection" style="width: 20em" enableFilter="true" onchange="return BS.BuildDiff.updateDiff('${ajaxUrl}');">
        <forms:option value="">-- Select build --</forms:option>
        <c:forEach items="${allBuilds}" var="buildA">
          <c:set var="buildAId" value="${buildA}"/>

          <forms:option value="${buildA.buildNumber}">
            <c:out value="${buildA.buildNumber}"/>
          </forms:option>
        </c:forEach>
      </forms:select>
    </div>

    <div id="buildDiffView"></div>

    <script type="application/javascript">
      prevBuildTypeValue = "${param.buildTypeId}";
      prevBuildTypeBValue = "${param.buildTypeIdB}";

      BS.BuildDiff.chooseBuild();
      BS.BuildDiff.updateDiff('${ajaxUrl}');

    </script>

  </c:otherwise>
</c:choose>