<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common-taglibs.jsp" %>

<c:set var="pageTitle" value="サービス - TerraGroup Labs" />
<c:set var="currentPage" value="service" />

<jsp:include page="fragments/header.jsp" />

	<!-- ヘッダー -->
	<header class="page-header py-5 mt-5 bg-light">
		<div class="container">
			<h1>サービス</h1>
			<p class="lead">TerraGroup Labsが提供する革新的なサービスと技術ソリューション</p>
		</div>
	</header>

	<!-- サービス詳細セクション -->
	<section class="py-5">
		<div class="container">
			<!-- サービス一覧をループで表示 -->
			<c:forEach var="service" items="${services}" varStatus="status">
				<div
					class="row mb-5 align-items-center service-detail ${status.index % 2 == 0 ? '' : 'flex-row-reverse'}">
					<div class="col-md-6">
						<div class="service-icon-large text-center mb-3">
							<i class="${service.iconName} display-1 text-primary"></i>
						</div>
					</div>
					<div class="col-md-6">
						<h2>${service.title}</h2>
						<p class="lead">${service.description}</p>
						<h4 class="mt-4">主な特徴</h4>
						<ul>
							<li>革新的なアプローチ</li>
							<li>持続可能な開発</li>
							<li>最先端の技術活用</li>
							<li>専門家チームによる実装</li>
						</ul>
						<a href="#" class="btn btn-primary mt-3">詳細を見る</a>
					</div>
				</div>
				<hr class="my-5">
			</c:forEach>
		</div>
	</section>

	<!-- 事例セクション -->
	<section class="py-5 bg-light">
		<div class="container">
			<h2 class="text-center mb-5">導入事例</h2>
			<div class="row">
				<div class="col-md-4 mb-4">
					<div class="card h-100">
						<div class="card-body">
							<h3 class="card-title">グリーンエネルギー株式会社</h3>
							<p class="card-text">バイオテクノロジーを活用した再生可能エネルギー生産システムの導入により、エネルギー効率が30%向上。</p>
						</div>
						<div class="card-footer bg-transparent border-0">
							<a href="#" class="btn btn-outline-primary">詳細を見る</a>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-4">
					<div class="card h-100">
						<div class="card-body">
							<h3 class="card-title">未来都市プロジェクト</h3>
							<p class="card-text">AIを活用した都市環境モニタリングシステムにより、CO2排出量の削減と生活環境の向上を実現。</p>
						</div>
						<div class="card-footer bg-transparent border-0">
							<a href="#" class="btn btn-outline-primary">詳細を見る</a>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-4">
					<div class="card h-100">
						<div class="card-body">
							<h3 class="card-title">エコファーム協同組合</h3>
							<p class="card-text">環境テクノロジーを活用した次世代農業システムの導入により、水使用量の50%削減と収穫量の増加を実現。</p>
						</div>
						<div class="card-footer bg-transparent border-0">
							<a href="#" class="btn btn-outline-primary">詳細を見る</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

<jsp:include page="fragments/footer.jsp" />