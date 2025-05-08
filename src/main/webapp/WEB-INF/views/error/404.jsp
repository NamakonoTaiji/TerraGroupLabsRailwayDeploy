<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 Not Found - TerraGroupLabs</title>
    <%-- ここに通常のCSSなどをインポート --%>
    <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
    <%-- 必要なら Bootstrap なども --%>
</head>
<body>
    <%-- ヘッダーなどをインクルードしても良い --%>
    <div class="container text-center py-5">
        <h1 class="display-1">404</h1>
        <h2>${error} - ページが見つかりません</h2>
        <p>${message}</p> <%-- ハンドラで設定したメッセージ --%>
        <p>URL: ${path}</p>
        <a href="<c:url value='/'/>" class="btn terra-btn-primary mt-3">トップページへ戻る</a>
    </div>
    <%-- フッターなどをインクルードしても良い --%>
</body>
</html>