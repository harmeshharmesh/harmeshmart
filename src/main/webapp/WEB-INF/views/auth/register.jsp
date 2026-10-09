<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Account - HarmeshMart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        body { background-color: #FFFFFF; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
        .auth-container { max-width: 420px; margin: 30px auto; padding: 0 15px; }
        .auth-box { border: 1px solid #D5D9D9; border-radius: 8px; padding: 26px 30px; }
    </style>
</head>
<body>

<div class="auth-container">
    <div class="text-center mb-3">
        <a href="${pageContext.request.contextPath}/shop" class="d-inline-block text-decoration-none">
            <span class="fs-2 fw-bold text-dark">Harmesh<span style="color: var(--hm-gold);">Mart</span></span>
            <svg class="d-block mx-auto" width="100" height="12" viewBox="0 0 130 15" fill="none">
                <path d="M 5 3 Q 65 18 125 3" stroke="#FF9900" stroke-width="3.5" stroke-linecap="round" fill="transparent"/>
                <path d="M 120 1 L 126 4 L 122 8 Z" fill="#FF9900"/>
            </svg>
        </a>
    </div>

    <div class="auth-box shadow-sm">
        <h3 class="fw-bold mb-3" style="font-size: 24px;">Create Account</h3>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger py-2 small mb-3">
                <i class="bi bi-exclamation-circle-fill me-1"></i><c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <div class="mb-3">
                <label class="form-label small fw-bold">Your name</label>
                <input type="text" name="name" class="form-control form-control-sm py-2" placeholder="First and last name" value="<c:out value='${name}'/>" required autofocus>
            </div>

            <div class="mb-3">
                <label class="form-label small fw-bold">Mobile number</label>
                <input type="tel" name="phone" class="form-control form-control-sm py-2" placeholder="e.g. 9876543210" value="<c:out value='${phone}'/>">
            </div>

            <div class="mb-3">
                <label class="form-label small fw-bold">Email address</label>
                <input type="email" name="email" class="form-control form-control-sm py-2" placeholder="name@example.com" value="<c:out value='${email}'/>" required>
            </div>

            <div class="mb-3">
                <label class="form-label small fw-bold">Password</label>
                <input type="password" name="password" class="form-control form-control-sm py-2" placeholder="At least 6 characters" minlength="6" required>
                <div class="text-muted small" style="font-size: 11px;">Passwords must be at least 6 characters.</div>
            </div>

            <div class="mb-3">
                <label class="form-label small fw-bold">I want to register as a:</label>
                <div class="d-flex gap-3">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="role" id="roleBuyer" value="BUYER" checked onclick="document.getElementById('sellerFields').style.display='none';">
                        <label class="form-check-label small" for="roleBuyer">Customer / Buyer</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="role" id="roleSeller" value="SELLER" onclick="document.getElementById('sellerFields').style.display='block';">
                        <label class="form-check-label small fw-bold text-primary" for="roleSeller">Merchant / Seller</label>
                    </div>
                </div>
            </div>

            <div id="sellerFields" class="p-3 bg-light rounded border mb-3" style="display: none;">
                <h6 class="fw-bold small mb-2 text-dark">Merchant Store Details</h6>
                <div class="mb-2">
                    <label class="form-label small">Store Business Name</label>
                    <input type="text" name="storeName" class="form-control form-control-sm" placeholder="e.g. Apex Electronics Hub">
                </div>
                <div class="mb-2">
                    <label class="form-label small">Store Description</label>
                    <textarea name="storeDescription" class="form-control form-control-sm" rows="2" placeholder="What products will you sell?"></textarea>
                </div>
            </div>

            <button type="submit" class="hm-btn-primary py-2 fw-bold mb-3">
                Verify mobile & create account
            </button>

            <div class="small text-muted mb-3" style="font-size: 11px;">
                By creating an account, you agree to HarmeshMart's Conditions of Use and Privacy Notice.
            </div>
        </form>

        <div class="border-top pt-3 text-center">
            <span class="small text-muted">Already have an account? </span>
            <a href="${pageContext.request.contextPath}/login" class="small fw-bold">Sign in <i class="bi bi-caret-right-fill"></i></a>
        </div>
    </div>
</div>

</body>
</html>
