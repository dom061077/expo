<head>
	<%
		out << "<script type='text/javascript'>";
		out << "var usuarioId = ${id};";
		out << "</script>";
	%>
	<meta name="layout" content="main" />
	<title>Modificar Usuario</title>
	<script type="text/javascript" src='${resource(dir:'js/person',file:'edit.js')}'></script>	
</head>

<body>

	<div class="nav">
		<span class="menuButton"><g:link class="list" action="list">Listado de Usuarios</g:link></span>
		<span class="menuButton"><g:link class="create" action="create">Alta de Usuario</g:link></span>
	</div>
	<div id='formulario_extjs'>
	</div>

</body>
