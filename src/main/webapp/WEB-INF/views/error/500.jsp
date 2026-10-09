<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Internal Server Error | HarmeshMart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body class="bg-white">

<div class="container py-5 text-center" style="max-width: 600px;">
    <a href="${pageContext.request.contextPath}/shop" class="d-inline-block text-decoration-none mb-4">
        <span class="fs-1 fw-bold text-dark">Harmesh<span style="color: var(--hm-gold);">Mart</span></span>
    </a>

    <div class="my-4">
        <i class="bi bi-exclamation-triangle text-danger" style="font-size: 80px;"></i>
        <h2 class="fw-bold mt-3">An unexpected error occurred</h2>
        <p class="text-muted">We apologize for the inconvenience. Our engineering team has been automatically alerted and is resolving the issue.</p>
    </div>

    <div class="d-flex justify-content-center gap-3 mt-4">
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-warning fw-bold px-4">
            <i class="bi bi-house-door me-1"></i> Return to Homepage
        </a>
        <a href="javascript:history.back();" class="btn btn-outline-secondary">
            Go Back
        </a>
    </div>
</div>

</body>
</html>
