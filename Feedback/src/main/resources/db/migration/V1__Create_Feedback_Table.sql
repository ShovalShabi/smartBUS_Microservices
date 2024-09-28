-- Enable the extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create the feedback table if it doesn't exist
CREATE TABLE IF NOT EXISTS feedback (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    agency VARCHAR(255),
    line_number VARCHAR(255),
    user_email VARCHAR(255),
    additional_details TEXT,
    creation_timestamp TIMESTAMP,
    rating DOUBLE PRECISION
);
