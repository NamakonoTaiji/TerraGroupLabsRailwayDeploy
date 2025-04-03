<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="ログイン - TerraGroup Labs" />
<c:set var="currentPage" value="login" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="main-content-wrapper">

    <!-- ログインフォーム -->
    <section class="py-5 mt-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-5">
                            <h2 class="text-center mb-4">管理者ログイン</h2>

                            <!-- エラーメッセージ表示 -->
                            <c:if test="${param.error != null}">
                                <div class="alert alert-danger" role="alert">
                                    ユーザー名またはパスワードが正しくありません。</div>
                            </c:if>

                            <c:if test="${param.logout != null}">
                                <div class="alert alert-success" role="alert">ログアウトしました。</div>
                            </c:if>

                            <!-- ログインフォーム -->
                            <form action="<c:url value='/login'/>" method="post">
                                <!-- CSRF対策トークン（Spring Securityが自動で追加） -->
                                <input type="hidden" name="${_csrf.parameterName}"
                                    value="${_csrf.token}" />

                                <div class="mb-3">
                                    <label for="username" class="form-label">ユーザー名</label> <input
                                        type="text" id="username" name="username" class="form-control"
                                        required autofocus>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">パスワード</label> <input
                                        type="password" id="password" name="password"
                                        class="form-control" required>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">ログイン</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />