<%--
  Created by IntelliJ IDEA.
  User: shiyi
  Date: 2022-09-07
  Time: 9:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改书籍</title>
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <div class="page-header">
                    <h1>
                        <small>修改书籍</small>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-md-8 column">
            <form action="${pageContext.request.contextPath}/book/updateBook" method="post">
                <%-- 这里要特别注意！--%>
                <input type="hidden" name="bookID" value="${QBook.bookID}"/>
                <div class="form-group">
                    <label for="bkname">书籍名称：</label>
                    <input type="text" class="form-control" id="bkname" value="${QBook.bookName}" name="bookName" required>
                </div>
                <div class="form-group">
                    <label for="bkcount">书籍数量：</label>
                    <input type="text" class="form-control" id="bkcount" value="${QBook.bookCounts}" name="bookCounts" required>
                </div>
                <div class="form-group">
                    <label for="bkdetail">书籍描述：</label>
                    <input type="text" class="form-control" id="bkdetail" value="${QBook.detail}" name="detail" required>
                </div>
                <div class="form-group">
                    <input type="submit" class="form-control" value="修改">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
