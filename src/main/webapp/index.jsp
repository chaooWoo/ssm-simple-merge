<%--
  Created by IntelliJ IDEA.
  User: W.Chao
  Date: 2019/8/5
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
index >> <br>
<<a href="account/findAll">test find all account</a>
<br>
<<a href="account/findAccountById">test find a account by id</a>
<br>
<form id="addAccount" method="post" action="account/addAccount">
    name:<input name="name" type="text">
    money:<input name="money" type="text">
    <input type="submit" value="submit">
</form>
<br>
<<a href="account/transfer">testing transfer of account to ensure spring transaction management</a>

</body>
</html>
