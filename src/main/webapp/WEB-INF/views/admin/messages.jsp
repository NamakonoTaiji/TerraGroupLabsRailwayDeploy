<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common-taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>お問い合わせ管理 - TerraGroup Labs</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Icons -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
<!-- カスタムCSS -->
<link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Space+Grotesk:wght@400;500;700&family=JetBrains+Mono&display=swap"
	rel="stylesheet">

</head>
<body>
	<!-- ナビゲーションバー -->
	<!-- ... 省略 ... -->

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
								<td>${msg.id}</td>
								<td>${msg.name}</td>
								<td>${msg.email}</td>
								<td>${msg.message.length() > 30 ? msg.message.substring(0, 30) : msg.message}...</td>
								<td><a href="<c:url value='/admin/messages/${msg.id}'/>"
									class="btn btn-sm btn-primary"> <i class="bi bi-eye"></i>
										詳細
								</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<!-- フッター -->
	<!-- ... 省略 ... -->

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>