<?php
    $upload_path = "Output/C/";
	$errpath="Output/C/error/";
	$outpath="Output/C/finaloutput/";
	$text=$_REQUEST["sendMessage"];
$name=$_REQUEST["fname"];
if(isset($_REQUEST["sendMessage"])&&isset($_REQUEST["fname"]))
{
$text=$_REQUEST["sendMessage"];
$name=$_REQUEST["fname"];

$upload_path=$upload_path.$name;
	$myfile = fopen($upload_path, "w") or die("Unable to open file!");
fwrite($myfile, $text);
fclose($myfile);
$command="start /b  G:\\compile $name";
shell_exec($command);
	$data=file_get_contents($errpath."err.txt");
	echo $data;
	if($data==null)
	{
	$command="start /b G:\\exe";
	shell_exec($command);
	$data=file_get_contents($outpath."output.txt");
	echo $data;
    }
	
}
	else
	{
        echo "fail";
    }

 ?>