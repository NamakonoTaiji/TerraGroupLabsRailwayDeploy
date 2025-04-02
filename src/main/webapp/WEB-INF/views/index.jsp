<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="currentPage" value="home" />
<c:set var="additionalHeadContent">
</c:set>

<jsp:include page="fragments/header.jsp" />

<!-- ヒーローセクション -->
<header class="hero-section">
	<div class="container text-center">
		<h1 class="display-title mb-mb-d mb-3 animate-fade-in">地球の未来をテクノロジーで創造する</h1>
		<p class="lead mb-md-5 mb-4 animate-fade-in-delayed">持続可能な社会の実現に向けた革新的な技術開発</p>
		<a href="#services"
			class="btn btn-lg terra-btn-primary animate-fade-in-delayed-more">詳細はこちら
			<i class="bi bi-arrow-right ms-2"></i>
		</a>
	</div>
</header>
<!-- 企業実績を表示するセクション -->
<section class="section bg-light">
	<div class="container">
		<div class="text-center mb-5">
			<span class="badge bg-primary px-3 py-2 rounded-pill mb-3">OUR
				ACHIEVEMENTS</span>
			<h2 class="display-5 fw-bold">数字で見るTerraGroup Labs</h2>
			<p class="lead text-muted">革新的な技術と持続可能な未来への取り組み</p>
		</div>

		<div class="row g-4 text-center">
			<div class="col-md-3 col-6">
				<div class="p-4 bg-white rounded-lg shadow-sm h-100">
					<div class="display-4 text-primary mb-2">35+</div>
					<p class="fw-bold mb-1">研究プロジェクト</p>
					<p class="text-muted small">進行中の革新的な取り組み</p>
				</div>
			</div>
			<div class="col-md-3 col-6">
				<div class="p-4 bg-white rounded-lg shadow-sm h-100">
					<div class="display-4 text-primary mb-2">120</div>
					<p class="fw-bold mb-1">専門研究員</p>
					<p class="text-muted small">世界トップクラスの科学者集団</p>
				</div>
			</div>
			<div class="col-md-3 col-6">
				<div class="p-4 bg-white rounded-lg shadow-sm h-100">
					<div class="display-4 text-primary mb-2">48</div>
					<p class="fw-bold mb-1">特許取得</p>
					<p class="text-muted small">革新的な技術ソリューション</p>
				</div>
			</div>
			<div class="col-md-3 col-6">
				<div class="p-4 bg-white rounded-lg shadow-sm h-100">
					<div class="display-4 text-primary mb-2">18</div>
					<p class="fw-bold mb-1">国際パートナー</p>
					<p class="text-muted small">グローバルな研究ネットワーク</p>
				</div>
			</div>
		</div>
	</div>
</section>

<!-- 企業の展望、理念を表示するセクション -->
<section class="section position-relative">
	<div class="bg-image position-absolute w-100 h-100"
		style="background: linear-gradient(rgba(0, 30, 55, 0.8), rgba(0, 30, 55, 0.9)), url('/images/webp/tech-vision-bg.webp'); background-size: cover; background-position: center;"></div>
	<div class="container position-relative text-white">
		<div class="row">
			<div class="col-lg-6 pt-3">
				<span
					class="badge bg-light text-primary px-3 py-2 rounded-pill mb-3">OUR
					VISION</span>
				<h2 class="display-5 fw-bold mb-4">持続可能な未来へのビジョン</h2>
				<p class="lead mb-4">TerraGroup
					Labsは「科学の力で世界の課題を解決する」という信念のもと、革新的な技術開発に取り組んでいます。</p>

				<div class="d-flex align-items-center mb-4">
					<div
						class="icon-box me-3 bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
						style="width: 50px; height: 50px;">
						<i class="bi bi-lightbulb-fill"></i>
					</div>
					<div>
						<h4 class="mb-1">革新的思考</h4>
						<p class="mb-0">従来の枠組みにとらわれない発想で新たな可能性を追求します</p>
					</div>
				</div>

				<div class="d-flex align-items-center mb-4">
					<div
						class="icon-box me-3 bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
						style="width: 50px; height: 50px;">
						<i class="bi bi-globe"></i>
					</div>
					<div>
						<h4 class="mb-1">地球規模の影響</h4>
						<p class="mb-0">すべてのプロジェクトは地球環境と人類社会の調和を目指しています</p>
					</div>
				</div>

				<div class="d-flex align-items-center">
					<div
						class="icon-box me-3 bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
						style="width: 50px; height: 50px;">
						<i class="bi bi-hand-thumbs-up"></i>
					</div>
					<div>
						<h4 class="mb-1">倫理的アプローチ</h4>
						<p class="mb-0">テクノロジーの発展と倫理的価値観の共存を大切にしています</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>

