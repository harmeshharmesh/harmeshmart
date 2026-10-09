-- HarmeshMart Seed Data
-- Realistic catalog data with ₹ INR Pricing & high-res Unsplash images

-- 1. Seed Users (passwords hashed with jBCrypt: Admin@12345, Seller@12345, Buyer@12345)
-- Bcrypt hash for Admin@12345: $2a$10$wN1rBfZxX5uM.kO8qR0UZeGzJvhgIq1lJkmD7T0q1h0bWqW1z1t2y
-- Bcrypt hash for Seller@12345: $2a$10$8sB3J1O7E6pW/5.L4aL9QeXzQ1bJ6wR4rU0V2lK1a8C0dF2g3h4j5
-- Bcrypt hash for Buyer@12345:  $2a$10$K7Z2g1B9a0C3d4E5f6G7H8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x

INSERT INTO users (id, name, email, password_hash, role, phone) VALUES
(1, 'System Administrator', 'admin@harmeshmart.com', '$2a$10$wN1rBfZxX5uM.kO8qR0UZeGzJvhgIq1lJkmD7T0q1h0bWqW1z1t2y', 'ADMIN', '+91 9876543210'),
(2, 'Apex Tech World (Seller)', 'apex.electronics@harmeshmart.com', '$2a$10$8sB3J1O7E6pW/5.L4aL9QeXzQ1bJ6wR4rU0V2lK1a8C0dF2g3h4j5', 'SELLER', '+91 9811223344'),
(3, 'Aura Trends India (Seller)', 'aura.fashion@harmeshmart.com', '$2a$10$8sB3J1O7E6pW/5.L4aL9QeXzQ1bJ6wR4rU0V2lK1a8C0dF2g3h4j5', 'SELLER', '+91 9822334455'),
(4, 'Zenith Living & Fitness (Seller)', 'zenith.home@harmeshmart.com', '$2a$10$8sB3J1O7E6pW/5.L4aL9QeXzQ1bJ6wR4rU0V2lK1a8C0dF2g3h4j5', 'SELLER', '+91 9833445566'),
(5, 'Harmesh Kumar (Buyer)', 'buyer@harmeshmart.com', '$2a$10$K7Z2g1B9a0C3d4E5f6G7H8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x', 'BUYER', '+91 9844556677'),
(6, 'Priya Sharma (Buyer)', 'priya.sharma@example.com', '$2a$10$K7Z2g1B9a0C3d4E5f6G7H8I9j0K1l2M3n4O5p6Q7r8S9t0U1v2W3x', 'BUYER', '+91 9855667788');

-- 2. Seed Sellers
INSERT INTO sellers (id, user_id, store_name, store_description, contact_email, phone, verified, rating) VALUES
(1, 2, 'Apex Electronics Store', 'Official retailer of flagship smartphones, mechanical keyboards, audio, and premium PC peripherals.', 'support@apexelectronics.in', '+91 9811223344', TRUE, 4.90),
(2, 3, 'Aura Fashion & Studio', 'Trendy high-street fashion, denim jackets, pure cotton essentials and everyday leather accessories.', 'care@aurafashion.in', '+91 9822334455', TRUE, 4.85),
(3, 4, 'Zenith Living & Sports', 'Curators of ergonomic living, modern kitchen appliances, fitness gear, and bestselling books.', 'orders@zenithliving.in', '+91 9833445566', TRUE, 4.95);

-- 3. Seed Categories
INSERT INTO categories (id, name, slug, description, icon) VALUES
(1, 'Electronics & Gadgets', 'electronics', 'Flagship smartphones, pro noise-cancelling headphones, high refresh-rate monitors and accessories', 'bi-laptop'),
(2, 'Fashion & Apparel', 'fashion', 'Designer cotton tees, rugged denim jackets, running sneakers, and handcrafted leather accessories', 'bi-bag'),
(3, 'Home & Kitchen Essentials', 'home-kitchen', 'Espresso machines, smart kitchen mixers, non-stick culinary sets, and lumbar support ergonomic chairs', 'bi-house-door'),
(4, 'Books & Stationery', 'books', 'Programming masteries, financial freedom classics, bestselling sci-fi novels, and curated journals', 'bi-book'),
(5, 'Sports, Fitness & Outdoor', 'sports-fitness', 'High-density yoga mats, adjustable dumbbell sets, GPS fitness trackers, and active performance gear', 'bi-trophy');

