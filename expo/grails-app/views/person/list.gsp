<head>
	<meta name="layout" content="main" />
	<title>Listado de Usuariost</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><g:link class="create" action="create">Alta de Usuario</g:link></span>
	</div>

	<div class="body">
		<h1>Person List</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="list">
			<table>
			<thead>
				<tr>
					<g:sortableColumn property="id" title="Id" />
					<g:sortableColumn property="username" title="Nombre de Usuario" />
					<g:sortableColumn property="userRealName" title="Nombre Completo" />
					<g:sortableColumn property="enabled" title="Habilitado" />
					<g:sortableColumn property="description" title="Descripción" />
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			<g:each in="${personList}" status="i" var="person">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${person.id}</td>
					<td>${person.username?.encodeAsHTML()}</td>
					<td>${person.userRealName?.encodeAsHTML()}</td>
					<td>${person.enabled?.encodeAsHTML()}</td>
					<td>${person.description?.encodeAsHTML()}</td>
					<td class="actionButtons">
						<span class="actionButton">
							<g:link action="show" id="${person.id}">Mostrar</g:link>
						</span>
					</td>
				</tr>
			</g:each>
			</tbody>
			</table>
		</div>

		<div class="paginateButtons">
			<g:paginate total="${personInstanceTotal}" />
		</div>

	</div>
</body>
