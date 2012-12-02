<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="edu.rutgers.hpc.*" %>
     <%@ page import="java.math.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%

String userid = request.getParameter("userid");
String passwd = request.getParameter("passwd");
String skill1 = request.getParameter("skill1");
String skill2 = request.getParameter("skill2");


List<String> skillsNeeded = new ArrayList<String>();
skillsNeeded.add(skill1);
skillsNeeded.add(skill2);

User user = new User();
user.setUserID(userid);
user.setPassword(passwd);

HPC hpc = new HPC(user);

String taskDescription = "Finding Directions";
HashMap<String, String> input = new HashMap<String, String>();
input.put("Start Location", "Piscatway, NJ");
input.put("End Location", "Cincinnati, OH");


long deadLine = 10000; // milleseconds

Cost cost = new Cost();
cost.setWorkHours(1); // Assuming 1 hour task
cost.setMoney(new BigDecimal(10.00)); // Money 10 dollar
cost.setEffort(10); // Effort scale 1-100, 10 being the least effort

HashMap<String, String> hpcResponse = hpc.invoke(taskDescription,
		input, deadLine, skillsNeeded, cost);
if (hpcResponse != null) {
	for (Map.Entry<String, String> entry : hpcResponse.entrySet()) {
		String key = entry.getKey();
		String value = entry.getValue();
		out.println("HPC Response Key = " + key);
		out.println("HPC Response Value = " + value);
	}
}


%>
</body>
</html>