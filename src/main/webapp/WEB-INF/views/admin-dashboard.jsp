<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="pageTitle" value="Administrator Console - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <!-- Admin Header -->
    <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
        <div>
            <div class="badge bg-danger mb-1"><i class="bi bi-shield-lock me-1"></i> Root Administration</div>
            <h2 class="fw-bold mb-0" style="font-size: 26px;">HarmeshMart Platform Admin Console</h2>
            <div class="text-muted small">Multi-vendor supervision, merchant verifications, and catalog compliance.</div>
        </div>
        <div>
            <span class="badge bg-success-subtle text-success p-2 fs-6">System Health: UP</span>
        </div>
    </div>

    <!-- Alert Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>Admin operation executed successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Metrics Cards -->
    <div class="row g-3 mb-4">
        <div class="col-md">
            <div class="card border-0 shadow-sm p-3 bg-white text-center rounded">
                <div class="text-muted small text-uppercase">Platform Revenue</div>
                <h4 class="fw-bold text-success mt-1 mb-0">₹<fmt:formatNumber value="${stats.platformRevenue}" pattern="#,##0"/></h4>
            </div>
        </div>
        <div class="col-md">
            <div class="card border-0 shadow-sm p-3 bg-white text-center rounded">
                <div class="text-muted small text-uppercase">Total Users</div>
                <h4 class="fw-bold text-dark mt-1 mb-0"><c:out value="${stats.totalUsers}"/></h4>
            </div>
        </div>
        <div class="col-md">
            <div class="card border-0 shadow-sm p-3 bg-white text-center rounded">
                <div class="text-muted small text-uppercase">Merchants / Sellers</div>
                <h4 class="fw-bold text-primary mt-1 mb-0"><c:out value="${stats.totalSellers}"/></h4>
            </div>
        </div>
        <div class="col-md">
            <div class="card border-0 shadow-sm p-3 bg-white text-center rounded">
                <div class="text-muted small text-uppercase">Active Catalog Items</div>
                <h4 class="fw-bold text-warning mt-1 mb-0"><c:out value="${stats.totalProducts}"/></h4>
            </div>
        </div>
        <div class="col-md">
            <div class="card border-0 shadow-sm p-3 bg-white text-center rounded">
                <div class="text-muted small text-uppercase">Orders Placed</div>
                <h4 class="fw-bold text-secondary mt-1 mb-0"><c:out value="${stats.totalOrders}"/></h4>
            </div>
        </div>
    </div>

    <!-- Admin Tabs -->
    <ul class="nav nav-tabs mb-4" id="adminTabs" role="tablist">
        <li class="nav-item">
            <button class="nav-link active fw-bold text-dark" data-bs-toggle="tab" data-bs-target="#tab-sellers">
                <i class="bi bi-shop me-1"></i> Seller Verifications (<c:out value="${fn:length(sellersList)}"/>)
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link fw-bold text-dark" data-bs-toggle="tab" data-bs-target="#tab-users">
                <i class="bi bi-people me-1"></i> User Directory (<c:out value="${fn:length(usersList)}"/>)
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link fw-bold text-dark" data-bs-toggle="tab" data-bs-target="#tab-moderation">
                <i class="bi bi-shield-x me-1"></i> Catalog Moderation (<c:out value="${fn:length(productsList)}"/>)
            </button>
        </li>
        <li class="nav-item">
            <button class="nav-link fw-bold text-dark" data-bs-toggle="tab" data-bs-target="#tab-categories">
                <i class="bi bi-tags me-1"></i> Category Management (<c:out value="${fn:length(categoriesList)}"/>)
            </button>
        </li>
    </ul>

    <div class="tab-content">
        <!-- 1. Sellers Tab -->
        <div class="tab-pane fade show active" id="tab-sellers">
            <div class="card border-0 shadow-sm p-3 bg-white rounded">
                <h5 class="fw-bold mb-3">Merchant Verification & Approval</h5>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Store ID</th>
                                <th>Store Name</th>
                                <th>Owner Name</th>
                                <th>Contact Email</th>
                                <th>Phone</th>
                                <th>Verification Status</th>
                                <th class="text-end">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${sellersList}">
                                <tr>
                                    <td>#<c:out value="${s.id}"/></td>
                                    <td class="fw-bold"><c:out value="${s.storeName}"/></td>
                                    <td><c:out value="${s.ownerName}"/></td>
                                    <td><c:out value="${s.contactEmail}"/></td>
                                    <td><c:out value="${s.phone}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.verified}">
                                                <span class="badge bg-success"><i class="bi bi-check-circle"></i> Verified</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark"><i class="bi bi-hourglass-split"></i> Pending Approval</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/admin/sellers/verify" method="POST" class="d-inline">
                                            <input type="hidden" name="sellerId" value="<c:out value='${s.id}'/>">
                                            <input type="hidden" name="verified" value="${s.verified ? 'false' : 'true'}">
                                            <button type="submit" class="btn btn-sm ${s.verified ? 'btn-outline-danger' : 'btn-success'}">
                                                ${s.verified ? 'Revoke Approval' : 'Approve Merchant'}
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 2. Users Tab -->
        <div class="tab-pane fade" id="tab-users">
            <div class="card border-0 shadow-sm p-3 bg-white rounded">
                <h5 class="fw-bold mb-3">Platform User Directory</h5>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Joined</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${usersList}">
                                <tr>
                                    <td>#<c:out value="${u.id}"/></td>
                                    <td class="fw-bold"><c:out value="${u.name}"/></td>
                                    <td><c:out value="${u.email}"/></td>
                                    <td><c:out value="${u.phone}"/></td>
                                    <td>
                                        <span class="badge ${u.role eq 'ADMIN' ? 'bg-danger' : (u.role eq 'SELLER' ? 'bg-warning text-dark' : 'bg-primary')}">
                                            <c:out value="${u.role}"/>
                                        </span>
                                    </td>
                                    <td class="small text-muted"><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 3. Moderation Tab -->
        <div class="tab-pane fade" id="tab-moderation">
            <div class="card border-0 shadow-sm p-3 bg-white rounded">
                <h5 class="fw-bold mb-3">Catalog Moderation & Content Compliance</h5>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Image</th>
                                <th>Product</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th>Store</th>
                                <th class="text-end">Moderate</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${productsList}">
                                <tr>
                                    <td>
                                        <img src="<c:out value='${p.imageUrl}'/>" alt="" style="width: 45px; height: 45px; object-fit: contain;">
                                    </td>
                                    <td>
                                        <div class="fw-bold small"><c:out value="${p.name}"/></div>
                                    </td>
                                    <td><span class="badge bg-secondary-subtle text-secondary"><c:out value="${p.category}"/></span></td>
                                    <td class="fw-bold small">₹<fmt:formatNumber value="${p.price}" pattern="#,##0"/></td>
                                    <td><c:out value="${p.stockQty}"/> units</td>
                                    <td class="small text-muted"><c:out value="${p.sellerStoreName}"/></td>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/admin/products/delete" method="POST" onsubmit="return confirm('Remove product from platform?');">
                                            <input type="hidden" name="productId" value="<c:out value='${p.id}'/>">
                                            <button type="submit" class="btn btn-outline-danger btn-sm" title="Remove Listing">
                                                <i class="bi bi-trash"></i> Remove
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 4. Categories Tab -->
        <div class="tab-pane fade" id="tab-categories">
            <div class="row g-4">
                <div class="col-md-7">
                    <div class="card border-0 shadow-sm p-3 bg-white rounded">
                        <h5 class="fw-bold mb-3">Active Platform Categories</h5>
                        <ul class="list-group">
                            <c:forEach var="cat" items="${categoriesList}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="bi <c:out value='${cat.icon}'/> fs-5 text-warning me-2"></i>
                                        <strong class="text-dark"><c:out value="${cat.name}"/></strong>
                                        <div class="small text-muted"><c:out value="${cat.description}"/></div>
                                    </div>
                                    <span class="badge bg-secondary rounded-pill"><c:out value="${cat.productCount}"/> products</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="card border-0 shadow-sm p-4 bg-white rounded">
                        <h5 class="fw-bold mb-3">Add New Category</h5>
                        <form action="${pageContext.request.contextPath}/admin/categories/save" method="POST">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Category Name</label>
                                <input type="text" name="name" class="form-control form-control-sm" placeholder="e.g. Beauty & Grooming" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Bootstrap Icon Class</label>
                                <input type="text" name="icon" class="form-control form-control-sm" placeholder="bi-stars" value="bi-tag">
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Description</label>
                                <textarea name="description" class="form-control form-control-sm" rows="3" placeholder="Category summary..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-warning fw-bold w-100" style="background-color: var(--hm-gold); border: none;">
                                Create Category
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
