-- Event store with indexes aligned to com.technicalbeep.techeventsmap.entity.Event
CREATE TABLE events (
    id UUID NOT NULL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    date_time TIMESTAMPTZ NOT NULL,
    venue VARCHAR(500) NOT NULL,
    city VARCHAR(200) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    category VARCHAR(32) NOT NULL,
    featured BOOLEAN NOT NULL,
    published BOOLEAN NOT NULL,
    show_on_map BOOLEAN NOT NULL,
    external_url VARCHAR(2048)
);

CREATE INDEX idx_events_date_time ON events (date_time);
CREATE INDEX idx_events_category ON events (category);
CREATE INDEX idx_events_city ON events (city);
CREATE INDEX idx_events_lat_lon ON events (latitude, longitude);