<!-- 企業のニュースを表示するセクション -->
<section class="section">
	<div class="container">
		<div class="text-center mb-5">
			<span class="badge bg-primary px-3 py-2 rounded-pill mb-3 mt-3">LATEST
				NEWS</span>
			<h2 class="display-5 fw-bold">最新の研究成果</h2>
			<p class="lead text-muted">TerraGroup Labsの最新の取り組みや成果をご紹介します</p>
		</div>

		<div class="row g-4">
			<div class="col-md-4">
				<div class="card h-100 border-0 shadow-sm">
					<picture>
					<source srcset="/images/webp/news-1.webp" type="image/webp">
					<img src="/images/jpg/news-1.jpg" class="card-img-top" alt="研究ニュース"
						loading="lazy"> </picture>
					<div class="card-body">
						<div class="d-flex align-items-center mb-2">
							<span class="badge bg-primary-light text-primary me-2">エネルギー部門</span>
							<small class="text-muted">2025.02.15</small>
						</div>
						<h4 class="card-title">Project Heliosがエネルギー効率30%向上を実現</h4>
						<p class="card-text">最新の核融合発電システムのテストで画期的な成果を達成。エネルギー効率が従来モデルより30%向上し、実用化への大きな一歩に。</p>
						<a href="#" class="btn btn-link p-0">詳細を読む <i
							class="bi bi-arrow-right"></i></a>
					</div>
				</div>
			</div>

			<div class="col-md-4">
				<div class="card h-100 border-0 shadow-sm">
					<picture>
					<source srcset="/images/webp/news-2.webp" type="image/webp">
					<img src="/images/jpg/news-2.jpg" class="card-img-top" alt="研究ニュース"
						loading="lazy"> </picture>
					<div class="card-body">
						<div class="d-flex align-items-center mb-2">
							<span class="badge bg-primary-light text-primary me-2">バイオテック部門</span>
							<small class="text-muted">2025.01.22</small>
						</div>
						<h4 class="card-title">GeneLock技術の臨床試験で有望な結果</h4>
						<p class="card-text">希少疾患治療に向けた遺伝子治療技術GeneLockの初期臨床試験で有望な結果を確認。安全性プロファイルも良好。</p>
						<a href="#" class="btn btn-link p-0">詳細を読む <i
							class="bi bi-arrow-right"></i></a>
					</div>
				</div>
			</div>

			<div class="col-md-4">
				<div class="card h-100 border-0 shadow-sm">
					<picture>
					<source srcset="/images/webp/news-3.webp" type="image/webp">
					<img src="/images/jpg/news-3.jpg" class="card-img-top" alt="研究ニュース"
						loading="lazy"> </picture>
					<div class="card-body">
						<div class="d-flex align-items-center mb-2">
							<span class="badge bg-primary-light text-primary me-2">防衛部門</span>
							<small class="text-muted">2024.12.05</small>
						</div>
						<h4 class="card-title">TG-Sentinelシステムがミサイルの飽和攻撃に対し高い迎撃率を記録</h4>
						<p class="card-text">無人戦闘システムTG-Sentinelが飽和攻撃に対する高い抗堪性を示した。コストと信頼性を両立させた、ミサイルに代わる新しい防衛システム。</p>
						<a href="#" class="btn btn-link p-0">詳細を読む <i
							class="bi bi-arrow-right"></i></a>
					</div>
				</div>
			</div>
		</div>

		<div class="text-center mt-5">
			<a href="#" class="btn terra-btn-primary">すべてのニュースを見る <i
				class="bi bi-arrow-right ms-2"></i></a>
		</div>
	</div>
</section>

