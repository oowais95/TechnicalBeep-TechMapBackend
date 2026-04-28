-- Narrow partial index for GET /events/featured: featured + public + on-map, ordered by date_time
CREATE INDEX idx_events_featured_public_datetime ON events (date_time)
    WHERE published = true AND show_on_map = true AND featured = true;
