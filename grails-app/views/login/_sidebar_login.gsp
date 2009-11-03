

       		<h3>Login</h3>	      
            <form class="wufoo" action="${resource(file: 'j_spring_security_check')}">
            
            	<ul>
            		<li>
            			<label class="desc" >User Id:</label>
            			<div style="float:left">
            				<g:textField name="j_username"/>
            			</div>
	                </li>
	                <li>
	                	<label class="desc">Password: </label>
	                	<div style="float:left">
	                		<g:textField name="j_password"/>
	                	</div>
	                </li>
	                <li>
				       Remember: <input type='checkbox' name='_spring_security_remember_me'<g:if test='${hasCookie}'> checked='checked'</g:if>/>
				    </li>
				    <li>
                    	<g:submitButton name="login" value="Login"/>
                    </li>
				</ul>
            </form>

