/**
 * HarmeshMart - Client-Side Interactive Engine
 * Handles AI Chatbot, AJAX Cart updates, Wishlist badges, and checkout flows.
 */

document.addEventListener('DOMContentLoaded', function () {
    initChatbot();
    initCartQuantityHandlers();
});

// ================= AI CHATBOT CONTROLLER =================
function initChatbot() {
    const fab = document.getElementById('hmChatbotFab');
    const panel = document.getElementById('hmChatbotPanel');
    const closeBtn = document.getElementById('hmChatCloseBtn');
    const sendBtn = document.getElementById('hmChatSendBtn');
    const input = document.getElementById('hmChatInput');
    const body = document.getElementById('hmChatBody');

    if (!fab || !panel) return;

    fab.addEventListener('click', function () {
        panel.style.display = (panel.style.display === 'none' || panel.style.display === '') ? 'flex' : 'none';
        if (panel.style.display === 'flex' && input) {
            input.focus();
        }
    });

    if (closeBtn) {
        closeBtn.addEventListener('click', function () {
            panel.style.display = 'none';
        });
    }

    function appendMessage(text, sender) {
        const msgDiv = document.createElement('div');
        msgDiv.className = 'hm-chat-msg ' + sender;
        msgDiv.textContent = text;
        body.appendChild(msgDiv);
        body.scrollTop = body.scrollHeight;
    }

    function sendQuery(message) {
        if (!message || message.trim() === '') return;
        appendMessage(message, 'user');
        if (input) input.value = '';

        // Show typing indicator
        const typingDiv = document.createElement('div');
        typingDiv.className = 'hm-chat-msg bot hm-typing';
        typingDiv.textContent = 'Thinking...';
        body.appendChild(typingDiv);
        body.scrollTop = body.scrollHeight;

        const ctx = window.location.pathname;
        const base = (typeof window.contextPath !== 'undefined') ? window.contextPath : '/HarmeshMart';
        const apiPath = base + '/api/v1/chat';

        fetch(apiPath, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({ message: message, context: ctx })
        })
        .then(res => res.json())
        .then(data => {
            if (typingDiv && typingDiv.parentNode) {
                typingDiv.parentNode.removeChild(typingDiv);
            }
            if (data.success && data.data && data.data.reply) {
                appendMessage(data.data.reply, 'bot');
            } else if (data.error && data.error.message) {
                appendMessage(data.error.message, 'bot');
            } else {
                appendMessage("I am here to assist with product inquiries and order questions. How can I help?", 'bot');
            }
        })
        .catch(err => {
            if (typingDiv && typingDiv.parentNode) {
                typingDiv.parentNode.removeChild(typingDiv);
            }
            appendMessage("Welcome to HarmeshMart! You can ask about order returns, shipping timelines, or our bestselling products.", 'bot');
        });
    }

    if (sendBtn && input) {
        sendBtn.addEventListener('click', function () {
            sendQuery(input.value);
        });

        input.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                sendQuery(input.value);
            }
        });
    }

    // Attach FAQ quick pills
    document.querySelectorAll('.hm-chat-faq-pill').forEach(btn => {
        btn.addEventListener('click', function () {
            const query = this.getAttribute('data-query');
            if (query) sendQuery(query);
        });
    });
}

// ================= CART AJAX UPDATES =================
function initCartQuantityHandlers() {
    const qtySelects = document.querySelectorAll('.hm-cart-qty-select');
    qtySelects.forEach(sel => {
        sel.addEventListener('change', function () {
            const form = this.closest('form');
            if (form) {
                form.submit();
            }
        });
    });
}

// ================= GLOBAL HELPERS =================
function toggleWishlist(productId, btn) {
    const base = (typeof window.contextPath !== 'undefined') ? window.contextPath : '/HarmeshMart';
    const apiPath = base + '/wishlist/add';
    const formData = new URLSearchParams();
    formData.append('productId', productId);

    fetch(apiPath, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            if (btn) {
                btn.innerHTML = '<i class="bi bi-heart-fill text-danger"></i> Saved';
            }
            const badge = document.querySelector('.hm-wishlist-badge');
            if (badge && data.data) {
                badge.textContent = data.data;
            }
        }
    })
    .catch(() => {
        window.location.href = base + '/login';
    });
}
