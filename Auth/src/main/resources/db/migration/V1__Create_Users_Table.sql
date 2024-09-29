-- Create the feedback table if it doesn't exist
CREATE TABLE users (
    organization_email VARCHAR(255) PRIMARY KEY,  -- Assuming the email is unique and acts as the primary key
    first_name VARCHAR(100) NOT NULL,             -- First name column, cannot be empty
    surname VARCHAR(100) NOT NULL,                -- Surname column, cannot be empty
    company VARCHAR(255) NOT NULL,                -- Company column, cannot be empty
    password VARCHAR(255) NOT NULL,               -- Password column, applying length of 255 to accommodate encoded passwords
    role VARCHAR(50) NOT NULL                     -- Role column, storing the role as a string
);
