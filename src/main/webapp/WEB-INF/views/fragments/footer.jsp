<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- フッター -->
    <footer class="bg-dark text-white py-4 mt-5">
        <div class="container text-center">
            <div class="row">
                <div class="col-md-4 mb-4 mb-md-0 text-md-start">
                    <h5>TerraGroup Labs</h5>
                    <p class="small mb-0">持続可能な社会の実現に向けた革新的な技術開発</p>
                    <p class="small mb-0">東京都港区虎ノ門1-2-3 テラグループビル</p>
                </div>
                <div class="col-md-4 mb-4 mb-md-0">
                    <h5>リンク</h5>
                    <ul class="list-unstyled">
                        <li><a href="<c:url value='/'/>" class="text-white">ホーム</a></li>
                        <li><a href="<c:url value='/service'/>" class="text-white">サービス</a></li>
                        <li><a href="<c:url value='/about'/>" class="text-white">会社概要</a></li>
                        <li><a href="<c:url value='/privacy-policy'/>" class="text-white">プライバシーポリシー</a></li>
                    </ul>
                </div>
                <div class="col-md-4 text-md-end">
                    <h5>お問い合わせ</h5>
                    <p class="small mb-2">お気軽にお問い合わせください。</p>
                    <a href="${currentPage == 'home' ? '#contact' : '/#contact'}" class="btn btn-outline-light btn-sm">お問い合わせフォーム</a>
                </div>
            </div>
            <hr class="my-4">
            <p>&copy; 2025 TerraGroup Labs All Rights Reserved.</p>
            <div class="social-icons mt-3">
                <a href="#" class="text-white me-3" aria-label="Twitter"><i class="bi bi-twitter"></i></a>
                <a href="#" class="text-white me-3" aria-label="Facebook"><i class="bi bi-facebook"></i></a>
                <a href="#" class="text-white me-3" aria-label="LinkedIn"><i class="bi bi-linkedin"></i></a>
            </div>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- メインのJavaScriptファイル -->
    <script src="/js/main.js"></script>
    
    <!-- Google アナリティクス -->
    <c:if test="${not empty googleAnalyticsId}">
    <script async src="https://www.googletagmanager.com/gtag/js?id=${googleAnalyticsId}"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', '${googleAnalyticsId}');
    </script>
    </c:if>
    
    <!-- 追加のスクリプト -->
    <c:if test="${not empty additionalScripts}">
        ${additionalScripts}
    </c:if>
</body>
</html>