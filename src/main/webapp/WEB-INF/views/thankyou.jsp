<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common-taglibs.jsp" %>

<c:set var="pageTitle" value="お問い合わせありがとうございました - TerraGroup Labs" />
<c:set var="currentPage" value="thankyou" />

<jsp:include page="fragments/header.jsp" />

	<!-- サンクスセクション -->
	<section class="py-5 mt-5">
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-lg-8">
					<div class="card border-0 shadow-sm">
						<div class="card-body text-center p-5">
							<div class="mb-4">
								<i class="bi bi-check-circle-fill text-success"
									style="font-size: 5rem;"></i>
							</div>
							<h1 class="display-5 mb-3">お問い合わせありがとうございました</h1>
							<p class="lead mb-4">ご入力いただいた内容は正常に送信されました。担当者より順次ご連絡いたします。</p>
							<p class="text-muted mb-4">お問い合わせの内容によっては、回答までにお時間をいただく場合がございます。ご了承ください。</p>
							<a href="<c:url value='/'/>" class="btn btn-primary"> <i
								class="bi bi-house-door me-2"></i>ホームに戻る
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

<jsp:include page="fragments/footer.jsp" />