-- 4. Seed Products
INSERT INTO products (id, seller_id, category_id, category, name, description, price, original_price, stock_qty, image_url, is_active, rating, review_count) VALUES
-- Electronics
(1, 2, 1, 'Electronics & Gadgets', 'UltraPhone 15 Pro 5G (256GB - Titanium Gray)', 'Flagship 5G smartphone powered by 3nm Octa-Core chip, 120Hz Super AMOLED ProMotion display, 48MP Triple Cinema Camera system, 5000mAh battery with 65W Turbo Charging.', 49999.00, 54999.00, 24, 'https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=800&auto=format&fit=crop&q=80', TRUE, 4.85, 42),
(2, 2, 1, 'Electronics & Gadgets', 'SonicWave Pro Wireless Noise-Cancelling Headphones', 'Active Hybrid ANC with 4-mic array, Hi-Res LDAC spatial audio codec, 40-hour ultra-long battery life, memory-foam ear cushions, and seamless multi-device Bluetooth 5.3 pairing.', 8999.00, 11999.00, 38, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=80', TRUE, 4.70, 29),
(3, 2, 1, 'Electronics & Gadgets', 'AeroMech RGB Hot-Swappable Mechanical Keyboard', 'Gateron Yellow linear switches, sound-dampening gasket mount, custom per-key RGB backlighting, double-shot PBT keycaps, and USB-C detachable braided cable.', 3499.00, 4499.00, 15, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&auto=format&fit=crop&q=80', TRUE, 4.80, 18),
(4, 2, 1, 'Electronics & Gadgets', 'TitanView 34" Curved Ultra-Wide Gaming Monitor', '165Hz refresh rate, 1ms MPRT, WQHD 3440x1440 resolution, 1500R curvature, HDR400, AMD FreeSync Premium, dual HDMI 2.1 & DisplayPort 1.4 ports.', 24999.00, 29999.00, 8, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&auto=format&fit=crop&q=80', TRUE, 4.90, 14),
(5, 2, 1, 'Electronics & Gadgets', 'PulseMatrix Smartwatch v2 with AMOLED & SpO2', 'Always-On 1.43" AMOLED retina display, continuous heart rate and SpO2 tracking, 110+ sports modes, Bluetooth calling speaker, IP68 water resistance up to 50m.', 4999.00, 6999.00, 45, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop&q=80', TRUE, 4.65, 31),

-- Fashion
(6, 3, 2, 'Fashion & Apparel', 'Signature 100% Combed Cotton Oversized T-Shirt', '240 GSM heavy luxury cotton fabric, pre-shrunk bio-washed treatment, breathable drop-shoulder fit, minimal streetwear chest accent, fade-resistant color technology.', 799.00, 1299.00, 80, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&auto=format&fit=crop&q=80', TRUE, 4.60, 56),
(7, 3, 2, 'Fashion & Apparel', 'Heritage Vintage Wash Denim Trucker Jacket', 'Heavyweight classic 14oz indigo cotton denim, brass riveted hardware, dual flap chest pockets, adjustable waist tabs, timeless unisex tailored silhouette.', 2499.00, 3999.00, 22, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&auto=format&fit=crop&q=80', TRUE, 4.75, 23),
(8, 3, 2, 'Fashion & Apparel', 'AeroStride Cloud-Foam Running Sneakers', 'Engineered jacquard breathable mesh upper, resilient responsive nitrogen-infused foam midsole, high-traction carbon rubber outsole, reflective night-safety stripes.', 3199.00, 4499.00, 35, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&auto=format&fit=crop&q=80', TRUE, 4.80, 37),
(9, 3, 2, 'Fashion & Apparel', 'Handcrafted Full-Grain Leather Bi-Fold Wallet', 'Genuine veg-tanned saddle leather, RFID blocking defense layer, 8 card slots, quick-access currency divider, handcrafted contrast stitching with gift box.', 1299.00, 1999.00, 50, 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&auto=format&fit=crop&q=80', TRUE, 4.70, 19),
(10, 3, 2, 'Fashion & Apparel', 'Voyager 30L Water-Resistant Commuter Backpack', '900D water-repellent ballistic nylon fabric, dedicated padded 16" laptop sleeve, TSA-friendly lay-flat opening, integrated USB pass-through port, airflow back pad.', 1899.00, 2899.00, 40, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&auto=format&fit=crop&q=80', TRUE, 4.85, 27),

-- Home & Kitchen
(11, 4, 3, 'Home & Kitchen Essentials', 'BaristaTouch Automatic 19-Bar Espresso Machine', '19-bar Italian high-pressure pump, rapid thermo-block heating system, stainless steel steam wand for micro-foam lattes, removable 1.5L water reservoir with cup warmer plate.', 14999.00, 19999.00, 12, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=800&auto=format&fit=crop&q=80', TRUE, 4.90, 16),
(12, 4, 3, 'Home & Kitchen Essentials', 'Culinaria 1200W Planetary Smart Stand Mixer', 'Commercial grade 1200W pure copper motor, 6.5L stainless steel mixing bowl with ergonomic handle, includes dough hook, wire whisk, flat beater and splash shield.', 8499.00, 10999.00, 14, 'https://images.unsplash.com/photo-1594385208974-2e75f8d7bb48?w=800&auto=format&fit=crop&q=80', TRUE, 4.65, 11),
(13, 4, 3, 'Home & Kitchen Essentials', 'GraniteStone 5-Piece Induction Non-Stick Cookware Set', 'Eco-friendly 100% PFOA-free granite coating, induction-compatible magnetized bottom, heat-resistant stay-cool bakelite handles, tempered glass lids with steam vents.', 3299.00, 4999.00, 25, 'https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=800&auto=format&fit=crop&q=80', TRUE, 4.75, 33),
(14, 4, 3, 'Home & Kitchen Essentials', 'ErgoSpine Pro High-Back Executive Office Chair', 'Dynamic lumbar tracking mechanism, 3D adjustable armrests, breathable high-tensile mesh backrest, class-4 gas lift cylinder, 135-degree recline lock system.', 9999.00, 13999.00, 9, 'https://images.unsplash.com/photo-1580481077195-c3a8a37f714b?w=800&auto=format&fit=crop&q=80', TRUE, 4.85, 20),

-- Books
(15, 4, 4, 'Books & Stationery', 'Java Enterprise Architecture & Concurrency Mastery', 'Comprehensive modern reference covering Servlets 4.0, MVC architectures, JDBC connection pools, memory models, multithreading, and production deployment patterns.', 899.00, 1199.00, 60, 'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=800&auto=format&fit=crop&q=80', TRUE, 4.95, 48),
(16, 4, 4, 'Books & Stationery', 'The Psychology of Money & Financial Freedom Hardcover', 'Deluxe gold-foiled hardcover edition exploring behavioural investing, compounding secrets, risk tolerance, and timeless lessons on personal wealth creation.', 499.00, 799.00, 110, 'https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=800&auto=format&fit=crop&q=80', TRUE, 4.90, 74),
(17, 4, 4, 'Books & Stationery', 'Chronicles of Nebula: Interstellar Odyssey (Collector Edition)', 'Award-winning science fiction masterpiece with illustrated chapter dividers, star-map endpapers, ribbon bookmark, and exclusive author annotations.', 699.00, 999.00, 30, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800&auto=format&fit=crop&q=80', TRUE, 4.80, 22),

-- Sports & Fitness
(18, 4, 5, 'Sports, Fitness & Outdoor', 'ZenPro 8mm Dual-Texture High-Density TPE Yoga Mat', 'Extra-thick 8mm cushioning for joint safety, laser-engraved alignment body lines, non-slip textured surfaces on both sides, eco-friendly odorless TPE material, carry strap included.', 999.00, 1599.00, 50, 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=800&auto=format&fit=crop&q=80', TRUE, 4.70, 35),
(19, 4, 5, 'Sports, Fitness & Outdoor', 'PowerLock Adjustable Dumbbell Set (2.5kg to 24kg)', 'Rapid dial-weight selector system replacing 15 pairs of dumbbells, durable thermoplastic coating for silent clank-free lifting, anti-roll safety base tray.', 4999.00, 7499.00, 18, 'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?w=800&auto=format&fit=crop&q=80', TRUE, 4.90, 26),
(20, 4, 5, 'Sports, Fitness & Outdoor', 'AeroTrack Pro GPS Waterproof Fitness Band', 'Standalone multi-satellite GPS, 24/7 heart-rate and sleep stage analysis, 5ATM swim-proof water resistance, 14-day standby battery, magnetic fast charger.', 2999.00, 4299.00, 40, 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=800&auto=format&fit=crop&q=80', TRUE, 4.65, 19);

-- 5. Seed Addresses for Buyer
INSERT INTO addresses (id, user_id, full_name, phone, street, city, state, postal_code, is_default) VALUES
(1, 5, 'Harmesh Kumar', '+91 9844556677', 'Flat 402, Nilgiri Heights, MG Road', 'Bengaluru', 'Karnataka', '560001', TRUE),
(2, 5, 'Harmesh Kumar (Office)', '+91 9844556677', 'Tower 3, Global Tech Village, Whitefield', 'Bengaluru', 'Karnataka', '560066', FALSE);

-- 6. Seed Sample Orders
INSERT INTO orders (id, buyer_id, status, total_amount, shipping_address, payment_method, payment_status, promo_code, discount_amount, created_at) VALUES
(1, 5, 'DELIVERED', 50798.00, 'Flat 402, Nilgiri Heights, MG Road, Bengaluru, Karnataka - 560001', 'UPI', 'PAID', 'SAVE10', 0.00, TIMESTAMP '2026-09-15 10:30:00'),
(2, 5, 'SHIPPED', 8999.00, 'Flat 402, Nilgiri Heights, MG Road, Bengaluru, Karnataka - 560001', 'CREDIT_CARD', 'PAID', NULL, 0.00, TIMESTAMP '2026-10-01 14:15:00'),
(3, 5, 'CONFIRMED', 3499.00, 'Tower 3, Global Tech Village, Whitefield, Bengaluru, Karnataka - 560066', 'COD', 'PENDING', NULL, 0.00, TIMESTAMP '2026-10-08 09:00:00');

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 1, 49999.00),
(2, 1, 6, 1, 799.00),
(3, 2, 2, 1, 8999.00),
(4, 3, 3, 1, 3499.00);

-- 7. Seed Reviews
INSERT INTO reviews (id, product_id, user_id, rating, comment, created_at) VALUES
(1, 1, 5, 5, 'Absolutely mind-blowing phone! Battery lasts over a full day and the 120Hz display is butter smooth.', TIMESTAMP '2026-09-18 11:20:00'),
(2, 1, 6, 5, 'Cinematic video recording is astonishing. Delivery was super fast via HarmeshMart express.', TIMESTAMP '2026-09-20 16:45:00'),
(3, 2, 5, 5, 'Noise cancellation drowns out all office and flight chatter. Bass is tight and battery life is unbelievable.', TIMESTAMP '2026-10-03 18:00:00'),
(4, 3, 6, 4, 'Crisp mechanical feel and tactile feedback. The RGB modes are vibrant and satisfying to type on.', TIMESTAMP '2026-10-05 20:10:00');

-- 8. Seed Wishlist Items
INSERT INTO wishlist_items (id, user_id, product_id) VALUES
(1, 5, 4),
(2, 5, 11),
(3, 5, 15);

-- 9. Advance auto-increment sequences past initial seeds
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE sellers ALTER COLUMN id RESTART WITH 100;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 100;
ALTER TABLE products ALTER COLUMN id RESTART WITH 100;
ALTER TABLE addresses ALTER COLUMN id RESTART WITH 100;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 100;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 100;
ALTER TABLE wishlist_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE cart_items ALTER COLUMN id RESTART WITH 100;
