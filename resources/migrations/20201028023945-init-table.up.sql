SET TIME ZONE 'UTC';

--;; Country

CREATE TABLE countries
(id UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL PRIMARY KEY,
 name VARCHAR(50) UNIQUE NOT NULL,
 code VARCHAR(2) UNIQUE NOT NULL);

 --;; City

CREATE TABLE cities
(id UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL PRIMARY KEY,
 name VARCHAR(50) UNIQUE NOT NULL,
 country_id UUID NOT NULL REFERENCES countries(id));

 --;; District

CREATE TABLE districts
(id UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL PRIMARY KEY,
 name VARCHAR(50) NOT NULL,
 city_id UUID NOT NULL REFERENCES cities(id),
 UNIQUE(name, city_id));
