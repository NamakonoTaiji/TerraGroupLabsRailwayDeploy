<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <%-- JSTL Core タグライブラリ --%>

<%-- 管理画面用の共通ヘッダーをインクルード (パスは実際の構成に合わせてください) --%>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div class="container mt-4">
    <%-- ページタイトルは Controller から渡されたものを使う (ヘッダー内で使われる想定) --%>
    <h2><c:out value="${pageTitle}" default="お問い合わせ詳細"/></h2> <%-- default値を追加 --%>

    <%-- message オブジェクトが存在する場合のみ詳細を表示 --%>
    <c:if test="${not empty message}">
        <div class="card mt-3">
            <div class="card-header">
                <strong>ID:</strong>
                <c:out value="${message.id}" />
            </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <strong>お名前:</strong>
                    <c:out value="${message.name}" />
                </li>
                <li class="list-group-item">
                    <strong>Email:</strong>
                    <c:out value="${message.email}" />
                </li>
                <li class="list-group-item">
                    <strong>メッセージ内容:</strong>
                    <%-- preタグで整形を保持しつつ、c:outでエスケープ --%>
                    <pre class="message-content-pre"><c:out value="${message.message}" /></pre>
                </li>
                <%-- 必要に応じて createdAt など他のフィールドも表示 --%>
                <%--
                <c:if test="${not empty message.createdAt}">
                    <li class="list-group-item">
                        <strong>受信日時:</strong>
                        <fmt:formatDate value="${message.createdAt}" pattern="yyyy/MM/dd HH:mm:ss" />  <-- fmt タグライブラリが必要
                    </li>
                </c:if>
                --%>
            </ul>
        </div>
    </c:if>

    <%-- message オブジェクトが存在しない (null) 場合の表示 --%>
    <c:if test="${empty message}">
        <div class="alert alert-warning mt-3" role="alert">
            指定されたIDのお問い合わせメッセージは見つかりませんでした。
        </div>
    </c:if>

    <%-- 一覧画面へ戻るボタン --%>
    <div class="mt-4">
        <a href="<c:url value='/admin/messages'/>" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> 一覧へ戻る
        </a>
    </div>

</div>

<%-- 管理画面用の共通フッターをインクルード (パスは実際の構成に合わせてください) --%>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />