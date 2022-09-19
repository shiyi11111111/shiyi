<%--
  Created by IntelliJ IDEA.
  User: shiyi
  Date: 2022-09-06
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>新增书籍</small>
                </h1>
            </div>
        </div>
    </div>

    <div class="col-md-6 column">
<%--        <div class="page-header">--%>
            <form action="${pageContext.request.contextPath}/book/addBook" method="post">
                <div class="form-group">
                    <label for="bkname">书籍名称：</label>
                    <input type="text" class="form-control" id="bkname" name="bookName" required>
                </div>
                <div class="form-group">
                    <label for="bkcount">书籍数量：</label>
                    <input type="text" class="form-control" id="bkcount" name="bookCounts" required>
                </div>
                <div class="form-group">
                    <label for="bkdetail">书籍描述：</label>
                    <input type="text" class="form-control" id="bkdetail" name="detail" required>
                </div>
                <div class="form-group">
                    <input type="submit" class="form-control" value="添加">
                </div>
            </form>
<%--        </div>--%>
    </div>
</div>
</body>
</html>
