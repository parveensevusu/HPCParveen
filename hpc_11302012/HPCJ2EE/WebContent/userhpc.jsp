<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User HPC</title>
</head>
<body>
<form name="input" action="userhpcprocess.jsp" method="post">
user id: <input type="text" name="userid" value="parveen"><br/>
password: <input type="password" name="passwd" value="hpc123"> <br/>
skills required: <br/>
<input type="text" name="skill1" value="Driving NJ"><br/>
<input type="text" name="skill2" value="Driving OH"> <br/>

<input type="submit" value="Submit Work Request">

</form>

</body>
</html>