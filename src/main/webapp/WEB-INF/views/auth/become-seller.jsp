<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="Become a Seller on HarmeshMart - Start Selling Today" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <!-- Merchant Banner Hero -->
    <div class="p-5 text-white rounded mb-4" style="background: linear-gradient(135deg, #131921 0%, #232F3E 100%);">
        <div class="row align-items-center">
            <div class="col-lg-7">
                <span class="badge bg-warning text-dark fw-bold px-3 py-2 mb-2"><i class="bi bi-rocket-takeoff-fill me-1"></i> GROW WITH HARMESHMART</span>
                <h1 class="fw-bold mb-3" style="font-size: 34px;">Sell to Millions of Customers Across India</h1>
                <p class="fs-5 text-white-50 mb-4">Launch your digital storefront in minutes. Enjoy competitive fee structures, doorstep logistics, and instant revenue analytics.</p>
            </div>
            <div class="col-lg-5">
                <div class="bg-white p-4 rounded text-dark shadow-lg">
                    <h4 class="fw-bold mb-3 text-dark">Register Your Store</h4>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small mb-3">
                            <i class="bi bi-exclamation-circle-fill me-1"></i><c:out value="${errorMessage}"/>
                        </div>
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty currentUser}">
                            <form action="${pageContext.request.contextPath}/become-seller" method="POST">
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Merchant Store Name</label>
                                    <input type="text" name="storeName" class="form-control form-control-sm" placeholder="e.g. Apex Tech World" required autofocus>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Business Phone Number</label>
                                    <input type="tel" name="phone" class="form-control form-control-sm" placeholder="+91 9876543210" value="<c:out value='${currentUser.phone}'/>" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Store Description / Product Specialization</label>
                                    <textarea name="storeDescription" class="form-control form-control-sm" rows="3" placeholder="Briefly describe your merchandise line..."></textarea>
                                </div>
                                <button type="submit" class="hm-btn-orange py-2 fs-6 fw-bold">
                                    Start Selling on HarmeshMart
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="text-secondary small mb-3">You need an active HarmeshMart account to launch a seller profile.</p>
                            <a href="${pageContext.request.contextPath}/login?redirect=/become-seller" class="btn btn-warning fw-bold w-100 py-2 mb-2">
                                Sign In to Continue
                            </a>
                            <a href="${pageContext.request.contextPath}/register?role=SELLER" class="btn btn-outline-dark w-100 py-2 small">
                                Create a New Seller Account
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Value Props Grid -->
    <div class="row g-4 text-center my-4">
        <div class="col-md-4">
            <div class="card h-100 p-4 border shadow-sm bg-white rounded">
                <i class="bi bi-currency-rupee fs-1 text-success mb-2"></i>
                <h5 class="fw-bold">Fast, Secure Payouts</h5>
                <p class="text-muted small">Funds deposited directly to your bank account with low commission rates and transparent reporting.</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 p-4 border shadow-sm bg-white rounded">
                <i class="bi bi-truck fs-1 text-warning mb-2"></i>
                <h5 class="fw-bold">HarmeshMart Fulfillment</h5>
                <p class="text-muted small">Store, pick, pack, and dispatch your goods nationwide with automated tracking and customer returns handling.</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 p-4 border shadow-sm bg-white rounded">
                <i class="bi bi-graph-up-arrow fs-1 text-primary mb-2"></i>
                <h5 class="fw-bold">Real-Time Analytics</h5>
                <p class="text-muted small">Access customer demand trends, stock warnings, and revenue metrics directly from your dedicated Seller Central.</p>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
