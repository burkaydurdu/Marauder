-- :name create-country! :! :n
-- :doc creates a new country record
INSERT INTO countries
(name, code)
VALUES (:name, :code)
ON CONFLICT DO NOTHING

-- :name get-all-country :? :*
-- :doc retrieves a country record given the id
SELECT * FROM countries

-- :name get-country :? :1
-- :doc retrieves a country record given the code
SELECT * FROM countries
WHERE code = :code

-- :name create-city! :! :n
-- :doc creates a new city record
INSERT INTO cities
(name, country_id)
VALUES (:name, :country_id)
ON CONFLICT DO NOTHING

-- :name get-all-city :? :*
-- :doc get all city
SELECT * FROM cities

-- :name get-city :? :*
-- :doc retrieves a city record given the country_id
SELECT * FROM cities
WHERE country_id = :country_id

-- :name create-district! :! :n
-- :doc creates a new district record
INSERT INTO districts
(name, city_id)
VALUES (:name, :city_id)
ON CONFLICT DO NOTHING

-- :name get-district :? :*
-- :doc retrieves a city record given the city_id
SELECT * FROM districts
WHERE city_id = :city_id
