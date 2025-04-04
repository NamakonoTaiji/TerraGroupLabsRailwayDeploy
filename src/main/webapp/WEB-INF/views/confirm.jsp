<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="お問い合わせ確認 - TerraGroup Labs" />
<c:set var="currentPage" value="contact" />

<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div class="main-content-wrapper">
    
    <!-- 確認セクション -->
    <section class="py-5 mt-5">
        <div class="container">
            <h2 class="text-center mb-5">お問い合わせ内容の確認</h2>
            <div class="row justify-content-center">
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-body">
                            <div class="mb-4">
                                <h5>お名前</h5>
                                <p class="border-bottom pb-2"><c:out value="${contactMessage.name}" /></p>
                            </div>

                            <div class="mb-4">
                                <h5>メールアドレス</h5>
                                <p class="border-bottom pb-2"><c:out value="${contactMessage.email}" /></p>
                            </div>

                            <div class="mb-4">
                                <h5>お問い合わせ内容</h5>
                                <div class="border p-3 bg-light rounded">
                                    <c:out value="${contactMessage.message}" escapeXml="true" /></div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <!-- CSRFトークンを追加 -->
                                <input type="hidden" name="${_csrf.parameterName}"
                                    value="${_csrf.token}" />
                                <form action="<c:url value='/contact/back'/>" method="post">
                                    <button type="submit" class="btn btn-secondary">修正する</button>
                                </form>

                                <form action="<c:url value='/contact'/>" method="post">
                                    <!-- CSRFトークンを追加 -->
                                    <input type="hidden" name="${_csrf.parameterName}"
                                        value="${_csrf.token}" />
                                    <button type="submit" class="btn btn-primary">送信する</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />