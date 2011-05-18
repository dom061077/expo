
<%@ page import="com.rural.ListaPrecios" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create ListaPrecios</title>
        
        
		<script type="text/javascript">$(function() {
			$('.map').maphilight();
			});
		</script>
        
    </head>
    <body>
    
<h1>A small map</h1>

<p>This is just a simple example of how the maps look.</p>
<p>
<img src=demo_simple.png width=420 height=300 class=map usemap="#simple">
<map name=simple>
<area href="#" shape=poly coords="47,62,106,61,134,72,135,118,30,116" alt=Link title="Default behavior">
<area href="#" shape=poly coords="32,157,133,157,127,192,127,211,28,211" alt=Link class="{strokeColor:'0000ff',strokeWidth:5,fillColor:'ff0000',fillOpacity:0.6}" title="Metadata'd up a bit">
<area id=squidhead href="#" shape=circle coords="290,102,30" alt=Octoface class="{stroke:false,fillColor:'ff0000',fillOpacity:0.6}" title="Metadata'd up a bit">
<area href="#" shape=poly coords="219,190,225,175,237,157,239,148,245,142,251,140,263,140,265,139,310,139,311,145,364,182,363,201,343,221,333,213,329,181,313,177,330,234,295,231,292,174,279,176,282,204,269,220,255,206,267,167,235,193" alt=Legs class="{stroke:false,fillColor:'000000',fillOpacity:1,alwaysOn:true}" title="Metadata'd up a bit">
</map>
</p>
<p>Because it's a really common question: <a id=squidheadlink href="#">mouse over this to trigger a hilight from an external element!</a></p>
<p>The code involved is very simple:</p>
<code><pre>$('#squidheadlink').mouseover(function(e) {
    $('#squidhead').mouseover();
}).mouseout(function(e) {
    $('#squidhead').mouseout();
}).click(function(e) { e.preventDefault(); });</pre></code>

<a href="./">Back to the index</a>

    
    </body>
</html>
