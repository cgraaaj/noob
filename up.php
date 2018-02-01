<?php

    $upload_path = "Uploads/"; 
	$mat_path="Mat_file/";
	$file_name=basename( $_FILES['uploaded_file']['name']);
	$handwriten=substr($file_name,0,5);
	$hfile_name=substr($file_name,0,-4).".txt";
    $upload_path = $upload_path . $file_name;
	$mat_path=$mat_path.$file_name;
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $upload_path)) {
        echo "success";
		$file=basename( $_FILES['uploaded_file']['name']);
		$command ="matlab -minimize -nojvm -nodesktop -nodisplay -r \"Preprocess ('$upload_path','$mat_path');exit";
		exec($command);
		
			set_time_limit(70);
			sleep(60);
			$filetes= substr($file_name,0,-4);
			echo $handwriten;
			if($handwriten=="CHSCP")
			{
			$command = "cmd /c G:\\ec $file_name $filetes";
			exec($command);
			echo "suc prin";
			}
			else if($handwriten=="CHSCH"){
				
				$command="start /b G:\\resize $file_name";
				shell_exec($command);
				$command="start /b G:\\hand $file_name $hfile_name";
				shell_exec($command);
				echo "suc hand";
				$command="start /b G:\\leven $hfile_name";
				shell_exec($command);
				echo "suc leven";
			}
			else{
				echo "fail of text recognition";
			}
			
			
		
    } 
	else{
        echo "fail";
    }
 ?>