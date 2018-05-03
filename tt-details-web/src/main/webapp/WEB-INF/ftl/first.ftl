<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
<table >
	<tr>
	   <td>编号</td>
	   <td>学号</td>
	   <td>姓名</td>
	   <td>年龄</td>
	   <td>地址</td>
	</tr>
	<#list stulist as stu>
		<tr>
		    <td>${stu_index}</td>
			<td>${stu.id}</td>
			<td>${stu.name}</td>
			<td>${stu.age}</td>
			<td>${stu.address}</td>
		</tr>
	</#list>
	
	${date?date}&nbsp;&nbsp;${date?time}&nbsp;&nbsp;${date?datetime}
	&nbsp;&nbsp;
	${date?string('yyyy-MM-dd HH:mm:ss')}
    
   
</table>
</body>
</html>
