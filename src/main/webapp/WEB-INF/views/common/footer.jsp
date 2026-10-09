<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer -->
<footer class="hm-footer">
    <div class="hm-footer-back-to-top" onclick="window.scrollTo({top: 0, behavior: 'smooth'});">
        Back to top
    </div>

    <div class="hm-footer-content">
        <div class="hm-footer-col">
            <h4>Get to Know Us</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/shop">About HarmeshMart</a></li>
                <li><a href="${pageContext.request.contextPath}/shop">Careers</a></li>
                <li><a href="${pageContext.request.contextPath}/shop">Press Releases</a></li>
                <li><a href="${pageContext.request.contextPath}/shop">Harmesh Science</a></li>
            </ul>
        </div>
        <div class="hm-footer-col">
            <h4>Connect with Us</h4>
            <ul>
                <li><a href="#"><i class="bi bi-facebook me-2"></i>Facebook</a></li>
                <li><a href="#"><i class="bi bi-twitter-x me-2"></i>Twitter / X</a></li>
                <li><a href="#"><i class="bi bi-instagram me-2"></i>Instagram</a></li>
                <li><a href="#"><i class="bi bi-linkedin me-2"></i>LinkedIn</a></li>
            </ul>
        </div>
        <div class="hm-footer-col">
            <h4>Make Money with Us</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/become-seller" class="text-warning fw-bold">Sell on HarmeshMart</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/dashboard">Seller Central Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/shop">Become an Affiliate</a></li>
                <li><a href="${pageContext.request.contextPath}/shop">Advertise Your Products</a></li>
            </ul>
        </div>
        <div class="hm-footer-col">
            <h4>Let Us Help You</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/orders">Your Account & Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/orders">Returns & Replacements</a></li>
                <li><a href="${pageContext.request.contextPath}/cart">Shopping Cart & Saved Items</a></li>
                <li><a href="javascript:void(0);" onclick="document.getElementById('hmChatbotFab').click();">AI Help Center</a></li>
            </ul>
        </div>
    </div>

    <div class="hm-footer-bottom">
        <div class="d-flex justify-content-center align-items-center gap-3 mb-2">
            <span class="fw-bold text-white fs-6">Harmesh<span style="color: var(--hm-gold);">Mart</span></span>
            <span class="border border-secondary px-2 py-1 rounded text-white-50"><i class="bi bi-globe me-1"></i> English</span>
            <span class="border border-secondary px-2 py-1 rounded text-white-50"><i class="bi bi-currency-rupee me-1"></i> INR - Indian Rupee</span>
        </div>
        <p class="mb-0 text-white-50">© 2026 HarmeshMart Inc. All rights reserved. Built with Java Servlets, JSP, H2, and HikariCP for Anna University R2025.</p>
    </div>
</footer>

<!-- Include Chatbot Widget -->
<jsp:include page="/WEB-INF/views/common/chat-widget.jsp"/>

<!-- Bootstrap 5 Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- HarmeshMart Main JS -->
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
</body>
</html>
