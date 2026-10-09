<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'HarmeshMart - Online Shopping for Electronics, Fashion, Home & More'}"/></title>
    <!-- Bootstrap 5 CSS & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
    <!-- HarmeshMart Theme CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <script>
        window.contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body>

<!-- Header Top Navigation -->
<header class="hm-header">
    <div class="hm-navbar-main">
        <!-- Logo with Amazon-Style Smile Arrow -->
        <a href="${pageContext.request.contextPath}/shop" class="hm-brand-logo">
            <div class="hm-logo-text">Harmesh<span>Mart</span></div>
            <svg class="hm-logo-smile" viewBox="0 0 130 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M 5 3 Q 65 18 125 3" stroke="#FF9900" stroke-width="3" stroke-linecap="round" fill="transparent"/>
                <path d="M 120 1 L 126 4 L 122 8 Z" fill="#FF9900"/>
            </svg>
        </a>

        <!-- Delivery Location Box -->
        <div class="hm-location-box d-none d-md-flex">
            <i class="bi bi-geo-alt fs-5 text-white"></i>
            <div>
                <div>Deliver to</div>
                <div class="hm-loc-strong">India, 560001</div>
            </div>
        </div>

        <!-- Global Search Bar -->
        <div class="hm-search-container">
            <form action="${pageContext.request.contextPath}/shop" method="GET" class="hm-search-form">
                <select name="category" class="hm-search-cat-select">
                    <option value="all">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="<c:out value='${cat.name}'/>" ${selectedCategory eq cat.name ? 'selected' : ''}>
                            <c:out value="${cat.name}"/>
                        </option>
                    </c:forEach>
                </select>
                <input type="text" name="keyword" class="hm-search-input" placeholder="Search HarmeshMart in Electronics, Fashion, Home..." value="<c:out value='${searchKeyword}'/>" autocomplete="off">
                <button type="submit" class="hm-search-btn" title="Search">
                    <i class="bi bi-search"></i>
                </button>
            </form>
        </div>

        <!-- Header Actions & Account Menu -->
        <div class="hm-nav-actions">
            <!-- Sell on HarmeshMart -->
            <c:if test="${empty currentUser or currentUser.role eq 'BUYER'}">
                <a href="${pageContext.request.contextPath}/become-seller" class="hm-nav-link d-none d-lg-flex">
                    <span class="hm-nav-link-sub">Opportunity</span>
                    <span class="hm-nav-link-main" style="color: var(--hm-gold);">Become a Seller</span>
                </a>
            </c:if>

            <!-- User Account Dropdown -->
            <c:choose>
                <c:when test="${not empty currentUser}">
                    <div class="dropdown">
                        <a href="#" class="hm-nav-link dropdown-toggle" data-bs-toggle="dropdown">
                            <span class="hm-nav-link-sub">Hello, <c:out value="${currentUser.name}"/></span>
                            <span class="hm-nav-link-main">Account & Lists</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end shadow">
                            <li class="dropdown-header">
                                Signed in as <strong><c:out value="${currentUser.email}"/></strong>
                                <span class="badge bg-warning text-dark ms-1"><c:out value="${currentUser.role}"/></span>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders"><i class="bi bi-box-seam me-2"></i>Your Orders</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist"><i class="bi bi-heart me-2"></i>Your Wishlist</a></li>
                            <c:if test="${currentUser.role eq 'SELLER' or currentUser.role eq 'ADMIN'}">
                                <li><a class="dropdown-item text-primary fw-bold" href="${pageContext.request.contextPath}/seller/dashboard"><i class="bi bi-shop me-2"></i>Seller Central</a></li>
                            </c:if>
                            <c:if test="${currentUser.role eq 'ADMIN'}">
                                <li><a class="dropdown-item text-danger fw-bold" href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-shield-check me-2"></i>Admin Console</a></li>
                            </c:if>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i>Sign Out</a></li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="hm-nav-link">
                        <span class="hm-nav-link-sub">Hello, sign in</span>
                        <span class="hm-nav-link-main">Account & Lists</span>
                    </a>
                </c:otherwise>
            </c:choose>

            <!-- Orders Shortcut -->
            <a href="${pageContext.request.contextPath}/orders" class="hm-nav-link d-none d-sm-flex">
                <span class="hm-nav-link-sub">Returns</span>
                <span class="hm-nav-link-main">& Orders</span>
            </a>

            <!-- Wishlist Shortcut -->
            <a href="${pageContext.request.contextPath}/wishlist" class="hm-nav-link d-none d-md-flex position-relative">
                <span class="hm-nav-link-sub">Saved</span>
                <span class="hm-nav-link-main"><i class="bi bi-heart"></i> Wishlist (<c:out value="${wishlistCount != null ? wishlistCount : 0}"/>)</span>
            </a>

            <!-- Cart Button -->
            <a href="${pageContext.request.contextPath}/cart" class="hm-cart-btn">
                <i class="bi bi-cart3 fs-3"></i>
                <span class="hm-cart-badge"><c:out value="${cartCount != null ? cartCount : 0}"/></span>
                <span class="d-none d-md-inline ms-1">Cart</span>
            </a>
        </div>
    </div>

    <!-- Secondary Sub-Navbar -->
    <div class="hm-subnav">
        <a href="${pageContext.request.contextPath}/shop" class="hm-subnav-item highlight">
            <i class="bi bi-list fs-5"></i> All Departments
        </a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/shop?category=<c:out value='${cat.name}'/>" class="hm-subnav-item">
                <c:out value="${cat.name}"/>
            </a>
        </c:forEach>
        <a href="${pageContext.request.contextPath}/shop?sort=rating" class="hm-subnav-item">
            <i class="bi bi-fire text-warning"></i> Best Sellers
        </a>
        <a href="${pageContext.request.contextPath}/shop?maxPrice=4999" class="hm-subnav-item">
            <i class="bi bi-percent text-warning"></i> Today's Deals
        </a>
        <c:if test="${empty currentUser or currentUser.role eq 'BUYER'}">
            <a href="${pageContext.request.contextPath}/become-seller" class="hm-subnav-item text-warning fw-bold">
                <i class="bi bi-shop"></i> Sell on HarmeshMart
            </a>
        </c:if>
        <a href="javascript:void(0);" onclick="document.getElementById('hmChatbotFab').click();" class="hm-subnav-item">
            <i class="bi bi-robot text-info"></i> AI Customer Assistant
        </a>
    </div>
</header>
