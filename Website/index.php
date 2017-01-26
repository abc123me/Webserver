<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
  <body>
    <div id="main">
<<<<<<< HEAD
    	<p><h1>Your bank.com</h1></p>
    	<form>
    		<p>Username: </p><input>
    		<p>Password: </p><input type="Password">
    	</form>
    	<br>
    	<button>Login</button>
=======
    	<p>This is a php test</p>
    	<?php
		/* counter */

		//opens countlog.txt to read the number of hits
		$datei = fopen("/countlog.txt","r");
		$count = fgets($datei,1000);
		fclose($datei);
		$count=$count + 1 ;
		echo "$count" ;
		echo " hits" ;
		echo "\n" ;

		// opens countlog.txt to change new hit number
		$datei = fopen("/countlog.txt","w");
		fwrite($datei, $count);
		fclose($datei);
    	?>
>>>>>>> branch 'master' of https://github.com/abc123me/Webserver.git
    </div>
  </body>
</html>
