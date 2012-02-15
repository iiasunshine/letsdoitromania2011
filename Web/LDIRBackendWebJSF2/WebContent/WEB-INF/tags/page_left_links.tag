<%@tag pageEncoding="UTF-8" %>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="role"%>

<div id="leftColumn">
 <c:if test="${(role eq 'ADMIN') or (role eq 'VOLUNTEER') or (role eq 'ORGANIZER')}">				
  <h:panelGroup rendered="#{teamBean.managerBool}">
  					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-add-editare.jsf">
					  	   <h:outputText value="» #{msg.echipa_add_team_link}" />
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
	<h:panelGroup rendered="#{not teamBean.managerBool}">  	
					  <h3>
					  	<a href="${pageContext.servletContext.contextPath}/users/echipa-del-editare.jsf">
					  	   <h:outputText value="» #{msg.echipa_remove_team_link}" />
					  	</a>
					  </h3>	
    </h:panelGroup>	
</c:if>    	
 <c:if test="${role eq 'ORGANIZER_MULTI'}">		
					    <h3>
							<a href="${pageContext.servletContext.contextPath}/users/echipa-org-multi-editare.jsf">
								 <h:outputText value="» #{msg.echipa_org_multi_link}"/>
							</a>
						</h3>
                         <br/>
				        <h3>
                             <h:outputText value="#{msg.echipa_empty_list}"
                                              rendered="#{fn:length(orgBean.teamList) eq 0}"/>
                             <h:outputText value="#{msg.echipa_list_title}"
                                              rendered="#{fn:length(orgBean.teamList) gt 0}"/>
                         </h3>
                         <h:panelGroup rendered="#{fn:length(orgBean.teamList) gt 0}">
                           <a4j:repeat value="#{orgBean.teamList}" var="team">
                          		 <div class="entryLeft">
                          		 	    <h:outputText value="Echipa Nume = " />
                                        <strong><h:outputText value="#{team.teamName}"/></strong>
                                        <br/>
                                        <h:outputLink value="echipa-org-multi-add.jsf?teamId=#{team.teamId}">
                                            <h:outputText value="#{msg.details_view_link}" escape="false"/>
                                        </h:outputLink>
                          		 </div>
                           </a4j:repeat>
                         
                         </h:panelGroup>
</c:if>  				  				  
					</div>		
