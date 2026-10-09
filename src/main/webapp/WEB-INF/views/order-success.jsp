<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pageTitle" value="Order Confirmation #${order.id} - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <div class="card border-0 shadow-sm p-4 bg-white mb-4" style="border-radius: 8px;">
        <!-- Amazon-style Green Checkmark Header -->
        <div class="d-flex align-items-center gap-3 border-bottom pb-3 mb-4">
            <i class="bi bi-check-circle-fill text-success" style="font-size: 48px;"></i>
            <div>
                <h2 class="fw-bold text-dark mb-0">Order Placed, thank you!</h2>
                <div class="text-muted small">Confirmation will be sent to your registered email: <strong><c:out value="${order.buyerEmail != null ? order.buyerEmail : currentUser.email}"/></strong></div>
            </div>
        </div>

        <!-- Tracking Status Bar -->
        <div class="bg-light p-4 rounded mb-4 border">
            <h6 class="fw-bold mb-3 text-secondary text-uppercase" style="font-size: 12px; letter-spacing: 0.5px;">Order Tracking Status</h6>
            <div class="position-relative m-4">
                <div class="progress" style="height: 6px;">
                    <div class="progress-bar bg-warning" role="progressbar" 
                         style="width: ${order.status eq 'DELIVERED' ? '100%' : (order.status eq 'SHIPPED' ? '70%' : (order.status eq 'CONFIRMED' ? '35%' : '10%'))};">
                    </div>
                </div>
                <div class="d-flex justify-content-between position-absolute top-0 start-0 w-100 translate-middle-y">
                    <button type="button" class="btn btn-sm btn-warning rounded-pill" style="width: 2rem; height:2rem;"><i class="bi bi-clock-history"></i></button>
                    <button type="button" class="btn btn-sm ${order.status ne 'PENDING' ? 'btn-warning' : 'btn-secondary'} rounded-pill" style="width: 2rem; height:2rem;"><i class="bi bi-check2"></i></button>
                    <button type="button" class="btn btn-sm ${order.status eq 'SHIPPED' or order.status eq 'DELIVERED' ? 'btn-warning' : 'btn-secondary'} rounded-pill" style="width: 2rem; height:2rem;"><i class="bi bi-truck"></i></button>
                    <button type="button" class="btn btn-sm ${order.status eq 'DELIVERED' ? 'btn-success' : 'btn-secondary'} rounded-pill" style="width: 2rem; height:2rem;"><i class="bi bi-box2"></i></button>
                </div>
            </div>
            <div class="d-flex justify-content-between text-muted small mt-2 px-1">
                <span>Pending</span>
                <span>Confirmed</span>
                <span>Shipped</span>
                <span>Delivered</span>
            </div>
        </div>

        <!-- Order Information Summary -->
        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <h6 class="fw-bold mb-2">Shipping Address</h6>
                <p class="text-secondary small mb-0"><c:out value="${order.shippingAddress}"/></p>
            </div>
            <div class="col-md-4">
                <h6 class="fw-bold mb-2">Payment Method</h6>
                <p class="text-secondary small mb-1">
                    <span class="badge bg-secondary"><c:out value="${order.paymentMethod}"/></span>
                    <span class="badge ${order.paymentStatus eq 'PAID' ? 'bg-success' : 'bg-warning text-dark'} ms-1"><c:out value="${order.paymentStatus}"/></span>
                </p>
                <c:if test="${not empty order.promoCode}">
                    <div class="small text-success">Promo Applied: <c:out value="${order.promoCode}"/></div>
                </c:if>
            </div>
            <div class="col-md-4">
                <h6 class="fw-bold mb-2">Order Summary</h6>
                <div class="d-flex justify-content-between small text-secondary mb-1">
                    <span>Order Date:</span>
                    <span><fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></span>
                </div>
                <div class="d-flex justify-content-between fw-bold text-dark fs-6 pt-2 border-top">
                    <span>Total Paid:</span>
                    <span class="text-danger">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/></span>
                </div>
            </div>
        </div>

        <!-- Items Ordered -->
        <h6 class="fw-bold mb-3 border-top pt-3">Items in this shipment:</h6>
        <div class="row g-3 mb-4">
            <c:forEach var="it" items="${order.items}">
                <div class="col-md-6">
                    <div class="d-flex align-items-center gap-3 p-3 border rounded">
                        <img src="<c:out value='${it.productImageUrl}'/>" alt="" style="width: 70px; height: 70px; object-fit: contain;">
                        <div>
                            <h6 class="mb-1 text-dark small fw-bold"><c:out value="${it.productName}"/></h6>
                            <div class="small text-muted">Qty: <c:out value="${it.quantity}"/> × ₹<fmt:formatNumber value="${it.unitPrice}" pattern="#,##0"/></div>
                            <div class="small text-success"><i class="bi bi-truck"></i> Estimated delivery: Tomorrow</div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Action Links -->
        <div class="d-flex flex-wrap gap-3 border-top pt-3">
            <a href="${pageContext.request.contextPath}/orders/invoice?id=<c:out value='${order.id}'/>" target="_blank" class="btn btn-outline-dark">
                <i class="bi bi-printer me-1"></i> Print / Download Tax Invoice
            </a>
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-warning fw-bold">
                <i class="bi bi-box-seam me-1"></i> View All Orders
            </a>
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-outline-secondary">
                Continue Shopping
            </a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
