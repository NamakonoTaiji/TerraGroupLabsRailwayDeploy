<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

	<!-- メインコンテンツ -->
	<div class="container mt-5 pt-5">
		<h1 class="mb-4">お問い合わせ管理</h1>

		<div class="card">
			<div class="card-body">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>名前</th>
							<th>メールアドレス</th>
							<th>メッセージ（抜粋）</th>
							<th>操作</th>
						</tr>
					</thead>
                    <tbody>
                        <c:forEach items="${messages}" var="msg">
                            <tr>
                                <td><c:out value="${msg.id}" /></td>
                                <td><c:out value="${msg.name}" /></td>
                                <td><c:out value="${msg.email}" /></td>
                                <td>
                                    <c:out value="${fn:substring(msg.message, 0, 30)}" />
                                    <c:if test="${fn:length(msg.message) > 30}">...</c:if>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/messages/${msg.id}" class="btn btn-sm btn-primary">
                                        <i class="bi bi-eye"></i> 詳細
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty messages}">
                            <tr>
                                <td colspan="5">メッセージはまだありません。</td>
                            </tr>
                        </c:if>
                    </tbody>
				</table>
			</div>
		</div>
	</div>
    <jsp:include page="/WEB-INF/views/fragments/footer.jsp" />