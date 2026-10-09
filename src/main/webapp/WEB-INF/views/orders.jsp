<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="pageTitle" value="Your Orders - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold mb-1" style="font-size: 26px;">Your Orders</h2>
            <div class="text-muted small">Manage past purchases, track active packages, and view invoices.</div>
        </div>
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-outline-dark btn-sm">
            <i class="bi bi-cart-plus me-1"></i> Continue Shopping
        </a>
    </div>

    <c:choose>
        <c:when test="${not empty orders}">
            <c:forEach var="ord" items="${orders}">
                <div class="card mb-4 border shadow-sm" style="border-radius: 8px; overflow: hidden;">
                    <!-- Order Card Header (Amazon style dark-gray header) -->
                    <div class="card-header py-3 px-4" style="background-color: #F0F2F2; font-size: 13px;">
                        <div class="row align-items-center">
                            <div class="col-6 col-md-3">
                                <div class="text-uppercase text-muted" style="font-size: 11px;">Order Placed</div>
                                <div class="fw-bold text-dark"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMMM yyyy"/></div>
                            </div>
                            <div class="col-6 col-md-2">
                                <div class="text-uppercase text-muted" style="font-size: 11px;">Total</div>
                                <div class="fw-bold text-dark">₹<fmt:formatNumber value="${ord.totalAmount}" pattern="#,##0"/></div>
                            </div>
                            <div class="col-6 col-md-4 mt-2 mt-md-0">
                                <div class="text-uppercase text-muted" style="font-size: 11px;">Ship To</div>
                                <div class="text-truncate" title="<c:out value='${ord.shippingAddress}'/>">
                                    <strong class="text-dark"><c:out value="${ord.buyerName != null ? ord.buyerName : currentUser.name}"/></strong>
                                </div>
                            </div>
                            <div class="col-6 col-md-3 text-end mt-2 mt-md-0">
                                <div class="text-uppercase text-muted" style="font-size: 11px;">Order # <c:out value="${ord.id}"/></div>
                                <div>
                                    <a href="${pageContext.request.contextPath}/orders/invoice?id=<c:out value='${ord.id}'/>" target="_blank" class="small fw-semibold text-primary">
                                        View Invoice
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Order Card Body -->
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center gap-2 mb-3">
                            <h5 class="fw-bold text-dark mb-0">
                                <c:choose>
                                    <c:when test="${ord.status eq 'DELIVERED'}">
                                        <span class="text-success"><i class="bi bi-check-circle-fill me-1"></i> Delivered</span>
                                    </c:when>
                                    <c:when test="${ord.status eq 'SHIPPED'}">
                                        <span class="text-primary"><i class="bi bi-truck me-1"></i> Package Dispatched (In Transit)</span>
                                    </c:when>
                                    <c:when test="${ord.status eq 'CONFIRMED'}">
                                        <span class="text-warning"><i class="bi bi-clock-fill me-1"></i> Order Confirmed by Merchant</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-secondary"><i class="bi bi-hourglass-split me-1"></i> Processing Order</span>
                                    </c:otherwise>
                                </c:choose>
                            </h5>
                            <span class="badge bg-secondary-subtle text-secondary"><c:out value="${ord.paymentMethod}"/></span>
                        </div>

                        <!-- Items in this order -->
                        <div class="row g-3">
                            <c:forEach var="item" items="${ord.items}">
                                <div class="col-12 col-md-8">
                                    <div class="d-flex align-items-center gap-3">
                                        <img src="<c:out value='${item.productImageUrl}'/>" alt="" style="width: 80px; height: 80px; object-fit: contain;" class="border p-1 rounded bg-light">
                                        <div>
                                            <h6 class="mb-1 fw-bold">
                                                <a href="${pageContext.request.contextPath}/product?id=<c:out value='${item.productId}'/>" class="text-dark">
                                                    <c:out value="${item.productName}"/>
                                                </a>
                                            </h6>
                                            <div class="small text-muted mb-1">Quantity: <c:out value="${item.quantity}"/> × ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/></div>
                                            <div class="small text-secondary">Sold by: <c:out value="${item.sellerStoreName != null ? item.sellerStoreName : 'HarmeshMart Seller'}"/></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-4 text-md-end d-flex flex-column justify-content-center gap-2">
                                    <!-- Buy it again button -->
                                    <form action="${pageContext.request.contextPath}/cart/add" method="POST">
                                        <input type="hidden" name="productId" value="<c:out value='${item.productId}'/>">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="hm-btn-primary btn-sm py-1" style="max-width: 200px; margin-left: auto;">
                                            <i class="bi bi-arrow-repeat me-1"></i> Buy it again
                                        </button>
                                    </form>
                                    <!-- Review Product button -->
                                    <a href="${pageContext.request.contextPath}/product?id=<c:out value='${item.productId}'/>#reviews" class="hm-btn-outline btn-sm py-1 text-decoration-none" style="max-width: 200px; margin-left: auto;">
                                        Write a product review
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="bg-white p-5 rounded text-center border shadow-sm">
                <i class="bi bi-box2 fs-1 text-muted mb-3"></i>
                <h4>No orders placed yet</h4>
                <p class="text-muted">You haven't placed any orders on HarmeshMart yet. Explore our top deals across gadgets, fashion, and home!</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-warning fw-bold px-4 py-2 mt-2">Start Shopping</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
