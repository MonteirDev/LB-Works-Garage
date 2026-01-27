CREATE TABLE activities (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
    project_id UUID,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);