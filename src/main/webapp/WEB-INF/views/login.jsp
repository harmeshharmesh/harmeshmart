<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HarmeshMart Sign-In</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        body { background-color: #FFFFFF; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
        .auth-container { max-width: 380px; margin: 30px auto; padding: 0 15px; }
        .auth-box { border: 1px solid #D5D9D9; border-radius: 8px; padding: 26px 30px; }
        .demo-card { background-color: #F8F9FA; border: 1px dashed #CDCDCD; border-radius: 6px; padding: 12px; margin-top: 20px; font-size: 12px; }
    </style>
</head>
<body>

<div class="auth-container">
    <!-- Brand Logo -->
    <div class="text-center mb-3">
        <a href="${pageContext.request.contextPath}/shop" class="d-inline-block text-decoration-none">
            <span class="fs-2 fw-bold text-dark">Harmesh<span style="color: var(--hm-gold);">Mart</span></span>
            <svg class="d-block mx-auto" width="100" height="12" viewBox="0 0 130 15" fill="none">
                <path d="M 5 3 Q 65 18 125 3" stroke="#FF9900" stroke-width="3.5" stroke-linecap="round" fill="transparent"/>
                <path d="M 120 1 L 126 4 L 122 8 Z" fill="#FF9900"/>
            </svg>
        </a>
    </div>

    <!-- Sign In Box -->
    <div class="auth-box shadow-sm">
        <h3 class="fw-bold mb-3" style="font-size: 24px;">Sign in</h3>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger py-2 small mb-3">
                <i class="bi bi-exclamation-circle-fill me-1"></i><c:out value="${errorMessage}"/>
            </div>
        </c:if>
        <c:if test="${not empty param.msg and param.msg eq 'logged_out'}">
            <div class="alert alert-info py-2 small mb-3">
                <i class="bi bi-info-circle-fill me-1"></i>You have been safely signed out.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <input type="hidden" name="redirect" value="<c:out value='${redirect}'/>">

            <div class="mb-3">
                <label class="form-label small fw-bold">Email address</label>
                <input type="email" id="loginEmail" name="email" class="form-control form-control-sm py-2" value="<c:out value='${email}'/>" required autofocus>
            </div>

            <div class="mb-3">
                <div class="d-flex justify-content-between align-items-center mb-1">
                    <label class="form-label small fw-bold mb-0">Password</label>
                </div>
                <input type="password" id="loginPassword" name="password" class="form-control form-control-sm py-2" required>
            </div>

            <button type="submit" class="hm-btn-primary py-2 fw-bold mb-3">
                Continue
            </button>

            <div class="small text-muted mb-3" style="font-size: 11px;">
                By continuing, you agree to HarmeshMart's Conditions of Use and Privacy Notice.
            </div>
        </form>

        <div class="border-top pt-3 text-center">
            <span class="small text-muted">New to HarmeshMart?</span>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-dark btn-sm w-100 mt-2 py-1">
                Create your HarmeshMart account
            </a>
        </div>
    </div>

    <!-- Quick Demo Accounts Box for Examiner / Reviewer -->
    <div class="demo-card">
        <div class="fw-bold text-dark mb-1"><i class="bi bi-key-fill text-warning me-1"></i> Pre-seeded Roles & Users:</div>
        <div class="mb-1">
            <strong>Admin:</strong> <code>admin@harmeshmart.com</code> / <code>admin123</code>
            <button class="btn btn-xs btn-outline-secondary py-0 px-1 float-end" onclick="fillCreds('admin@harmeshmart.com', 'admin123')">Fill</button>
        </div>
        <div class="mb-1">
            <strong>Seller:</strong> <code>seller@harmeshmart.com</code> / <code>seller123</code>
            <button class="btn btn-xs btn-outline-secondary py-0 px-1 float-end" onclick="fillCreds('seller@harmeshmart.com', 'seller123')">Fill</button>
        </div>
        <div>
            <strong>Buyer:</strong> <code>buyer@harmeshmart.com</code> / <code>buyer123</code>
            <button class="btn btn-xs btn-outline-secondary py-0 px-1 float-end" onclick="fillCreds('buyer@harmeshmart.com', 'buyer123')">Fill</button>
        </div>
    </div>

    <!-- Footer links -->
    <div class="text-center text-muted small mt-4 pt-3 border-top" style="font-size: 11px;">
        <span class="me-3"><a href="#" class="text-secondary text-decoration-none">Conditions of Use</a></span>
        <span class="me-3"><a href="#" class="text-secondary text-decoration-none">Privacy Notice</a></span>
        <span><a href="#" class="text-secondary text-decoration-none">Help</a></span>
        <div class="mt-2 text-muted">© 2026 HarmeshMart Inc.</div>
    </div>
</div>

<script>
function fillCreds(e, p) {
    document.getElementById('loginEmail').value = e;
    document.getElementById('loginPassword').value = p;
}
</script>

</body>
</html>