<!-- サービスセクション -->
<section id="services" class="section bg-light">
	<div class="container">
		<div class="text-center mb-5">
			<span class="badge bg-primary px-3 py-2 rounded-pill mb-3">OUR
				SERVICES</span>
			<h2 class="display-5 fw-bold">革新的な技術ソリューション</h2>
			<p class="lead text-muted">最先端の研究と開発で、より良い未来を創造します</p>
		</div>

		<div class="row g-4">
			<!-- エネルギー部門 -->
			<div class="col-md-4">
				<div class="terra-card h-100">
					<div class="card-img-top-container">
						<picture>
						<source srcset="/images/webp/energy-tech.webp" type="image/webp">
						<img src="<c:url value='/images/jpg/energy-tech.jpg'/>"
							class="card-img-top" alt="エネルギー技術" loading="lazy"> </picture>
						<div class="card-body p-4">
							<div class="service-icon mb-4">
								<i class="bi bi-lightning-charge-fill"></i>
							</div>
							<h3>エネルギー技術</h3>
							<p>次世代のクリーンエネルギー技術で持続可能な社会の実現を目指します。核融合研究「Project
								Helios」や次世代太陽光パネルなど、革新的なエネルギーソリューションを開発しています。</p>
						</div>
					</div>
				</div>
			</div>

			<!-- 防衛部門 -->
			<div class="col-md-4">
				<div class="terra-card h-100">
					<div class="card-img-top-container">
						<picture>
						<source srcset="/images/webp/defence-tech.webp" type="image/webp">
						<img src="<c:url value='/images/jpg/defence-tech.jpg'/>"
							class="card-img-top" alt="防衛技術" loading="lazy"> </picture>
						<div class="card-body p-4">
							<div class="service-icon mb-4">
								<i class="bi bi-shield-lock-fill"></i>
							</div>
							<h3>防衛システム</h3>
							<p>高度なセキュリティと防衛技術で社会の安全を守ります。無人戦闘システム「TG-Sentinel」や特殊装備の開発を通じて、最先端の防衛ソリューションを提供しています。</p>
						</div>
					</div>
				</div>
			</div>

			<!-- バイオテクノロジー部門 -->
			<div class="col-md-4">
				<div class="terra-card h-100">
					<div class="card-img-top-container">
						<picture>
						<source srcset="/images/webp/bio-tech.webp" type="image/webp">
						<img src="<c:url value='/images/jpg/bio-tech.jpg'/>"
							class="card-img-top" alt="バイオテクノロジー" loading="lazy"> </picture>
						<div class="card-body p-4">
							<div class="service-icon mb-4">
								<i class="bi bi-virus"></i>
							</div>
							<h3>バイオテクノロジー</h3>
							<p>生命科学の革新的研究により、人類の健康と環境の未来を切り拓きます。遺伝子治療「GeneLock」や極限環境耐性生物の研究など、最先端のバイオテクノロジーを探求しています。</p>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 企業スローガン -->
		<div class="text-center mt-5">
			<p class="lead fw-bold">"Innovating Tomorrow, Securing Today"</p>
			<a href="<c:url value='/service'/>"
				class="btn terra-btn-primary mt-3"> すべてのサービスを見る <i
				class="bi bi-arrow-right ms-2"></i>
			</a>
		</div>
	</div>
</section>

<!-- お問い合わせセクション -->
<section id="contact" class="section">
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-lg-8">
				<div class="text-center mb-5">
					<span class="badge bg-primary px-3 py-2 rounded-pill mb-3">CONTACT
						US</span>
					<h2 class="display-5 fw-bold">お問い合わせ</h2>
					<p class="lead text-muted">ご質問やお見積りのご依頼など、お気軽にお問い合わせください</p>
				</div>

				<div class="card shadow-sm">
					<div class="card-body p-4 p-md-5">
						<form:form id="contactForm" modelAttribute="contactMessage"
							action="${pageContext.request.contextPath}/contact/confirm"
							method="post" class="needs-validation" novalidate="true">
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
							<div class="mb-4">
								<label for="name" class="form-label">お名前 <span
									class="text-danger">*</span></label>
								<div class="input-group">
									<span class="input-group-text"><i class="bi bi-person"></i></span>
									<form:input path="name" id="name" class="form-control"
										required="true" />
								</div>
								<form:errors path="name" cssClass="text-danger small mt-1"
									element="div" />
							</div>

							<div class="mb-4">
								<label for="email" class="form-label">メールアドレス <span
									class="text-danger">*</span></label>
								<div class="input-group">
									<span class="input-group-text"><i class="bi bi-envelope"></i></span>
									<form:input path="email" id="email" type="email"
										class="form-control" required="true" />
								</div>
								<form:errors path="email" cssClass="text-danger small mt-1"
									element="div" />
							</div>

							<div class="mb-4">
								<label for="message" class="form-label">お問い合わせ内容 <span
									class="text-danger">*</span></label>
								<form:textarea path="message" id="message" class="form-control"
									rows="5" required="true" />
								<form:errors path="message" cssClass="text-danger small mt-1"
									element="div" />
							</div>

							<div class="mb-3">
								<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"
									data-callback="recaptchaCallback" aria-label="reCAPTCHA チャレンジ"></div>
								<p class="sr-only">このサイトはreCAPTCHAによって保護されており、Googleのプライバシーポリシーと利用規約が適用されます。</p>
								<c:if test="${not empty recaptchaError}">
									<div class="text-danger mt-2">${recaptchaError}</div>
								</c:if>
							</div>

							<div class="d-grid gap-2">
								<button type="submit" class="btn btn-primary btn-lg">
									送信する <i class="bi bi-send ms-2"></i>
								</button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<jsp:include page="fragments/footer.jsp" />