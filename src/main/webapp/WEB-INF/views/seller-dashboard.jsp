<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="pageTitle" value="Seller Central Dashboard - HarmeshMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <!-- Merchant Dashboard Header -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
        <div>
            <div class="badge bg-warning text-dark mb-1"><i class="bi bi-shop me-1"></i> Seller Central</div>
            <h2 class="fw-bold mb-0" style="font-size: 26px;">Merchant Hub</h2>
            <div class="text-muted small">Manage catalog listings, real-time inventory, and customer fulfillment.</div>
        </div>
        <button type="button" class="btn btn-warning fw-bold px-4 py-2" data-bs-toggle="modal" data-bs-target="#addProductModal" style="background-color: var(--hm-gold); border: none;">
            <i class="bi bi-plus-circle me-1"></i> Add New Product Listing
        </button>
    </div>

    <!-- Feedback messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>Action completed successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Metrics Cards (Revenue, Listings, Orders) -->
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="card border-0 shadow-sm p-4 bg-white" style="border-radius: 8px; border-left: 4px solid #007600 !important;">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <div class="text-muted small text-uppercase fw-bold">Gross Merchant Revenue</div>
                        <h3 class="fw-bold text-dark mt-1 mb-0">₹<fmt:formatNumber value="${sellerRevenue != null ? sellerRevenue : 0}" pattern="#,##0"/></h3>
                    </div>
                    <i class="bi bi-currency-rupee fs-1 text-success opacity-75"></i>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card border-0 shadow-sm p-4 bg-white" style="border-radius: 8px; border-left: 4px solid var(--hm-gold) !important;">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <div class="text-muted small text-uppercase fw-bold">Active Listings</div>
                        <h3 class="fw-bold text-dark mt-1 mb-0"><c:out value="${totalListings}"/></h3>
                    </div>
                    <i class="bi bi-box-seam fs-1 text-warning opacity-75"></i>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card border-0 shadow-sm p-4 bg-white" style="border-radius: 8px; border-left: 4px solid #007185 !important;">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <div class="text-muted small text-uppercase fw-bold">Incoming Orders</div>
                        <h3 class="fw-bold text-dark mt-1 mb-0"><c:out value="${totalIncomingOrders}"/></h3>
                    </div>
                    <i class="bi bi-truck fs-1 text-primary opacity-75"></i>
                </div>
            </div>
        </div>
    </div>

    <!-- Tabs Navigation -->
    <ul class="nav nav-tabs mb-4" id="sellerTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active fw-bold text-dark" id="products-tab" data-bs-toggle="tab" data-bs-target="#products-panel" type="button" role="tab">
                <i class="bi bi-grid me-2"></i>My Product Catalog (<c:out value="${totalListings}"/>)
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link fw-bold text-dark" id="orders-tab" data-bs-toggle="tab" data-bs-target="#orders-panel" type="button" role="tab">
                <i class="bi bi-receipt me-2"></i>Customer Orders Fulfillment (<c:out value="${totalIncomingOrders}"/>)
            </button>
        </li>
    </ul>

    <!-- Tabs Content -->
    <div class="tab-content" id="sellerTabsContent">
        <!-- Products Tab -->
        <div class="tab-pane fade show active" id="products-panel" role="tabpanel">
            <div class="card border-0 shadow-sm bg-white p-3" style="border-radius: 8px;">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 80px;">Image</th>
                                <th>Product Details</th>
                                <th>Category</th>
                                <th>Price (₹)</th>
                                <th>Stock Qty</th>
                                <th>Rating</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${sellerProducts}">
                                <tr>
                                    <td>
                                        <img src="<c:out value='${p.imageUrl}'/>" alt="" style="width: 60px; height: 60px; object-fit: contain;" class="border rounded p-1 bg-light">
                                    </td>
                                    <td>
                                        <h6 class="mb-1 fw-bold">
                                            <a href="${pageContext.request.contextPath}/product?id=<c:out value='${p.id}'/>" class="text-dark" target="_blank">
                                                <c:out value="${p.name}"/>
                                            </a>
                                        </h6>
                                        <small class="text-muted text-truncate d-block" style="max-width: 320px;"><c:out value="${p.description}"/></small>
                                    </td>
                                    <td><span class="badge bg-secondary-subtle text-secondary"><c:out value="${p.category}"/></span></td>
                                    <td class="fw-bold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0"/></td>
                                    <td>
                                        <!-- Inline Stock Update Form -->
                                        <form action="${pageContext.request.contextPath}/seller/products/stock" method="POST" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="productId" value="<c:out value='${p.id}'/>">
                                            <input type="number" name="stockQty" value="<c:out value='${p.stockQty}'/>" min="0" max="9999" class="form-control form-control-sm" style="width: 70px;">
                                            <button type="submit" class="btn btn-outline-secondary btn-sm" title="Save Stock"><i class="bi bi-check"></i></button>
                                        </form>
                                    </td>
                                    <td>
                                        <span class="hm-stars small">★</span> <fmt:formatNumber value="${p.rating}" pattern="0.0"/>
                                        <small class="text-muted">(<c:out value="${p.reviewCount}"/>)</small>
                                    </td>
                                    <td class="text-end">
                                        <div class="btn-group btn-group-sm">
                                            <!-- Delete -->
                                            <form action="${pageContext.request.contextPath}/seller/products/delete" method="POST" onsubmit="return confirm('Delete this listing?');">
                                                <input type="hidden" name="productId" value="<c:out value='${p.id}'/>">
                                                <button type="submit" class="btn btn-outline-danger"><i class="bi bi-trash"></i></button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Orders Tab -->
        <div class="tab-pane fade" id="orders-panel" role="tabpanel">
            <div class="card border-0 shadow-sm bg-white p-3" style="border-radius: 8px;">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer & Shipping Address</th>
                                <th>Date</th>
                                <th>Payment</th>
                                <th>Amount</th>
                                <th>Fulfillment Workflow</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ord" items="${incomingOrders}">
                                <tr>
                                    <td><strong class="text-dark">#<c:out value="${ord.id}"/></strong></td>
                                    <td>
                                        <div class="fw-bold"><c:out value="${ord.buyerName}"/></div>
                                        <div class="small text-muted text-truncate" style="max-width: 250px;"><c:out value="${ord.shippingAddress}"/></div>
                                    </td>
                                    <td class="small text-muted"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                                    <td>
                                        <span class="badge bg-secondary"><c:out value="${ord.paymentMethod}"/></span>
                                    </td>
                                    <td class="fw-bold text-dark">₹<fmt:formatNumber value="${ord.totalAmount}" pattern="#,##0"/></td>
                                    <td>
                                        <!-- Update Order Status Workflow Form -->
                                        <form action="${pageContext.request.contextPath}/seller/orders/status" method="POST" class="d-flex align-items-center gap-2">
                                            <input type="hidden" name="orderId" value="<c:out value='${ord.id}'/>">
                                            <select name="status" class="form-select form-select-sm" style="width: 140px;">
                                                <option value="PENDING" ${ord.status eq 'PENDING' ? 'selected' : ''}>Pending</option>
                                                <option value="CONFIRMED" ${ord.status eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                                <option value="SHIPPED" ${ord.status eq 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                                <option value="DELIVERED" ${ord.status eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                                <option value="CANCELLED" ${ord.status eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-dark">Update</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/seller/products/save" method="POST">
                <div class="modal-header bg-dark text-white">
                    <h5 class="modal-title fw-bold"><i class="bi bi-box-seam me-2"></i>Create New Product Listing</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-bold">Product Title</label>
                            <input type="text" name="name" class="form-control" placeholder="e.g. Flagship 5G Wireless Smartphone" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Department / Category</label>
                            <select name="category" class="form-select" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="<c:out value='${c.name}'/>"><c:out value="${c.name}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Selling Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control" placeholder="4999.00" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">M.R.P. / Original Price (₹)</label>
                            <input type="number" step="0.01" name="originalPrice" class="form-control" placeholder="6999.00">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-bold">Initial Stock Units</label>
                            <input type="number" name="stockQty" class="form-control" placeholder="25" min="1" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">High-Resolution Image URL (Unsplash or CDN)</label>
                            <input type="url" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/photo-..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-bold">Product Description & Bullet Points</label>
                            <textarea name="description" class="form-control" rows="4" placeholder="Detailed product specifications, features, warranty, and package contents..." required></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-warning fw-bold px-4" style="background-color: var(--hm-gold); border: none;">Publish Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
