<%@ include file="/include.jsp" %>
<jsp:useBean id="diffUrl" scope="request" type="java.lang.String"/>
<div>
  <a href="${diffUrl}" title="Click to go to builds diff page." >Go to builds diff page</a>
</div>