-- Supports frequent public list: published + on map + time ordering (UTC TIMESTAMPTZ).
CREATE INDEX idx_events_public_datetime ON events (date_time)
    WHERE published = true AND show_on_map = true;

-- Public list filtered by category (still partial: only on-map published rows).
CREATE INDEX idx_events_public_category_datetime ON events (category, date_time)
    WHERE published = true AND show_on_map = true;
