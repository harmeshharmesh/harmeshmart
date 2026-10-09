<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pageTitle" value="${product.name} - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <!-- Breadcrumb -->
    <nav class="mb-3" style="font-size: 13px;">
        <a href="${pageContext.request.contextPath}/shop" class="text-muted">Home</a>
        <span class="mx-1 text-muted">›</span>
        <a href="${pageContext.request.contextPath}/shop?category=<c:out value='${product.category}'/>" class="text-muted"><c:out value="${product.category}"/></a>
        <span class="mx-1 text-muted">›</span>
        <span class="text-dark fw-semibold"><c:out value="${product.name}"/></span>
    </nav>

    <!-- Product 3-Column Layout -->
    <div class="hm-product-detail-layout">
        <!-- Col 1: High-Res Image Box -->
        <div>
            <div class="hm-detail-img-box">
                <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>" id="mainProductImg">
            </div>
            <div class="text-center text-muted small mt-2">
                <i class="bi bi-zoom-in"></i> High-Resolution Product Preview
            </div>
        </div>

        <!-- Col 2: Product Specifications & Details -->
        <div>
            <span class="badge bg-secondary mb-2"><c:out value="${product.category}"/></span>
            <h1 class="fw-bold mb-2" style="font-size: 24px; line-height: 1.3;"><c:out value="${product.name}"/></h1>

            <!-- Seller link -->
            <div class="mb-3 text-muted small">
                Brand/Sold by: 
                <span class="text-primary fw-bold">
                    <c:out value="${product.sellerStoreName != null ? product.sellerStoreName : 'HarmeshMart Verified Merchant'}"/>
                </span>
                <span class="badge bg-success-subtle text-success ms-1"><i class="bi bi-patch-check-fill"></i> Verified</span>
            </div>

            <!-- Rating breakdown -->
            <div class="d-flex align-items-center gap-2 mb-3 pb-3 border-bottom">
                <span class="hm-stars fs-5">
                    <c:choose>
                        <c:when test="${product.rating >= 4.5}">★★★★★</c:when>
                        <c:when test="${product.rating >= 3.5}">★★★★☆</c:when>
                        <c:when test="${product.rating >= 2.5}">★★★☆☆</c:when>
                        <c:otherwise>★★☆☆☆</c:otherwise>
                    </c:choose>
                </span>
                <span class="fw-bold text-dark"><fmt:formatNumber value="${product.rating}" pattern="0.0"/> out of 5</span>
                <span class="text-muted">|</span>
                <a href="#reviews" class="text-decoration-none"><c:out value="${product.reviewCount}"/> ratings</a>
            </div>

            <!-- Price Breakdown -->
            <div class="mb-4">
                <div class="d-flex align-items-baseline gap-2">
                    <c:if test="${product.discountPercentage > 0}">
                        <span class="text-danger fs-3 fw-bold">-<c:out value="${product.discountPercentage}"/>%</span>
                    </c:if>
                    <span class="fs-2 fw-bold text-dark">₹<fmt:formatNumber value="${product.price}" pattern="#,##0"/></span>
                </div>
                <c:if test="${product.originalPrice != null and product.originalPrice > product.price}">
                    <div class="text-muted small">
                        M.R.P.: <span class="text-decoration-line-through">₹<fmt:formatNumber value="${product.originalPrice}" pattern="#,##0"/></span>
                    </div>
                </c:if>
                <div class="text-success small fw-semibold mt-1">Inclusive of all taxes</div>
            </div>

            <!-- Feature Badges -->
            <div class="row g-2 text-center mb-4 py-2 border-top border-bottom bg-light rounded">
                <div class="col-3">
                    <i class="bi bi-truck fs-4 text-warning"></i>
                    <div class="small fw-semibold mt-1">Free Delivery</div>
                </div>
                <div class="col-3">
                    <i class="bi bi-arrow-repeat fs-4 text-warning"></i>
                    <div class="small fw-semibold mt-1">7 Days Return</div>
                </div>
                <div class="col-3">
                    <i class="bi bi-shield-check fs-4 text-warning"></i>
                    <div class="small fw-semibold mt-1">1 Year Warranty</div>
                </div>
                <div class="col-3">
                    <i class="bi bi-lock fs-4 text-warning"></i>
                    <div class="small fw-semibold mt-1">Secure Transaction</div>
                </div>
            </div>

            <!-- Description -->
            <div>
                <h6 class="fw-bold mb-2">About this item:</h6>
                <p class="text-secondary" style="font-size: 14px; line-height: 1.7; white-space: pre-line;">
                    <c:out value="${product.description}"/>
                </p>
            </div>
        </div>

        <!-- Col 3: Buy Box (Order Action Card) -->
        <div>
            <div class="hm-buy-box shadow-sm">
                <div class="fs-3 fw-bold text-dark mb-2">
                    ₹<fmt:formatNumber value="${product.price}" pattern="#,##0"/>
                </div>

                <div class="mb-3 small">
                    <div class="text-success fw-semibold"><i class="bi bi-check2"></i> FREE delivery <strong>Tomorrow</strong></div>
                    <div class="text-muted">Order within 4 hrs 20 mins.</div>
                </div>

                <div class="mb-3">
                    <c:choose>
                        <c:when test="${product.stockQty > 5}">
                            <div class="text-success fw-bold fs-5"><i class="bi bi-check-circle"></i> In Stock</div>
                        </c:when>
                        <c:when test="${product.stockQty > 0}">
                            <div class="text-danger fw-bold fs-5"><i class="bi bi-exclamation-triangle"></i> Only <c:out value="${product.stockQty}"/> left in stock</div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-danger fw-bold fs-5"><i class="bi bi-x-circle"></i> Currently Unavailable</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="small text-muted mb-3">
                    <div>Ships from: <strong class="text-dark">HarmeshMart Logistics</strong></div>
                    <div>Sold by: <strong class="text-dark"><c:out value="${product.sellerStoreName != null ? product.sellerStoreName : 'HarmeshMart'}"/></strong></div>
                </div>

                <!-- Add to Cart Form -->
                <form action="${pageContext.request.contextPath}/cart/add" method="POST" class="mb-2">
                    <input type="hidden" name="productId" value="<c:out value='${product.id}'/>">
                    <div class="mb-3">
                        <label for="productQtySelect" class="form-label small fw-bold">Quantity:</label>
                        <select id="productQtySelect" name="quantity" class="form-select form-select-sm" ${product.stockQty <= 0 ? 'disabled' : ''}>
                            <c:forEach var="i" begin="1" end="${product.stockQty > 10 ? 10 : (product.stockQty > 0 ? product.stockQty : 1)}">
                                <option value="${i}">${i}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <button type="submit" class="hm-btn-primary mb-2" ${product.stockQty <= 0 ? 'disabled' : ''}>
                        <i class="bi bi-cart3 me-1"></i> Add to Cart
                    </button>
                </form>

                <!-- Buy Now Form -->
                <form action="${pageContext.request.contextPath}/cart/add" method="POST" class="mb-3">
                    <input type="hidden" name="productId" value="<c:out value='${product.id}'/>">
                    <input type="hidden" name="quantity" value="1">
                    <input type="hidden" name="buyNow" value="true">
                    <button type="submit" class="hm-btn-orange mb-2" ${product.stockQty <= 0 ? 'disabled' : ''}>
                        <i class="bi bi-lightning-fill me-1"></i> Buy Now
                    </button>
                </form>

                <!-- Wishlist Toggle -->
                <form action="${pageContext.request.contextPath}/wishlist/add" method="POST">
                    <input type="hidden" name="productId" value="<c:out value='${product.id}'/>">
                    <button type="submit" class="hm-btn-outline w-100">
                        <i class="bi ${inWishlist ? 'bi-heart-fill text-danger' : 'bi-heart'} me-1"></i>
                        ${inWishlist ? 'Added to Wishlist' : 'Add to Wishlist'}
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Related Products in Category -->
    <c:if test="${not empty relatedProducts and fn:length(relatedProducts) > 1}">
        <div class="bg-white p-4 rounded border shadow-sm mb-4">
            <h5 class="fw-bold mb-3 border-bottom pb-2">Related items you may also like</h5>
            <div class="row g-3">
                <c:forEach var="rel" items="${relatedProducts}">
                    <c:if test="${rel.id ne product.id}">
                        <div class="col-6 col-md-3 col-lg-2">
                            <div class="card h-100 border-0 text-center p-2">
                                <a href="${pageContext.request.contextPath}/product?id=<c:out value='${rel.id}'/>">
                                    <img src="<c:out value='${rel.imageUrl}'/>" alt="<c:out value='${rel.name}'/>" class="img-fluid mb-2" style="height: 120px; object-fit: contain;">
                                </a>
                                <h6 class="small fw-semibold text-truncate mb-1"><a href="${pageContext.request.contextPath}/product?id=<c:out value='${rel.id}'/>" class="text-dark"><c:out value="${rel.name}"/></a></h6>
                                <div class="fw-bold text-dark small">₹<fmt:formatNumber value="${rel.price}" pattern="#,##0"/></div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </c:if>

    <!-- Customer Reviews Section -->
    <div id="reviews" class="bg-white p-4 rounded border shadow-sm mb-4">
        <div class="row">
            <!-- Review Summary & Write Review Form -->
            <div class="col-md-5 pe-md-4 border-end">
                <h4 class="fw-bold mb-3">Customer Reviews</h4>
                <div class="d-flex align-items-center gap-2 mb-2">
                    <span class="hm-stars fs-4">
                        <c:choose>
                            <c:when test="${product.rating >= 4.5}">★★★★★</c:when>
                            <c:when test="${product.rating >= 3.5}">★★★★☆</c:when>
                            <c:when test="${product.rating >= 2.5}">★★★☆☆</c:when>
                            <c:otherwise>★★☆☆☆</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="fs-4 fw-bold"><fmt:formatNumber value="${product.rating}" pattern="0.0"/></span>
                    <span class="text-muted">out of 5</span>
                </div>
                <p class="text-muted small mb-4"><c:out value="${product.reviewCount}"/> global customer ratings</p>

                <!-- Review Form -->
                <div class="card p-3 bg-light border-0">
                    <h6 class="fw-bold mb-2">Review this product</h6>
                    <p class="text-muted small mb-3">Share your thoughts with other customers</p>

                    <c:choose>
                        <c:when test="${not empty currentUser}">
                            <form action="${pageContext.request.contextPath}/reviews/add" method="POST">
                                <input type="hidden" name="productId" value="<c:out value='${product.id}'/>">
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Overall Rating</label>
                                    <select name="rating" class="form-select form-select-sm">
                                        <option value="5">★★★★★ 5 Stars (Excellent)</option>
                                        <option value="4">★★★★☆ 4 Stars (Good)</option>
                                        <option value="3">★★★☆☆ 3 Stars (Average)</option>
                                        <option value="2">★★☆☆☆ 2 Stars (Poor)</option>
                                        <option value="1">★☆☆☆☆ 1 Star (Very Bad)</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label small fw-bold">Write your review</label>
                                    <textarea name="comment" class="form-control form-control-sm" rows="3" placeholder="What did you like or dislike? How was the delivery?" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-warning btn-sm w-100 fw-bold">Submit Customer Review</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login?redirect=/product?id=<c:out value='${product.id}'/>" class="btn btn-outline-dark btn-sm w-100">
                                Sign in to write a customer review
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Customer Review Feed -->
            <div class="col-md-7 ps-md-4 mt-4 mt-md-0">
                <h5 class="fw-bold mb-3">Top reviews from India</h5>
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <c:forEach var="rev" items="${reviews}">
                            <div class="border-bottom pb-3 mb-3">
                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <i class="bi bi-person-circle fs-5 text-secondary"></i>
                                    <strong class="text-dark small"><c:out value="${rev.userName}"/></strong>
                                </div>
                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <span class="hm-stars small">
                                        <c:choose>
                                            <c:when test="${rev.rating eq 5}">★★★★★</c:when>
                                            <c:when test="${rev.rating eq 4}">★★★★☆</c:when>
                                            <c:when test="${rev.rating eq 3}">★★★☆☆</c:when>
                                            <c:when test="${rev.rating eq 2}">★★☆☆☆</c:when>
                                            <c:otherwise>★☆☆☆☆</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="text-success small fw-semibold"><i class="bi bi-patch-check"></i> Verified Purchase</span>
                                </div>
                                <div class="text-muted small mb-2">Reviewed in India on <fmt:formatDate value="${rev.createdAt}" pattern="dd MMMM yyyy"/></div>
                                <p class="text-secondary small mb-0" style="line-height: 1.5;"><c:out value="${rev.comment}"/></p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted">
                            <i class="bi bi-chat-square-text fs-1 mb-2"></i>
                            <p>No customer reviews yet. Be the first to share your experience!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
