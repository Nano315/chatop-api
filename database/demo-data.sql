-- ============================================================================
-- ChâTop - demo data (3 users, 8 rentals, 3 messages)
--
-- Run it on chatop_db after the schema script (MySQL Workbench or mysql CLI).
-- It can be run again at any time: it first removes the previous demo data.
--
-- Demo accounts (same password for all: test!31, like the Mockoon test account):
--   test@test.com          Test TEST     owns 3 rentals
--   marie.dupont@test.com  Marie Dupont  owns 3 rentals
--   lucas.martin@test.com  Lucas Martin  owns 2 rentals
-- Only the BCrypt hash of the password is stored.
-- Pictures: Unsplash photos (free to use), loaded from images.unsplash.com.
-- ============================================================================

USE chatop_db;

-- The deletes below filter on indexed columns through subqueries
SET SQL_SAFE_UPDATES = 0;

START TRANSACTION;

-- 1. Remove the previous demo data
DELETE FROM messages
WHERE user_id IN (SELECT id FROM users WHERE email IN ('test@test.com', 'marie.dupont@test.com', 'lucas.martin@test.com'))
   OR rental_id IN (SELECT r.id FROM rentals r JOIN users u ON u.id = r.owner_id
                    WHERE u.email IN ('test@test.com', 'marie.dupont@test.com', 'lucas.martin@test.com'));

DELETE FROM rentals
WHERE owner_id IN (SELECT id FROM users WHERE email IN ('test@test.com', 'marie.dupont@test.com', 'lucas.martin@test.com'));

DELETE FROM users
WHERE email IN ('test@test.com', 'marie.dupont@test.com', 'lucas.martin@test.com');

-- 2. Users
INSERT INTO users (email, name, password, created_at, updated_at) VALUES
  ('test@test.com',         'Test TEST',    '$2a$10$UzkyPZfOZDClcoL9Gb8A5uJ4KaGwaUjWQ..CPWTDM6dbercG6340m', '2026-01-15 10:00:00', '2026-01-15 10:00:00'),
  ('marie.dupont@test.com', 'Marie Dupont', '$2a$10$UzkyPZfOZDClcoL9Gb8A5uJ4KaGwaUjWQ..CPWTDM6dbercG6340m', '2026-02-03 09:30:00', '2026-02-03 09:30:00'),
  ('lucas.martin@test.com', 'Lucas Martin', '$2a$10$UzkyPZfOZDClcoL9Gb8A5uJ4KaGwaUjWQ..CPWTDM6dbercG6340m', '2026-03-21 18:45:00', '2026-03-21 18:45:00');

SET @test  = (SELECT id FROM users WHERE email = 'test@test.com');
SET @marie = (SELECT id FROM users WHERE email = 'marie.dupont@test.com');
SET @lucas = (SELECT id FROM users WHERE email = 'lucas.martin@test.com');

-- 3. Rentals (price per night, in euros)
INSERT INTO rentals (name, surface, price, picture, description, owner_id, created_at, updated_at) VALUES
  ('Villa Océane - Biarritz', 180, 320,
   'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&q=80&auto=format&fit=crop',
   'Villa contemporaine avec piscine à 5 minutes de la Grande Plage. 4 chambres, grande terrasse et vue dégagée sur l''océan.',
   @marie, '2026-02-10 14:00:00', '2026-05-02 09:15:00'),
  ('Villa contemporaine - Bidart', 140, 260,
   'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800&q=80&auto=format&fit=crop',
   'Maison lumineuse avec piscine privée, jardin paysager et baies vitrées. Idéale pour les familles, plage de l''Uhabia à pied.',
   @marie, '2026-02-18 11:20:00', '2026-02-18 11:20:00'),
  ('Maison en bois - Espelette', 120, 150,
   'https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800&q=80&auto=format&fit=crop',
   'Chalet chaleureux au cœur du village, entouré de collines. Cheminée, jardin et calme absolu, à 25 minutes de la côte.',
   @marie, '2026-03-05 16:40:00', '2026-03-05 16:40:00'),
  ('Maison de famille - Anglet', 160, 210,
   'https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800&q=80&auto=format&fit=crop',
   'Grande maison avec véranda et jardin arboré, proche de la forêt du Pignada et des plages de la Chambre d''Amour.',
   @test, '2026-01-20 10:30:00', '2026-04-11 08:00:00'),
  ('Appartement cosy - Bayonne', 62, 85,
   'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800&q=80&auto=format&fit=crop',
   'Appartement rénové dans le Petit Bayonne, au bord de la Nive. Deux chambres, salon lumineux, commerces au pied de l''immeuble.',
   @test, '2026-01-28 19:05:00', '2026-01-28 19:05:00'),
  ('Studio lumineux - Saint-Jean-de-Luz', 32, 70,
   'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800&q=80&auto=format&fit=crop',
   'Studio moderne et bien équipé, à 200 m de la baie et du port. Parfait pour un couple ou un séjour en solo.',
   @test, '2026-02-25 13:10:00', '2026-02-25 13:10:00'),
  ('Maison coloniale - Hendaye', 200, 280,
   'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800&q=80&auto=format&fit=crop',
   'Belle demeure avec piscine et jardin tropical, face à la baie de Txingudi. 5 chambres, idéale pour un séjour entre amis.',
   @lucas, '2026-03-28 09:00:00', '2026-03-28 09:00:00'),
  ('Villa d''architecte - Guéthary', 170, 350,
   'https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800&q=80&auto=format&fit=crop',
   'Villa design avec piscine à débordement, à deux pas du spot de surf de Parlementia. Prestations haut de gamme.',
   @lucas, '2026-04-02 17:30:00', '2026-04-15 12:00:00');

-- 4. Messages sent to owners
INSERT INTO messages (rental_id, user_id, message, created_at, updated_at) VALUES
  ((SELECT id FROM rentals WHERE name = 'Villa Océane - Biarritz'), @test,
   'Bonjour, la villa est-elle disponible du 12 au 19 juillet pour 6 personnes ?', '2026-05-10 20:12:00', '2026-05-10 20:12:00'),
  ((SELECT id FROM rentals WHERE name = 'Appartement cosy - Bayonne'), @lucas,
   'Bonjour, est-il possible d''arriver tard le soir, vers 22h ?', '2026-05-14 08:47:00', '2026-05-14 08:47:00'),
  ((SELECT id FROM rentals WHERE name = 'Villa d''architecte - Guéthary'), @marie,
   'Bonjour, les animaux de compagnie sont-ils acceptés ?', '2026-05-20 15:30:00', '2026-05-20 15:30:00');

COMMIT;

SET SQL_SAFE_UPDATES = 1;
