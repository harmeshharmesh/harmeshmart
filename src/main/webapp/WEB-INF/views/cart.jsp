<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="pageTitle" value="HarmeshMart Shopping Cart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <div class="hm-cart-layout">
        <!-- Left: Cart Items List -->
        <div class="hm-cart-panel shadow-sm">
            <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                <h2 class="fw-bold mb-0" style="font-size: 26px;">Shopping Cart</h2>
                <span class="text-muted small">Price</span>
            </div>

            <c:choose>
                <c:when test="${not empty cartSummary.items}">
                    <c:forEach var="item" items="${cartSummary.items}">
                        <div class="hm-cart-item-row">
                            <img src="<c:out value='${item.imageUrl}'/>" alt="<c:out value='${item.productName}'/>" class="hm-cart-item-img">
                            <div class="flex-grow-1">
                                <h5 class="mb-1" style="font-size: 16px;">
                                    <a href="${pageContext.request.contextPath}/product?id=<c:out value='${item.productId}'/>" class="text-dark fw-bold">
                                        <c:out value="${item.productName}"/>
                                    </a>
                                </h5>
                                <div class="text-success small fw-semibold mb-2">
                                    <i class="bi bi-check-circle-fill"></i> In Stock
                                </div>
                                <div class="text-muted small mb-3">Eligible for FREE Shipping</div>

                                <!-- Quantity, Delete, and Save for later actions -->
                                <div class="d-flex flex-wrap align-items-center gap-3">
                                    <!-- Qty Selector -->
                                    <form action="${pageContext.request.contextPath}/cart/update" method="POST" class="d-flex align-items-center gap-1">
                                        <input type="hidden" name="cartItemId" value="<c:out value='${item.cartItemId}'/>">
                                        <input type="hidden" name="promoCode" value="<c:out value='${cartSummary.promoCode}'/>">
                                        <label class="small text-muted mb-0">Qty:</label>
                                        <select name="quantity" class="form-select form-select-sm hm-cart-qty-select" style="width: 75px;">
                                            <c:forEach var="q" begin="1" end="10">
                                                <option value="${q}" ${item.quantity eq q ? 'selected' : ''}>${q}</option>
                                            </c:forEach>
                                        </select>
                                    </form>

                                    <!-- Delete Button -->
                                    <form action="${pageContext.request.contextPath}/cart/remove" method="POST">
                                        <input type="hidden" name="cartItemId" value="<c:out value='${item.cartItemId}'/>">
                                        <button type="submit" class="btn btn-link btn-sm text-danger p-0 text-decoration-none">
                                            <i class="bi bi-trash"></i> Delete
                                        </button>
                                    </form>

                                    <!-- Save to Wishlist -->
                                    <form action="${pageContext.request.contextPath}/wishlist/add" method="POST">
                                        <input type="hidden" name="productId" value="<c:out value='${item.productId}'/>">
                                        <button type="submit" class="btn btn-link btn-sm text-primary p-0 text-decoration-none">
                                            <i class="bi bi-heart"></i> Save for later
                                        </button>
                                    </form>
                                </div>
                            </div>

                            <!-- Item Total -->
                            <div class="text-end">
                                <div class="fw-bold fs-5 text-dark">
                                    ₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0"/>
                                </div>
                                <c:if test="${item.quantity > 1}">
                                    <small class="text-muted">₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/> each</small>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Subtotal bar at bottom of cart -->
                    <div class="text-end pt-3 border-top mt-3">
                        <span class="fs-5">Subtotal (<c:out value="${cartSummary.totalItems}"/> items): </span>
                        <span class="fs-4 fw-bold text-dark">₹<fmt:formatNumber value="${cartSummary.subtotal}" pattern="#,##0"/></span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5">
                        <i class="bi bi-cart-x fs-1 text-muted mb-3"></i>
                        <h4>Your HarmeshMart Cart is empty</h4>
                        <p class="text-muted">Your shopping cart is waiting. Give it purpose — fill it with electronics, fashion, and home supplies.</p>
                        <a href="${pageContext.request.contextPath}/shop" class="btn btn-warning fw-bold px-4 py-2 mt-2">Continue Shopping</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Right: Checkout & Promo Code Panel -->
        <c:if test="${not empty cartSummary.items}">
            <div>
                <div class="bg-white p-4 rounded border border-light-subtle shadow-sm mb-4">
                    <!-- Free shipping indicator -->
                    <c:choose>
                        <c:when test="${cartSummary.subtotal >= 999}">
                            <div class="text-success small fw-semibold mb-2">
                                <i class="bi bi-check-circle-fill"></i> Your order is eligible for <strong>FREE Delivery</strong>.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-muted small mb-2">
                                Add <strong class="text-dark">₹<fmt:formatNumber value="${999 - cartSummary.subtotal}" pattern="#,##0"/></strong> of eligible items to qualify for <strong>FREE Delivery</strong>.
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="fs-5 mb-3">
                        Subtotal (<c:out value="${cartSummary.totalItems}"/> items):
                        <div class="fs-3 fw-bold text-dark">₹<fmt:formatNumber value="${cartSummary.subtotal}" pattern="#,##0"/></div>
                    </div>

                    <!-- Proceed to Checkout Button -->
                    <a href="${pageContext.request.contextPath}/checkout" class="hm-btn-primary d-block text-center py-2 fs-6 mb-3">
                        Proceed to Checkout
                    </a>

                    <!-- Summary details -->
                    <div class="border-top pt-3" style="font-size: 13px;">
                        <div class="d-flex justify-content-between mb-1">
                            <span>Items Subtotal:</span>
                            <span>₹<fmt:formatNumber value="${cartSummary.subtotal}" pattern="#,##0"/></span>
                        </div>
                        <div class="d-flex justify-content-between mb-1">
                            <span>Delivery Fee:</span>
                            <c:choose>
                                <c:when test="${cartSummary.shipping == 0}">
                                    <span class="text-success fw-bold">FREE</span>
                                </c:when>
                                <c:otherwise>
                                    <span>₹<fmt:formatNumber value="${cartSummary.shipping}" pattern="#,##0"/></span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <c:if test="${cartSummary.discount > 0}">
                            <div class="d-flex justify-content-between mb-1 text-success fw-bold">
                                <span>Promo Discount (<c:out value="${cartSummary.promoCode}"/>):</span>
                                <span>-₹<fmt:formatNumber value="${cartSummary.discount}" pattern="#,##0"/></span>
                            </div>
                        </c:if>
                        <div class="d-flex justify-content-between border-top pt-2 mt-2 fw-bold fs-6">
                            <span>Order Total:</span>
                            <span class="text-danger">₹<fmt:formatNumber value="${cartSummary.grandTotal}" pattern="#,##0"/></span>
                        </div>
                    </div>

                    <!-- Promo Code Form -->
                    <form action="${pageContext.request.contextPath}/cart" method="GET" class="mt-4 pt-3 border-top">
                        <label for="cartPromoInput" class="form-label small fw-bold">Apply Gift Card or Promo Code</label>
                        <div class="input-group input-group-sm">
                            <input type="text" id="cartPromoInput" name="promoCode" class="form-control" placeholder="Enter 'SAVE10'" value="<c:out value='${cartSummary.promoCode}'/>">
                            <button type="submit" class="btn btn-outline-dark">Apply</button>
                        </div>
                        <c:if test="${cartSummary.discount > 0}">
                            <div class="text-success small mt-1"><i class="bi bi-check-circle"></i> Promo code applied successfully!</div>
                        </c:if>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
