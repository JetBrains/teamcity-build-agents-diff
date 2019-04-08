<%@ include file="/include.jsp" %>
<jsp:useBean id="diffUrl" scope="request" type="java.lang.String"/>
<c:set var="diffFullUrl"><c:url value="${diffUrl}"/></c:set>
<div>
  <a href="${diffFullUrl}" title="Click to go to build agents diff page." >Go to build agents diff page</a>
</div>