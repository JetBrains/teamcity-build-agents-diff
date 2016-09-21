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

      <c:choose>
        <c:when test="${empty param.buildTypeIdB}">
          <c:set var="selectedbuildType" value="${param.buildTypeId}"/>
        </c:when>
        <c:otherwise>
          <c:set var="selectedbuildType" value="${param.buildTypeIdB}"/>
        </c:otherwise>
      </c:choose>

      <forms:select name="buildTypeBSelection" id="buildTypeBSelection"
                    style="width: 20em" enableFilter="true"
                    onchange="return BS.BuildDiff.updateDiff('${ajaxUrl}');">

        <c:forEach items="${allBuildTypes}" var="potentialBuildType">
          <forms:option value="${potentialBuildType}" selected="${potentialBuildType == selectedbuildType}">
            <c:out value="${potentialBuildType}"/>
          </forms:option>
        </c:forEach>
      </forms:select>

      <label>build:</label>
      <forms:select name="buildBSelection" id="buildBSelection"
                    style="width: 20em" enableFilter="true"
                    onchange="return BS.BuildDiff.updateDiff('${ajaxUrl}');">

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