<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>404 - Page Not Found | HarmeshMart</title>
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
        <i class="bi bi-question-circle text-warning" style="font-size: 80px;"></i>
        <h2 class="fw-bold mt-3">Looking for something?</h2>
        <p class="text-muted">We're sorry. The Web address you entered is not a functioning page on our site.</p>
    </div>

    <form action="${pageContext.request.contextPath}/shop" method="GET" class="mb-4">
        <div class="input-group">
            <input type="text" name="keyword" class="form-control" placeholder="Search HarmeshMart catalog...">
            <button type="submit" class="btn btn-warning fw-bold px-4" style="background-color: var(--hm-gold); border: none;">Search</button>
        </div>
    </form>

    <div class="d-flex justify-content-center gap-3">
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-outline-dark">
            <i class="bi bi-house-door me-1"></i> Go to HarmeshMart's Home Page
        </a>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary">
            Your Orders
        </a>
    </div>
</div>

</body>
</html>
