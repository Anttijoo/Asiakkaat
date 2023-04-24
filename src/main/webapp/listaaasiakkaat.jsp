<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="scripts/main.js"></script>
<title>Asiakaslista</title>
<style>
	
	th {
	background-color: lightblue;
	}

	#listaus, th, td {
	border: 1px solid black;
	border-collapse: collapse;
	
	}
	th, td {
	padding: 15px;
	}
</style>
</head>
<body>
<table id="listaus">
	<thead>	
		<tr>
			<tr>
			<th>Hakusana:</th>
			<th colspan="2"><input type="text" id="hakusana"></th>
			<th><input type="button" value="hae" id="hakunappi" onclick="haeAsiakkaat()"></th>
		</tr>
		</tr>		
		<tr>
			<th>Etunimi</th>
			<th>Sukunimi</th>
			<th>puhelinnumero</th>
			<th>Sähköposti</th>
			
		</tr>
	</thead>
	<tbody id="tbody">
	</tbody>
</table>
<span id="ilmo"></span>
<script>
haeAsiakkaat();
</script>
</body>
</html>