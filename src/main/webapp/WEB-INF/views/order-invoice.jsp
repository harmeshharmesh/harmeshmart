<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tax Invoice - Order #${order.id} | HarmeshMart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #F8F9FA; font-family: Arial, sans-serif; font-size: 13px; color: #333; }
        .invoice-card { max-width: 850px; margin: 30px auto; background: #FFFFFF; border: 1px solid #DDD; padding: 40px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
        .invoice-header-title { font-size: 26px; font-weight: 800; color: #131921; }
        .invoice-header-title span { color: #FF9900; }
        @media print {
            .no-print { display: none !important; }
            .invoice-card { box-shadow: none; border: none; margin: 0; padding: 0; max-width: 100%; }
        }
    </style>
</head>
<body>

<div class="container my-3 no-print text-center">
    <button onclick="window.print();" class="btn btn-warning fw-bold px-4 me-2">
        <i class="bi bi-printer me-1"></i> Print Invoice
    </button>
    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary">
        Back to Orders
    </a>
</div>

<div class="invoice-card">
    <!-- Top Header -->
    <div class="d-flex justify-content-between align-items-start border-bottom pb-4 mb-4">
        <div>
            <div class="invoice-header-title">Harmesh<span>Mart</span></div>
            <div class="text-muted small">HarmeshMart E-Commerce Marketplace Private Limited</div>
            <div class="text-muted small">GSTIN: 29AABCH1234F1Z8 | CIN: U52100KA2026PTC123456</div>
            <div class="text-muted small">Karnataka, India - 560001</div>
        </div>
        <div class="text-end">
            <h4 class="fw-bold text-uppercase text-secondary mb-1">Tax Invoice</h4>
            <div class="small"><strong>Invoice No:</strong> HMAR-2026-<c:out value="${order.id}"/></div>
            <div class="small"><strong>Order ID:</strong> #<c:out value="${order.id}"/></div>
            <div class="small"><strong>Invoice Date:</strong> <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy"/></div>
        </div>
    </div>

    <!-- Billing & Shipping Details -->
    <div class="row mb-4">
        <div class="col-6">
            <h6 class="fw-bold text-uppercase small text-muted">Shipping & Delivery Address:</h6>
            <div class="fw-bold text-dark"><c:out value="${order.buyerName != null ? order.buyerName : currentUser.name}"/></div>
            <div class="text-secondary small"><c:out value="${order.shippingAddress}"/></div>
            <div class="text-secondary small">Email: <c:out value="${order.buyerEmail != null ? order.buyerEmail : currentUser.email}"/></div>
        </div>
        <div class="col-6 text-end">
            <h6 class="fw-bold text-uppercase small text-muted">Payment Details:</h6>
            <div class="small"><strong>Payment Method:</strong> <c:out value="${order.paymentMethod}"/></div>
            <div class="small"><strong>Payment Status:</strong> <span class="badge bg-success"><c:out value="${order.paymentStatus}"/></span></div>
            <div class="small"><strong>Fulfillment Mode:</strong> HarmeshMart Express Prime</div>
        </div>
    </div>

    <!-- Items Table -->
    <div class="table-responsive mb-4">
        <table class="table table-bordered">
            <thead class="table-light">
                <tr>
                    <th style="width: 5%;">#</th>
                    <th style="width: 50%;">Description</th>
                    <th class="text-center" style="width: 15%;">Unit Price</th>
                    <th class="text-center" style="width: 10%;">Qty</th>
                    <th class="text-end" style="width: 20%;">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="it" items="${order.items}" varStatus="status">
                    <tr>
                        <td><c:out value="${status.index + 1}"/></td>
                        <td>
                            <div class="fw-bold"><c:out value="${it.productName}"/></div>
                            <div class="small text-muted">Sold by: <c:out value="${it.sellerStoreName != null ? it.sellerStoreName : 'Authorized Merchant'}"/></div>
                        </td>
                        <td class="text-center">₹<fmt:formatNumber value="${it.unitPrice}" pattern="#,##0"/></td>
                        <td class="text-center"><c:out value="${it.quantity}"/></td>
                        <td class="text-end fw-bold">₹<fmt:formatNumber value="${it.unitPrice * it.quantity}" pattern="#,##0"/></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="4" class="text-end fw-bold">Subtotal:</td>
                    <td class="text-end fw-bold">₹<fmt:formatNumber value="${order.totalAmount + order.discountAmount}" pattern="#,##0"/></td>
                </tr>
                <c:if test="${order.discountAmount > 0}">
                    <tr>
                        <td colspan="4" class="text-end text-success fw-bold">Promotional Discount (<c:out value="${order.promoCode}"/>):</td>
                        <td class="text-end text-success fw-bold">-₹<fmt:formatNumber value="${order.discountAmount}" pattern="#,##0"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="4" class="text-end fw-bold">Shipping & Handling:</td>
                    <td class="text-end text-success fw-bold">FREE</td>
                </tr>
                <tr class="table-active fs-6">
                    <td colspan="4" class="text-end fw-bold text-dark">Grand Total (INR):</td>
                    <td class="text-end fw-bold text-danger">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/></td>
                </tr>
            </tfoot>
        </table>
    </div>

    <!-- Footer Terms & Stamp -->
    <div class="border-top pt-4 text-muted small">
        <p class="mb-1"><strong>Terms & Conditions:</strong></p>
        <p class="mb-1">1. Goods sold are subject to HarmeshMart's standard 7-day replacement and warranty policy.</p>
        <p class="mb-3">2. This is a computer-generated tax invoice and does not require a physical signature.</p>
        <div class="text-end fw-bold text-dark">Authorized Signatory for HarmeshMart Inc.</div>
    </div>
</div>

</body>
</html>
