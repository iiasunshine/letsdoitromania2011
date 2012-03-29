<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<%@taglib prefix="customfn" uri="/WEB-INF/tld/custom-fn.tld" %>
<%@taglib prefix="custom" tagdir="/WEB-INF/tags/" %>

<f:view>


  

<div>
<h:messages warnClass="registerMessageError" infoClass="registerMessageOk" errorClass="registerMessageError" />

	<h:outputText 
		 value="#{adminGarbageManagerBean.setGarbageId(request.getParameter('garbageId'))}"> 
	</h:outputText>
		
	<h:outputText 
		 value="[[[[#{adminGarbageManagerBean.actionToVote()}]]]]"> 
	</h:outputText>

</div>
</f:view>
