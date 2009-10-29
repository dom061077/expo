<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'reset-fonts-grids.css')}"/>        
        <g:layoutHead />
        <g:javascript library="application" />				
   <style type="text/css">
   #custom-doc { width: 75%; min-width: 250px; }
   
	#hd {
		background: #fff url('/expo/images/topbg.jpg');
		background-repeat:no-repeat;
	}   
   </style>
</head>
<body>
<div id="custom-doc" class="yui-t7">
   <div id="hd" role="banner">
   	  <div>
   	  	 
   	  </div>
   </div>
   <div id="bd" role="main">
	<div class="yui-ge">
    	<div class="yui-u first">
			<g:layoutBody/>
	    </div>
    	<div class="yui-u">
			<!-- YOUR DATA GOES HERE -->
	    </div>
</div>

	</div>
   <div id="ft" role="undefined"><p>Footer</p></div>
</div>
</body>
</html>
