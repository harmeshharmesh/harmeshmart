<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="pageTitle" value="Your Wishlist - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h2 class="fw-bold mb-1" style="font-size: 26px;">Your Wishlist / Saved Items</h2>
            <div class="text-muted small">Items you saved for later purchase or price monitoring.</div>
        </div>
        <span class="badge bg-secondary fs-6"><c:out value="${fn:length(wishlist)}"/> items</span>
    </div>

    <c:choose>
        <c:when test="${not empty wishlist}">
            <div class="row g-3">
                <c:forEach var="w" items="${wishlist}">
                    <div class="col-md-6 col-lg-4">
                        <div class="card h-100 border shadow-sm p-3 bg-white" style="border-radius: 8px;">
                            <div class="d-flex gap-3">
                                <img src="<c:out value='${w.product.imageUrl}'/>" alt="" style="width: 100px; height: 100px; object-fit: contain;" class="border rounded p-1 bg-light">
                                <div class="flex-grow-1">
                                    <div class="text-muted small"><c:out value="${w.product.category}"/></div>
                                    <h6 class="fw-bold mb-1 text-truncate" style="max-width: 200px;">
                                        <a href="${pageContext.request.contextPath}/product?id=<c:out value='${w.productId}'/>" class="text-dark">
                                            <c:out value="${w.product.name}"/>
                                        </a>
                                    </h6>
                                    <div class="fw-bold text-dark fs-5 mb-2">
                                        ₹<fmt:formatNumber value="${w.product.price}" pattern="#,##0"/>
                                    </div>

                                    <div class="d-flex gap-2">
                                        <!-- Move to Cart -->
                                        <form action="${pageContext.request.contextPath}/wishlist/move-to-cart" method="POST">
                                            <input type="hidden" name="productId" value="<c:out value='${w.productId}'/>">
                                            <button type="submit" class="hm-btn-primary btn-sm py-1 px-3">
                                                <i class="bi bi-cart-plus me-1"></i> Move to Cart
                                            </button>
                                        </form>

                                        <!-- Remove from Wishlist -->
                                        <form action="${pageContext.request.contextPath}/wishlist/remove" method="POST">
                                            <input type="hidden" name="productId" value="<c:out value='${w.productId}'/>">
                                            <button type="submit" class="btn btn-outline-danger btn-sm py-1">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="bg-white p-5 rounded text-center border shadow-sm">
                <i class="bi bi-heart fs-1 text-muted mb-3"></i>
                <h4>Your Wishlist is currently empty</h4>
                <p class="text-muted">Explore items in Electronics, Fashion, Books, and Sports and click "Add to Wishlist" to save them here.</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-warning fw-bold px-4 py-2 mt-2">Discover Products</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
