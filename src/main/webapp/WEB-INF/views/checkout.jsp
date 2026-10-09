<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="pageTitle" value="HarmeshMart Checkout" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<main class="hm-container">
    <div class="row g-4">
        <!-- Left: Checkout Steps -->
        <div class="col-lg-8">
            <h2 class="fw-bold mb-4" style="font-size: 26px;">Secure Checkout</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${errorMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/checkout" method="POST" id="checkoutForm">
                <input type="hidden" name="promoCode" value="<c:out value='${cartSummary.promoCode}'/>">

                <!-- Step 1: Shipping Address Selection -->
                <div class="card mb-4 border shadow-sm">
                    <div class="card-header bg-white py-3">
                        <h5 class="fw-bold mb-0 text-dark">
                            <span class="badge bg-warning text-dark me-2">1</span> Select a Delivery Address
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <div class="mb-3">
                                    <label class="form-label fw-semibold small">Choose from your saved addresses:</label>
                                    <c:forEach var="addr" items="${addresses}" varStatus="status">
                                        <div class="form-check p-3 border rounded mb-2 ${addr.isDefault() ? 'bg-light border-warning' : ''}">
                                            <input class="form-check-input" type="radio" name="addressId" id="addr_${addr.id}" value="${addr.id}" ${addr.isDefault() or status.first ? 'checked' : ''}>
                                            <label class="form-check-label ms-2" for="addr_${addr.id}">
                                                <strong class="text-dark"><c:out value="${addr.fullName}"/></strong>
                                                <c:if test="${addr.isDefault()}">
                                                    <span class="badge bg-secondary ms-1">Default</span>
                                                </c:if>
                                                <div class="small text-secondary mt-1">
                                                    <c:out value="${addr.street}"/>, <c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.postalCode}"/>
                                                </div>
                                                <div class="small text-muted">Phone: <c:out value="${addr.phone}"/></div>
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info py-2 small mb-3">No saved addresses found. Please enter your delivery address below:</div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Add or Use New Address Collapse -->
                        <div class="mt-3">
                            <a class="btn btn-outline-dark btn-sm" data-bs-toggle="collapse" href="#newAddressBox">
                                <i class="bi bi-plus-lg me-1"></i> Deliver to a new address
                            </a>
                            <div class="collapse mt-3" id="newAddressBox">
                                <div class="p-3 border rounded bg-light">
                                    <h6 class="fw-bold mb-3">New Delivery Address</h6>
                                    <div class="row g-2">
                                        <div class="col-md-6 mb-2">
                                            <label class="form-label small">Full Name</label>
                                            <input type="text" name="fullName" class="form-control form-control-sm" placeholder="e.g. Harmesh Kumar" value="<c:out value='${currentUser.name}'/>">
                                        </div>
                                        <div class="col-md-6 mb-2">
                                            <label class="form-label small">10-Digit Mobile Number</label>
                                            <input type="tel" name="phone" class="form-control form-control-sm" placeholder="e.g. 9876543210" value="<c:out value='${currentUser.phone}'/>">
                                        </div>
                                        <div class="col-12 mb-2">
                                            <label class="form-label small">Flat, House no., Building, Company, Apartment</label>
                                            <input type="text" name="street" class="form-control form-control-sm" placeholder="Street Address">
                                        </div>
                                        <div class="col-md-4 mb-2">
                                            <label class="form-label small">City</label>
                                            <input type="text" name="city" class="form-control form-control-sm" placeholder="City">
                                        </div>
                                        <div class="col-md-4 mb-2">
                                            <label class="form-label small">State</label>
                                            <input type="text" name="state" class="form-control form-control-sm" placeholder="State">
                                        </div>
                                        <div class="col-md-4 mb-2">
                                            <label class="form-label small">PIN Code</label>
                                            <input type="text" name="postalCode" class="form-control form-control-sm" placeholder="6 digits PIN">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Step 2: Payment Method Simulation -->
                <div class="card mb-4 border shadow-sm">
                    <div class="card-header bg-white py-3">
                        <h5 class="fw-bold mb-0 text-dark">
                            <span class="badge bg-warning text-dark me-2">2</span> Payment Method Simulation
                        </h5>
                    </div>
                    <div class="card-body">
                        <!-- Option A: UPI -->
                        <div class="form-check p-3 border rounded mb-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="pay_upi" value="UPI" checked onclick="togglePaymentFields('upi');">
                            <label class="form-check-label ms-2 d-flex justify-content-between align-items-center w-100" for="pay_upi">
                                <div>
                                    <strong class="text-dark"><i class="bi bi-qr-code-scan text-primary me-2"></i>UPI (Google Pay, PhonePe, Paytm, BHIM)</strong>
                                    <div class="small text-muted">Scan QR or enter Virtual Payment Address (VPA)</div>
                                </div>
                                <span class="badge bg-success-subtle text-success">Instant Instant Verification</span>
                            </label>
                            <div id="upiDetailsBox" class="mt-3 pt-2 border-top">
                                <label class="form-label small">UPI ID / Mobile Number</label>
                                <input type="text" name="paymentDetails" class="form-control form-control-sm" style="max-width: 300px;" placeholder="e.g. harmesh@okhdfcbank" value="harmesh@upi">
                            </div>
                        </div>

                        <!-- Option B: Credit/Debit Card -->
                        <div class="form-check p-3 border rounded mb-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="pay_card" value="CREDIT_CARD" onclick="togglePaymentFields('card');">
                            <label class="form-check-label ms-2" for="pay_card">
                                <strong class="text-dark"><i class="bi bi-credit-card-2-front text-danger me-2"></i>Credit / Debit Card</strong>
                                <div class="small text-muted">Visa, MasterCard, RuPay, American Express</div>
                            </label>
                            <div id="cardDetailsBox" class="mt-3 pt-2 border-top" style="display: none;">
                                <div class="row g-2">
                                    <div class="col-12 col-md-6">
                                        <input type="text" class="form-control form-control-sm mb-2" placeholder="Card Number (e.g. 4242 4242 4242 4242)">
                                    </div>
                                    <div class="col-6 col-md-3">
                                        <input type="text" class="form-control form-control-sm mb-2" placeholder="MM/YY">
                                    </div>
                                    <div class="col-6 col-md-3">
                                        <input type="password" class="form-control form-control-sm mb-2" placeholder="CVV" maxlength="4">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Option C: Cash on Delivery -->
                        <div class="form-check p-3 border rounded">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="pay_cod" value="COD" onclick="togglePaymentFields('cod');">
                            <label class="form-check-label ms-2" for="pay_cod">
                                <strong class="text-dark"><i class="bi bi-cash-stack text-success me-2"></i>Cash on Delivery (COD)</strong>
                                <div class="small text-muted">Pay in cash or scan QR upon delivery at your doorstep</div>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Step 3: Review Items -->
                <div class="card mb-4 border shadow-sm">
                    <div class="card-header bg-white py-3">
                        <h5 class="fw-bold mb-0 text-dark">
                            <span class="badge bg-warning text-dark me-2">3</span> Review Items and Delivery
                        </h5>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Product</th>
                                        <th class="text-center">Quantity</th>
                                        <th class="text-end">Price</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartSummary.items}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="<c:out value='${item.imageUrl}'/>" alt="" style="width: 50px; height: 50px; object-fit: contain;">
                                                    <div>
                                                        <h6 class="mb-0 text-dark small fw-bold"><c:out value="${item.productName}"/></h6>
                                                        <small class="text-success"><i class="bi bi-truck"></i> FREE Delivery</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center fw-bold"><c:out value="${item.quantity}"/></td>
                                            <td class="text-end fw-bold">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Action Button in main form -->
                <button type="submit" class="hm-btn-orange py-3 fs-5 fw-bold shadow">
                    <i class="bi bi-shield-lock-fill me-2"></i> Place Your Order and Pay
                </button>
            </form>
        </div>

        <!-- Right: Order Summary Sticky Card -->
        <div class="col-lg-4">
            <div class="bg-white p-4 rounded border shadow-sm sticky-top" style="top: 80px;">
                <button type="button" class="hm-btn-orange py-2 fs-6 fw-bold mb-3" onclick="document.getElementById('checkoutForm').submit();">
                    Place Your Order
                </button>
                <p class="text-muted small text-center mb-3">By placing your order, you agree to HarmeshMart's privacy notice and conditions of use.</p>

                <h5 class="fw-bold border-bottom pb-2 mb-3">Order Summary</h5>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Items (<c:out value="${cartSummary.totalItems}"/>):</span>
                    <span>₹<fmt:formatNumber value="${cartSummary.subtotal}" pattern="#,##0"/></span>
                </div>
                <div class="d-flex justify-content-between mb-2 small">
                    <span class="text-muted">Delivery & Handling:</span>
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
                    <div class="d-flex justify-content-between mb-2 small text-success fw-bold">
                        <span>Promo Code (<c:out value="${cartSummary.promoCode}"/>):</span>
                        <span>-₹<fmt:formatNumber value="${cartSummary.discount}" pattern="#,##0"/></span>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between border-top pt-3 mt-3 fs-5 fw-bold">
                    <span class="text-dark">Order Total:</span>
                    <span class="text-danger">₹<fmt:formatNumber value="${cartSummary.grandTotal}" pattern="#,##0"/></span>
                </div>

                <div class="bg-light p-3 rounded mt-4 border small text-muted">
                    <div class="d-flex align-items-center gap-2 mb-1">
                        <i class="bi bi-shield-fill-check text-success fs-5"></i>
                        <strong class="text-dark">HarmeshMart A-to-z Guarantee</strong>
                    </div>
                    Safe delivery, guaranteed authentic products, and 100% purchase protection.
                </div>
            </div>
        </div>
    </div>
</main>

<script>
function togglePaymentFields(type) {
    const upiBox = document.getElementById('upiDetailsBox');
    const cardBox = document.getElementById('cardDetailsBox');
    if (upiBox) upiBox.style.display = (type === 'upi') ? 'block' : 'none';
    if (cardBox) cardBox.style.display = (type === 'card') ? 'block' : 'none';
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
