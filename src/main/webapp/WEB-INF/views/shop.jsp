<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pageTitle" value="HarmeshMart - Deals in Electronics, Fashion, Home & Kitchen" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <!-- Hero Banner (shown when browsing root / no filter) -->
    <c:if test="${not isFiltered}">
        <div class="hm-hero-banner">
            <div class="hm-hero-content">
                <span class="hm-hero-tag"><i class="bi bi-lightning-charge-fill"></i> FESTIVE DHAMAKA SALE</span>
                <h1 class="hm-hero-title">Upgrade Your Lifestyle with Big Indian Deals</h1>
                <p class="hm-hero-desc">Up to 40% off on Flagship 5G Smartphones, Audio, Designer Fashion, Espresso Machines & Fitness Essentials.</p>
                <div class="d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/shop?category=Electronics%20%26%20Gadgets" class="btn btn-warning fw-bold px-4 py-2" style="background-color: var(--hm-gold); border: none;">Shop Tech Deals</a>
                    <a href="${pageContext.request.contextPath}/become-seller" class="btn btn-outline-light fw-bold px-4 py-2">Sell on HarmeshMart</a>
                </div>
            </div>
        </div>

        <!-- Category Showcase Pills -->
        <div class="row g-3 mb-4 mt-2 position-relative" style="z-index: 10;">
            <c:forEach var="cat" items="${categories}">
                <div class="col-6 col-md-4 col-lg-2">
                    <a href="${pageContext.request.contextPath}/shop?category=<c:out value='${cat.name}'/>" class="text-decoration-none">
                        <div class="card h-100 text-center p-3 border-0 shadow-sm bg-white" style="border-radius: 8px; transition: transform 0.2s ease;">
                            <i class="bi <c:out value='${cat.icon}'/> fs-1 text-warning mb-2"></i>
                            <h6 class="fw-bold text-dark mb-1" style="font-size: 13px;"><c:out value="${cat.name}"/></h6>
                            <small class="text-muted"><c:out value="${cat.productCount}"/> items</small>
                        </div>
                    </a>
                </div>
            </c:forEach>
            <div class="col-6 col-md-4 col-lg-2">
                <a href="${pageContext.request.contextPath}/shop?sort=rating" class="text-decoration-none">
                    <div class="card h-100 text-center p-3 border-0 shadow-sm bg-white" style="border-radius: 8px;">
                        <i class="bi bi-award fs-1 text-danger mb-2"></i>
                        <h6 class="fw-bold text-dark mb-1" style="font-size: 13px;">Top Rated</h6>
                        <small class="text-muted">4.8+ Stars</small>
                    </div>
                </a>
            </div>
        </div>
    </c:if>

    <!-- Main Content Layout: Sidebar + Product Grid -->
    <div class="row g-4 ${not isFiltered ? 'mt-4' : 'mt-2'}">
        <!-- Sidebar Filter -->
        <aside class="col-lg-3 col-md-4">
            <div class="bg-white p-3 rounded border border-light-subtle shadow-sm mb-4">
                <h6 class="fw-bold border-bottom pb-2 mb-3">Department</h6>
                <ul class="list-unstyled mb-4" style="font-size: 14px;">
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/shop" class="${selectedCategory eq 'all' ? 'fw-bold text-dark' : 'text-secondary'}">
                            <i class="bi bi-chevron-right me-1" style="font-size: 10px;"></i> All Categories
                        </a>
                    </li>
                    <c:forEach var="cat" items="${categories}">
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/shop?category=<c:out value='${cat.name}'/>" 
                               class="${selectedCategory eq cat.name ? 'fw-bold text-dark' : 'text-secondary'}">
                                <i class="bi bi-chevron-right me-1" style="font-size: 10px;"></i> <c:out value="${cat.name}"/>
                            </a>
                        </li>
                    </c:forEach>
                </ul>

                <h6 class="fw-bold border-bottom pb-2 mb-3">Price Range (₹)</h6>
                <form action="${pageContext.request.contextPath}/shop" method="GET" class="mb-4">
                    <input type="hidden" name="category" value="<c:out value='${selectedCategory}'/>">
                    <input type="hidden" name="keyword" value="<c:out value='${searchKeyword}'/>">
                    <input type="hidden" name="sort" value="<c:out value='${selectedSort}'/>">
                    <div class="d-flex align-items-center gap-2 mb-2">
                        <input type="number" name="minPrice" placeholder="Min ₹" class="form-control form-control-sm" value="<c:out value='${selectedMinPrice}'/>">
                        <span>to</span>
                        <input type="number" name="maxPrice" placeholder="Max ₹" class="form-control form-control-sm" value="<c:out value='${selectedMaxPrice}'/>">
                    </div>
                    <button type="submit" class="btn btn-outline-dark btn-sm w-100">Apply Filter</button>
                </form>

                <h6 class="fw-bold border-bottom pb-2 mb-3">Customer Reviews</h6>
                <ul class="list-unstyled mb-4" style="font-size: 13px;">
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/shop?sort=rating" class="text-decoration-none">
                            <span class="hm-stars">★★★★☆</span> <span class="text-dark">& Up</span>
                        </a>
                    </li>
                </ul>

                <h6 class="fw-bold border-bottom pb-2 mb-3">Special Deals</h6>
                <div class="d-flex flex-column gap-2" style="font-size: 13px;">
                    <a href="${pageContext.request.contextPath}/shop?maxPrice=1000" class="text-secondary">Under ₹1,000</a>
                    <a href="${pageContext.request.contextPath}/shop?minPrice=1000&maxPrice=5000" class="text-secondary">₹1,000 - ₹5,000</a>
                    <a href="${pageContext.request.contextPath}/shop?minPrice=5000" class="text-secondary">Over ₹5,000</a>
                </div>
            </div>
        </aside>

        <!-- Product Listings -->
        <section class="col-lg-9 col-md-8">
            <!-- Results Header & Sorting Bar -->
            <div class="bg-white p-3 rounded border border-light-subtle shadow-sm mb-3 d-flex flex-wrap justify-content-between align-items-center gap-2">
                <div>
                    <span class="text-muted" style="font-size: 14px;">
                        Showing <strong><c:out value="${fn:length(products)}"/></strong> results
                        <c:if test="${not empty searchKeyword}">
                            for "<span class="text-danger fw-bold"><c:out value="${searchKeyword}"/></span>"
                        </c:if>
                        <c:if test="${selectedCategory ne 'all' and not empty selectedCategory}">
                            in <span class="fw-bold text-dark"><c:out value="${selectedCategory}"/></span>
                        </c:if>
                    </span>
                </div>

                <form action="${pageContext.request.contextPath}/shop" method="GET" class="d-flex align-items-center gap-2">
                    <input type="hidden" name="category" value="<c:out value='${selectedCategory}'/>">
                    <input type="hidden" name="keyword" value="<c:out value='${searchKeyword}'/>">
                    <input type="hidden" name="minPrice" value="<c:out value='${selectedMinPrice}'/>">
                    <input type="hidden" name="maxPrice" value="<c:out value='${selectedMaxPrice}'/>">
                    <label for="sortSelect" class="text-muted small text-nowrap">Sort by:</label>
                    <select id="sortSelect" name="sort" class="form-select form-select-sm" onchange="this.form.submit();">
                        <option value="featured" ${selectedSort eq 'featured' ? 'selected' : ''}>Featured</option>
                        <option value="price_asc" ${selectedSort eq 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                        <option value="price_desc" ${selectedSort eq 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                        <option value="rating" ${selectedSort eq 'rating' ? 'selected' : ''}>Avg. Customer Review</option>
                        <option value="newest" ${selectedSort eq 'newest' ? 'selected' : ''}>Newest Arrivals</option>
                    </select>
                </form>
            </div>

            <!-- Product Grid -->
            <c:choose>
                <c:when test="${not empty products}">
                    <div class="hm-product-grid">
                        <c:forEach var="p" items="${products}">
                            <div class="hm-product-card">
                                <div>
                                    <!-- Image Box -->
                                    <div class="hm-card-img-wrap">
                                        <a href="${pageContext.request.contextPath}/product?id=<c:out value='${p.id}'/>">
                                            <img src="<c:out value='${p.imageUrl}'/>" alt="<c:out value='${p.name}'/>" class="hm-card-img" loading="lazy">
                                        </a>
                                    </div>

                                    <!-- Category & Title -->
                                    <div class="hm-product-category"><c:out value="${p.category}"/></div>
                                    <h3 class="hm-product-title">
                                        <a href="${pageContext.request.contextPath}/product?id=<c:out value='${p.id}'/>">
                                            <c:out value="${p.name}"/>
                                        </a>
                                    </h3>

                                    <!-- Rating -->
                                    <div class="hm-rating-row">
                                        <span class="hm-stars">
                                            <c:choose>
                                                <c:when test="${p.rating >= 4.5}">★★★★★</c:when>
                                                <c:when test="${p.rating >= 3.5}">★★★★☆</c:when>
                                                <c:when test="${p.rating >= 2.5}">★★★☆☆</c:when>
                                                <c:otherwise>★★☆☆☆</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="hm-rating-count">(<c:out value="${p.reviewCount}"/>)</span>
                                    </div>

                                    <!-- Pricing -->
                                    <div class="hm-price-row">
                                        <span class="hm-current-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0"/></span>
                                        <c:if test="${p.originalPrice != null and p.originalPrice > p.price}">
                                            <span class="hm-original-price">₹<fmt:formatNumber value="${p.originalPrice}" pattern="#,##0"/></span>
                                            <span class="hm-discount-badge">(<c:out value="${p.discountPercentage}"/>% off)</span>
                                        </c:if>
                                    </div>

                                    <!-- Delivery & Stock Status -->
                                    <div class="mb-2" style="font-size: 12px; color: var(--hm-text-muted);">
                                        <i class="bi bi-check2-circle text-primary"></i> <strong>FREE Delivery</strong> by Tomorrow
                                    </div>
                                    <div class="hm-stock-status">
                                        <c:choose>
                                            <c:when test="${p.stockQty > 5}">
                                                <span class="hm-stock-in"><i class="bi bi-check-circle-fill"></i> In Stock</span>
                                            </c:when>
                                            <c:when test="${p.stockQty > 0}">
                                                <span class="hm-stock-low"><i class="bi bi-exclamation-triangle-fill"></i> Only <c:out value="${p.stockQty}"/> left!</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-danger"><i class="bi bi-x-circle-fill"></i> Out of Stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <!-- Action Buttons -->
                                <div class="hm-card-actions">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="POST">
                                        <input type="hidden" name="productId" value="<c:out value='${p.id}'/>">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="hm-btn-primary" ${p.stockQty <= 0 ? 'disabled' : ''}>
                                            <i class="bi bi-cart-plus me-1"></i> Add to Cart
                                        </button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/cart/add" method="POST">
                                        <input type="hidden" name="productId" value="<c:out value='${p.id}'/>">
                                        <input type="hidden" name="quantity" value="1">
                                        <input type="hidden" name="buyNow" value="true">
                                        <button type="submit" class="hm-btn-orange" ${p.stockQty <= 0 ? 'disabled' : ''}>
                                            <i class="bi bi-lightning-fill me-1"></i> Buy Now
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="bg-white p-5 rounded text-center border shadow-sm">
                        <i class="bi bi-search fs-1 text-muted mb-3"></i>
                        <h4>No matching products found</h4>
                        <p class="text-muted">Try checking your spelling or use more general terms.</p>
                        <a href="${pageContext.request.contextPath}/shop" class="btn btn-warning mt-2">Clear Filters & View All Products</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
