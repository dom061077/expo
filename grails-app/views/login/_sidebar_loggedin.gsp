            <dl>
                <dd>
                  <g:loggedInUserInfo field="userRealName">Guest</g:loggedInUserInfo>
                </dd>
            </dl>
            <ul>
            	<li>
		            <g:isLoggedIn>
		            	<g:link controller="person" action="editpassw">
		            		Cambiar Contraseña
		            	</g:link>
		            </g:isLoggedIn>
		        </li>
		        <li>
		            <g:isLoggedIn><g:link controller="logout" action="index">Cerrar Sesión</g:link></g:isLoggedIn>
		        </li>
			</ul>