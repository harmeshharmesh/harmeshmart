<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Floating AI Chatbot Widget (Section 11 & Section 17) -->
<div id="hmChatbotFab" class="hm-chatbot-fab" title="Chat with HarmeshMart AI Assistant">
    <i class="bi bi-chat-dots-fill fs-3"></i>
</div>

<div id="hmChatbotPanel" class="hm-chatbot-panel" style="display: none;">
    <div class="hm-chat-header">
        <div class="d-flex align-items-center gap-2">
            <i class="bi bi-robot fs-4 text-warning"></i>
            <div>
                <div class="fw-bold" style="font-size: 14px;">HarmeshMart AI Assistant</div>
                <div class="text-white-50" style="font-size: 11px;">Instant Product & Order Help</div>
            </div>
        </div>
        <button type="button" id="hmChatCloseBtn" class="btn-close btn-close-white" aria-label="Close"></button>
    </div>

    <div id="hmChatBody" class="hm-chat-body">
        <div class="hm-chat-msg bot">
            Hello! I am your <strong>HarmeshMart AI Assistant</strong>. Ask me anything about tracking your order, returns, payment options, or product recommendations!
        </div>
        <div class="d-flex flex-wrap gap-1 mt-1">
            <button class="btn btn-outline-secondary btn-sm hm-chat-faq-pill" style="font-size: 11px;" data-query="How does return and refund work?">7-Day Returns</button>
            <button class="btn btn-outline-secondary btn-sm hm-chat-faq-pill" style="font-size: 11px;" data-query="What payment methods are supported?">UPI & Card</button>
            <button class="btn btn-outline-secondary btn-sm hm-chat-faq-pill" style="font-size: 11px;" data-query="How fast is the delivery?">Delivery Time</button>
            <button class="btn btn-outline-secondary btn-sm hm-chat-faq-pill" style="font-size: 11px;" data-query="How can I become a seller?">Become a Seller</button>
            <button class="btn btn-outline-secondary btn-sm hm-chat-faq-pill" style="font-size: 11px;" data-query="What promo codes are available?">Coupon Code</button>
        </div>
    </div>

    <div class="hm-chat-footer">
        <input type="text" id="hmChatInput" class="hm-chat-input" placeholder="Type your question..." maxlength="500">
        <button type="button" id="hmChatSendBtn" class="hm-chat-send-btn" title="Send">
            <i class="bi bi-arrow-up text-dark fw-bold"></i>
        </button>
    </div>
</div>
