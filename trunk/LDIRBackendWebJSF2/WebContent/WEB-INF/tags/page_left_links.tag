<%@tag pageEncoding="UTF-8" %>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="role"%>

					<div id="leftColumn">

  <h:panelGroup rendered="#{teamBean.managerBool}">
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa_mem_detalii.jsf">
					  	   <h:outputText value="» #{msg.echipa_add_mem_link}" />
					  	</a>
					  </h3>
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-mem-editare.jsf">
					  	   <h:outputText value="» #{msg.echipa_mod_mem_link}" />
					  	</a>
					  </h3>
		<h:panelGroup rendered="#{not teamBean.orgBool}">  			  
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-org-detalii.jsf">
					  	   <h:outputText value="» #{msg.echipa_add_org_link}" />
					  	</a>
					  </h3>
		</h:panelGroup>			  
		<h:panelGroup rendered="#{teamBean.orgBool}">  
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-org-detalii.jsf">
					  	   <h:outputText value="» #{msg.echipa_mod_org_link}" />
					  	</a>
					  </h3>
		</h:panelGroup>		
		<h:panelGroup rendered="#{not teamBean.equipmentBool}">  		  
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-equip-detalii.jsf">
					  	   <h:outputText value="» #{msg.echipa_add_dotari_link}" />
					  	</a>
					  </h3>
	    </h:panelGroup>	
		<h:panelGroup rendered="#{teamBean.equipmentBool}">  			  
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-equip-detalii.jsf">
					  	   <h:outputText value="» #{msg.echipa_mod_dotari_link}" />
					  	</a>
					  </h3>
		</h:panelGroup>				  
</h:panelGroup>	
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-add-editare.jsf">
					  	   <h:outputText value="» #{msg.echipa_add_team_link}" />
					  	</a>
					  </h3>				    
	<h:panelGroup rendered="#{not teamBean.managerBool}">  	
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-del-editare.jsf">
					  	   <h:outputText value="» #{msg.echipa_remove_team_link}" />
					  	</a>
					  </h3>	
    </h:panelGroup>						  				  
					</div>		
